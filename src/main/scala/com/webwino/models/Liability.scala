package com.webwino.models

/**
 * Hold a liability
 */
object Liability {
  def apply(liability:Liability, payment:Double): Liability = {
    new Liability(liability.payoffAmount - payment, liability.periodInterestRate)
  }
  def apply(principal:Double, periodInterestRate:Double): Liability = {
    new Liability(principal, periodInterestRate)
  }
}
class Liability private (val amountOwed: Double, val periodInterestRate: Double) {
  val interestAccrued = amountOwed * periodInterestRate
  val payoffAmount = amountOwed + interestAccrued

  def applyPayment(paymentAmount: Double): (Liability, Double) = {
    (new Liability(payoffAmount, periodInterestRate), paymentAmount)
  }
}
