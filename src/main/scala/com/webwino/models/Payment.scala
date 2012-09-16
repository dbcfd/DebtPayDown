package com.webwino.models

object LiabilityPayment {
  def apply(liability:Liability, payment:Double): LiabilityPayment = {
    val interestAmount = liability.interestAccrued
    val principalAmount = payment - interestAmount
    new LiabilityPayment(interestAmount, principalAmount)
  }
}

class LiabilityPayment private (val interestAmount:Double, val principalAmount:Double) {

}

object PlanPayment {
  def apply(requiredPayment:Double, extraPayment:Double): PlanPayment = {
    new PlanPayment(requiredPayment, extraPayment)
  }
}

class PlanPayment private (val requiredPayment:Double, val extraPayment:Double) {
  def paysExtra : Boolean = extraPayment > 0
  def totalPayment : Double = requiredPayment + extraPayment
}
