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
  private var currentPaymentPlan: IndexedSeq[Double] = IndexedSeq()
  private var currentStrategy: PlanStrategy = null
  private var currentDebts: List[Debt] = debts

  val amountOwed: Double = debts.foldLeft(0.0)( (amountSum:Double, debt) => amountSum + debt.payoffAmount)

  def updateStrategy {
    if (debts.isEmpty) {
      currentPaymentPlan = IndexedSeq()
    }
    else {
      currentStrategy = PlanStrategy.generatePlan
      strategies = List(currentStrategy) ::: strategies
      currentPaymentPlan = currentStrategy.generatePlan(currentDebts, extraPaymentAmount)
    }
  }

  def applyStrategy {
    var needToUpdateStrategy = false
    var index:Int = 0
    debts foreach ( debt => {
      debt.applyPayment(currentPaymentPlan(index))
      if (debt.isPayedOff) needToUpdateStrategy = true
      index += 1
    })
    if (needToUpdateStrategy) {
      currentDebts = debts filter ((debt: Debt) => !debt.isPayedOff)
      updateStrategy
    }
  }

  def isPlanComplete:Boolean = currentDebts.isEmpty

  def payoffAmount:Double = currentDebts.foldLeft(0.0)( (payoffSum:Double, debt:Debt) => payoffSum + debt.payoffAmount)
}