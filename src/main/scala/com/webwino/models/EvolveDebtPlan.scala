package com.webwino.models

import com.webwino.evolution.Evolvable

/*
 * Debt plan that can be evolved
 */
class EvolveDebtPlan(val plan:DebtPlan) extends Evolvable {

  override def generateOffspring(shouldMutate:Boolean) : Evolvable = {
    val newPlan = DebtPlan.apply(plan)
    if (shouldMutate) newPlan.updateStrategy
    new EvolveDebtPlan(newPlan)
  }

  override def determineFitness : Double = {
    math.max(0.0, 1.0 - plan.payoffAmount / plan.amountOwed)
  }
}
