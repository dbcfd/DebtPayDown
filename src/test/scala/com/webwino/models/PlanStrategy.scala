package com.webwino.models

import org.specs2.mutable._

class PlanStrategySpec extends Specification {
  "The PlanStrategy" should {
    "create a strategy" in {
      PlanStrategy.generatePlan must not beNull
    }
  }
}

class PayOneStrategySpec extends Specification {
  "The PayOneStrategy" should {
    "create a strategy" in {
      new PayOneStrategy() must not beNull
    }
    "have no strategies with extra payments if extra is 0" in {
      val strategy = new PayOneStrategy()
      val payments = strategy.generatePlan(List(Debt(100, 0.01), Debt(200, 0.01)), 0)
      val paymentsWithExtra = payments filter ( _.paysExtra)
      paymentsWithExtra must be empty
    }
    "have one strategy with extra payment if extra is greater than 0" in {
      val strategy = new PayOneStrategy()
      val payments = strategy.generatePlan(List(Debt(100, 0.01), Debt(200, 0.01)), 100)
      val paymentsWithExtra = payments filter ( _.paysExtra)
      paymentsWithExtra must have length(1)
    }
  }
}

class PayEqualStrategySpec extends Specification {
  "The PayEqualStrategy" should {
    "create a strategy" in {
      new PayEqualStrategy() must not beNull
    }
    "have no strategies with extra payments if extra is 0" in {
      val strategy = new PayEqualStrategy()
      val payments = strategy.generatePlan(List(Debt(100, 0.01), Debt(200, 0.01)), 0)
      val paymentsWithExtra = payments filter ( _.paysExtra)
      paymentsWithExtra must be empty
    }
    "have all strategies with equal payments if extra is greater than 0" in {
      val strategy = new PayEqualStrategy()
      val payments = strategy.generatePlan(List(Debt(100, 0.01), Debt(200, 0.01)), 100)
      val paymentsWithExtra = payments filter ( payment => {
        val hasCorrectExtra = payment.paysExtra && payment.extraPayment == 50
        hasCorrectExtra
      })
      paymentsWithExtra must have length(payments.length)
    }
  }
}

class PayEqualToSomeStrategySpec extends Specification {
  "The PayEqualToSomeStrategy" should {
    "create a strategy" in {
      new PayEqualToSomeStrategy() must not beNull
    }
    "have no strategies with extra payments if extra is 0" in {
      val strategy = new PayEqualToSomeStrategy()
      val payments = strategy.generatePlan(List(Debt(100, 0.01), Debt(200, 0.01)), 0)
      val paymentsWithExtra = payments filter ( _.paysExtra)
      paymentsWithExtra must be empty
    }
    "have some strategies with extra payments if extra is greater than 0" in {
      val strategy = new PayEqualToSomeStrategy()
      val payments = strategy.generatePlan(List(Debt(100, 0.01), Debt(200, 0.01), Debt(300, 0.01)), 100)
      val paymentsWithExtra = payments filter ( _.paysExtra)
      (paymentsWithExtra.length > 0 && paymentsWithExtra.length < payments.length)
    }
    "have a strategies with extra payments if extra is greater than 0 and one debt is listed" in {
      val strategy = new PayEqualToSomeStrategy()
      val payments = strategy.generatePlan(List(Debt(100, 0.01)), 100)
      val paymentsWithExtra = payments filter ( _.paysExtra)
      (paymentsWithExtra.length > 0 && paymentsWithExtra.length == payments.length)
    }
  }
}

class PayRandomStrategySpec extends Specification {
  "The PayRandomStrategy" should {
    "create a strategy" in {
      new PayRandomStrategy() must not beNull
    }
    "have no strategies with extra payments if extra is 0" in {
      val strategy = new PayRandomStrategy()
      val payments = strategy.generatePlan(List(Debt(100, 0.01), Debt(200, 0.01)), 0)
      val paymentsWithExtra = payments filter ( _.paysExtra)
      paymentsWithExtra must be empty
    }
    "have a strategy with extra payment if extra is greater than 0" in {
      val strategy = new PayRandomStrategy()
      val payments = strategy.generatePlan(List(Debt(100, 0.01), Debt(200, 0.01), Debt(300, 0.01)), 100)
      val paymentsWithExtra = payments filter ( _.paysExtra)
      (paymentsWithExtra.length > 0)
    }
  }
}