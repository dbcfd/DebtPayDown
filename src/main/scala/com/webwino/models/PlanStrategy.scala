package com.webwino.models

import scala.util.Random
import collection.mutable.ArrayBuffer

/**
 * A strategy for paying down debts
 */
trait PlanStrategy {
	def generatePlan(debts:List[Debt], extraPaymentAmount:Double) : IndexedSeq[PlanPayment]
	def name : String
	def description : String
}

object PlanStrategy {
	val rng = new Random()
	private val strategies:Vector[PlanStrategy] = Vector(
		new PayOneStrategy(),
		new PayEqualStrategy(),
		new PayEqualToSomeStrategy(),
		new PayRandomStrategy()
	)
	
	def generatePlan : PlanStrategy = {
    val index:Int = rng.nextInt(strategies.size)
		strategies(index)
	}
}

/**
 * Actual strategies implemented
 */
class PayOneStrategy extends PlanStrategy {
	def name : String = "PayOneStrategy"
	def description : String = "All extra payment is applied to a single debt"

	def generatePlan(debts:List[Debt], extraPaymentAmount:Double) : IndexedSeq[PlanPayment] = {
		//pick the debt to pay first
		val payments:ArrayBuffer[PlanPayment] = ArrayBuffer() ++ debts map ( (debt:Debt) => PlanPayment(debt.minimumPayment, 0) )
    if(extraPaymentAmount > 0)
    {
      if(payments.size == 1)
      {
        payments(0) = PlanPayment(payments(0).requiredPayment, extraPaymentAmount)
      }
      else
      {
        val index = PlanStrategy.rng.nextInt(debts.size)
		    payments(index) = PlanPayment(payments(index).requiredPayment, extraPaymentAmount)
      }
    }
    payments.toIndexedSeq
	}
}

class PayEqualStrategy extends PlanStrategy {
	def name : String = "PayEqualStrategy"
	def description : String = "Split additional payment between all debts"
	
	def generatePlan(debts:List[Debt], extraPaymentAmount:Double) : IndexedSeq[PlanPayment] = {
		//pay all debts with the same amount of extra
		val extra = extraPaymentAmount / debts.size
		(debts map ( (debt:Debt) => PlanPayment(debt.minimumPayment, extra) )).toIndexedSeq
	}
}

class PayEqualToSomeStrategy extends PlanStrategy {
	def name : String = "PayEqualToSomeStrategy"
	def description : String = "Extra payment is split up amongst some of the debts, in equal amounts"
	
	def generatePlan(debts:List[Debt], extraPaymentAmount:Double) : IndexedSeq[PlanPayment] = {
		val payments:ArrayBuffer[PlanPayment] = ArrayBuffer() ++ debts map ( (debt:Debt) => PlanPayment(debt.minimumPayment, 0) )
    if(extraPaymentAmount > 0)
    {
      if(payments.size == 1)
      {
        payments(0) = PlanPayment(payments(0).requiredPayment, extraPaymentAmount)
      }
      else
      {
        //pay some of the debts with an equal amount
        val debtCount = math.max(1, PlanStrategy.rng.nextInt(debts.size))
        val indices = Random.shuffle(debts.indices)
        val debtsWithExtra = indices.take(debtCount)
        val extra = extraPaymentAmount / debtCount
        debtsWithExtra foreach ( (index:Int) => payments(index) = PlanPayment(payments(index).requiredPayment, extra) )
      }
    }
		payments.toIndexedSeq
	}
}

class PayRandomStrategy extends PlanStrategy {
	def name : String = "PayOneStrategy"
	def description : String = "All extra payment is applied to a single debt"
	
	def generatePlan(debts:List[Debt], extraPaymentAmount:Double) : IndexedSeq[PlanPayment] = {
		val payments:ArrayBuffer[PlanPayment] = ArrayBuffer() ++ debts map ( (debt:Debt) => PlanPayment(debt.minimumPayment, 0) )
    if(extraPaymentAmount > 0)
    {
      if(payments.size == 1)
      {
        payments(0) = PlanPayment(payments(0).requiredPayment, extraPaymentAmount)
      }
      else
      {
        val indices = Random.shuffle(debts.indices)
        var remaining = extraPaymentAmount
        indices foreach ( (index:Int) => {
          val extra = {
            if(remaining > 25.0) PlanStrategy.rng.nextDouble() * remaining
            else remaining
          }
          payments(index) = PlanPayment(payments(index).requiredPayment, extra)
          remaining -= extra
        } )
        if(remaining > 0) {
          val index = PlanStrategy.rng.nextInt(payments.size)
          val existingPayment = payments(index)
          payments(index) = PlanPayment(existingPayment.requiredPayment, existingPayment.extraPayment + remaining)
        }
      }
    }
		payments.toIndexedSeq
	}
}
		
	