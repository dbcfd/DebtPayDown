package com.webwino.models

import org.specs2.mutable._

class EvolveDebtPlanSpec extends Specification {
  "The EvolveDebtPlan" should {
    "create an object" in {
      new EvolveDebtPlan(DebtPlan.apply(Nil, 0)) must not beNull
    }
  }
}