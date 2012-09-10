package com.webwino.models

/**
 * Hold a liability
 */
class Liability(val amountOwed: Double, val periodInterestRate: Double) {
  val interestAccrued = amountOwed * periodInterestRate
  val payoffAmount = amountOwed + interestAccrued

  def applyPayment(paymentAmount: Double): (Liability, Double) = {
    val paymentApplied = math.min(payoffAmount, paymentAmount)
    val nextAmountOwed = payoffAmount - paymentApplied
    (new Liability(nextAmountOwed, periodInterestRate), paymentApplied)
  }
}

/**
 * Hold a debt representation
 */
object Debt {
  def apply(amountOwed:Double, interestRate:Double) : Debt = {
    val periodInterestRate = interestRate / 12.0
    new Debt(amountOwed, interestRate, amountOwed * periodInterestRate, periodInterestRate)
  }
  def apply(amountOwed:Double, interestRate:Double, minimumPayment:Double) : Debt = {
    new Debt(amountOwed, interestRate, minimumPayment, interestRate / 12.0)
  }
}

class Debt(val amountOwed: Double, val interestRate: Double, val minimumPayment: Double, val periodInterestRate: Double) {
  private var liabilityOverTime: List[Liability] = List(new Liability(amountOwed, periodInterestRate))
  private def currentLiability: Liability = liabilityOverTime.head
  private var paymentOverTime:List[Double] = List()

  def payoffAmount: Double = currentLiability.payoffAmount

  def isPayedOff: Boolean = (currentLiability.payoffAmount == 0)

  def isValidPayment(paymentAmount: Double): Boolean = paymentAmount >= minimumPayment

  def applyPayment(paymentAmount: Double) {
    val paymentApplied = currentLiability.applyPayment(paymentAmount)
    liabilityOverTime = List(paymentApplied._1) ::: liabilityOverTime
    paymentOverTime = List(paymentApplied._2) ::: paymentOverTime
  }
}