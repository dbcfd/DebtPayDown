package com.webwino.models

import org.specs2.mutable._

class PlanStrategySpec extends Specification {
  "The PlanStrategy" should {
    "create a strategy" in {
      PlanStrategy.generatePlan must not beNull
    }
  }
}