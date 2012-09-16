package com.webwino.models

/**
 * Hold a set of debts and the actions taken to pay them off
 */
object DebtPlan {
  def apply(debts: List[Debt], extraPaymentAmount: Double): DebtPlan = {
    val plan = new DebtPlan(debts, extraPaymentAmount)
    plan.updateStrategy
    plan
  }
  def apply(debts: List[Debt]) : DebtPlan = {
    apply(debts, 0)
  }

  def apply(plan:DebtPlan): DebtPlan = {
    val newPlan = new DebtPlan(plan.debts, plan.extraPaymentAmount)
    newPlan.strategies = plan.strategies
    newPlan.currentPaymentPlan = plan.currentPaymentPlan
    newPlan.currentStrategy = plan.currentStrategy
    newPlan
  }
}

class DebtPlan private(val debts: List[Debt], val extraPaymentAmount: Double) {
  private var strategies: List[PlanStrategy] = List()
  private var currentPaymentPlan: IndexedSeq[PlanPayment] = IndexedSeq()
  private var currentStrategy: PlanStrategy = null
  private var currentDebts: List[Debt] = debts

  val amountOwed: Double = debts.foldLeft(0.0)( (amountSum:Double, debt) => amountSum + debt.payoffAmount)

  def updateStrategy {
    if (currentDebts.isEmpty) {
      currentPaymentPlan = IndexedSeq()
    }
    else {
      currentStrategy = PlanStrategy.generatePlan
      strategies = List(currentStrategy) ::: strategies
      currentPaymentPlan = currentStrategy.generatePlan(currentDebts, extraPaymentAmount)
    }
  }

  def applyPayments( applyMethod: (Debt, PlanPayment) => Double ):(Boolean,Double) = {
    var needToUpdateStrategy = false
    var index:Int = 0
    var paymentRemaining:Double = 0
    currentDebts foreach ( debt => {
      paymentRemaining += applyMethod(debt, currentPaymentPlan(index))
      if (debt.isPayedOff) needToUpdateStrategy = true
      index += 1
    })
    (needToUpdateStrategy, paymentRemaining)
  }

  def applyStrategy {
    var needToUpdateStrategyAndPaymentRemaining = applyPayments( (debt:Debt, payment:PlanPayment) => {
      debt.applyPayment(payment.totalPayment)
    })
    while (needToUpdateStrategyAndPaymentRemaining._1) {
      currentDebts = debts filter ((debt: Debt) => !debt.isPayedOff)
      needToUpdateStrategyAndPaymentRemaining = (!currentDebts.isEmpty, needToUpdateStrategyAndPaymentRemaining._2)
      if (needToUpdateStrategyAndPaymentRemaining._1)
      {
        updateStrategy
        var extraPaymentRemaining = needToUpdateStrategyAndPaymentRemaining._2
        needToUpdateStrategyAndPaymentRemaining = applyPayments( (debt:Debt, payment:PlanPayment) => {
          val paymentToApply = math.max(0, extraPaymentRemaining - payment.extraPayment)
          val paymentRemaining = debt.applyAdditionalPayment(paymentToApply)
          extraPaymentRemaining += paymentRemaining
          paymentRemaining
        })
      }
    }
  }

  def isPlanComplete:Boolean = currentDebts.isEmpty

  def payoffAmount:Double = currentDebts.foldLeft(0.0)( (payoffSum:Double, debt:Debt) => payoffSum + debt.payoffAmount)
}