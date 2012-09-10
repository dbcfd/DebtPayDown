package com.webwino.models

import org.specs2.mutable._

class DebtPlanSpec extends Specification {
  "The DebtPlan" should {
    "Create a DebtPlan object" in {
      new DebtPlan(Nil, 100) must not beNull
    }
    "Have a debt plan object with payoff equal to amount owed when interest is 0" in {
      new DebtPlan(List(new Debt(100, 0.0), new Debt(100, 0.0), new Debt(100, 0.0))).payoffAmount must be equal 300
    }
    "Have a debt plan object with payoff equal to amount owed plus interest when interest is not 0" {
      new DebtPlan(List(new Debt(100, 0.01), new Debt(100, 0.01), new Debt(100, 0.01))).payoffAmount must be equal 303
    }
    "Have a debt plan object reduce payoff when minimum payments are higher than interest and no extra is applied" {
      val plan = DebtPlan.apply(List(new Debt(100, 0.01, 20)), 0)
      plan.applyStrategy
      plan.payoffAmount must be less 300
    }
    "Have a debt plan object reduce payoff when minimum payments are equal to interest and extra is applied" {
      val plan = DebtPlan.apply(List(new Debt(100, 0.01)), 100)
      plan.applyStrategy
      plan.payoffAmount must be less 300
    }
    "Have a debt plan object not be paid off with minimal payments and extra" {
      val plan = DebtPlan.apply(List(new Debt(100, 0.01), 10))
      plan.applyStrategy
      plan.isPlanComplete must not be true
    }
    "have a debt plan object be paid off with minimal payments and high extra" {
      val plan = DebtPlan.apply(List(new Debt(100, 0.01), 1000))
      plan.applyStrategy
      plan.isPlanComplete must be true
    }

  }
}