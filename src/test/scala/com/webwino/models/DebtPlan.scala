package com.webwino.models

import org.specs2.mutable._

class DebtPlanSpec extends Specification {
  "The DebtPlan" should {
    "Create a DebtPlan object" in {
      DebtPlan(Nil, 100) must not beNull
    }
    "Have a debt plan object with payoff equal to amount owed when interest is 0" in {
      val payOff:Double = (DebtPlan(List(Debt(100, 0.0), Debt(100, 0.0), Debt(100, 0.0)))).payoffAmount
      payOff should beCloseTo(300.0, 0.001)
    }
    "Have a debt plan object with payoff equal to amount owed plus interest when interest is not 0" in {
      val payOff:Double = (DebtPlan(List(Debt(100, 0.12), Debt(100, 0.12), Debt(100, 0.12)))).payoffAmount
      payOff should beCloseTo(303.0, 0.001)
    }
    "Have a debt plan object reduce payoff when minimum payments are higher than interest and no extra is applied" in {
      val plan = DebtPlan(List(Debt(100, 0.01, 20)), 0)
      plan.applyStrategy
      plan.payoffAmount must beLessThan(300.0)
    }
    "Have a debt plan object reduce payoff when minimum payments are equal to interest and extra is applied" in {
      val plan = DebtPlan(List(Debt(100, 0.01)), 100)
      plan.applyStrategy
      plan.payoffAmount must beLessThan(300.0)
    }
    "Have a debt plan object not be paid off with minimal payments and extra" in {
      val plan = DebtPlan(List(Debt(100, 0.01)), 10)
      plan.applyStrategy
      plan.isPlanComplete must not be equalTo(true)
    }
    "have a debt plan object be paid off with minimal payments and high extra" in {
      val plan = DebtPlan(List(Debt(100, 0.01)), 1000)
      plan.applyStrategy
      plan.isPlanComplete must beTrue
    }

  }
}