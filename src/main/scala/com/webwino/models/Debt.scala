package com.webwino.models

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

class Debt private (val amountOwed: Double, val interestRate: Double, val minimumPayment: Double, val periodInterestRate: Double) {
  private var liabilityOverTime: List[Liability] = List[Liability](Liability(amountOwed, periodInterestRate))
  private def currentLiability: Liability = liabilityOverTime.head
  private var paymentOverTime:List[LiabilityPayment] = List()

  def payoffAmount: Double = currentLiability.payoffAmount

  def isPayedOff: Boolean = (currentLiability.payoffAmount == 0)

  def isValidPayment(paymentAmount: Double): Boolean = paymentAmount >= minimumPayment

  def applyPayment(paymentAmount: Double) : Double = {
    val paymentApplied = math.min(currentLiability.payoffAmount, paymentAmount)
    paymentOverTime = List(LiabilityPayment(currentLiability, paymentApplied)) ::: paymentOverTime
    liabilityOverTime = List(Liability(currentLiability, paymentApplied)) ::: liabilityOverTime
    paymentAmount - paymentApplied
  }

  def applyAdditionalPayment(paymentAmount: Double) : Double = {
    val paymentApplied = math.min(currentLiability.payoffAmount, paymentAmount)
    paymentOverTime = List(LiabilityPayment(currentLiability, paymentApplied)) ::: paymentOverTime.tail
    liabilityOverTime = List(Liability(currentLiability, paymentApplied)) ::: liabilityOverTime.tail
    paymentAmount - paymentApplied
  }
}