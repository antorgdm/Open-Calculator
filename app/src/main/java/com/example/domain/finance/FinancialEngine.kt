package com.example.domain.finance

import kotlin.math.pow

object FinancialEngine {
    data class InterestResult(val interest: Double, val totalAmount: Double)
    data class EmiResult(val monthlyEmi: Double, val totalInterest: Double, val totalPayment: Double)
    data class SipResult(val totalInvested: Double, val wealthGained: Double, val maturityValue: Double)
    data class TipResult(val tipAmount: Double, val totalBill: Double, val perPersonAmount: Double, val tipPerPerson: Double)

    fun simpleInterest(principal: Double, annualRate: Double, timeYears: Double): InterestResult {
        val interest = (principal * annualRate * timeYears) / 100.0
        return InterestResult(interest, principal + interest)
    }

    fun compoundInterest(
        principal: Double,
        annualRate: Double,
        timeYears: Double,
        compoundingPeriodsPerYear: Int = 12
    ): InterestResult {
        val r = (annualRate / 100.0) / compoundingPeriodsPerYear
        val n = compoundingPeriodsPerYear * timeYears
        val total = principal * (1.0 + r).pow(n)
        return InterestResult(total - principal, total)
    }

    fun loanEmi(principal: Double, annualRate: Double, tenureMonths: Int): EmiResult {
        if (principal <= 0 || tenureMonths <= 0) return EmiResult(0.0, 0.0, 0.0)
        val monthlyRate = (annualRate / 100.0) / 12.0
        val emi = if (monthlyRate == 0.0) {
            principal / tenureMonths
        } else {
            val factor = (1.0 + monthlyRate).pow(tenureMonths.toDouble())
            (principal * monthlyRate * factor) / (factor - 1.0)
        }
        val totalPayment = emi * tenureMonths
        val totalInterest = totalPayment - principal
        return EmiResult(emi, totalInterest, totalPayment)
    }

    fun sip(monthlyDeposit: Double, annualRate: Double, tenureYears: Double): SipResult {
        val months = tenureYears * 12.0
        val i = (annualRate / 100.0) / 12.0
        val maturity = if (i == 0.0) {
            monthlyDeposit * months
        } else {
            monthlyDeposit * ((1.0 + i).pow(months) - 1.0) / i * (1.0 + i)
        }
        val invested = monthlyDeposit * months
        val gains = maturity - invested
        return SipResult(invested, gains, maturity)
    }

    fun tip(bill: Double, tipPercent: Double, splitCount: Int): TipResult {
        val tip = bill * (tipPercent / 100.0)
        val total = bill + tip
        val people = splitCount.coerceAtLeast(1)
        return TipResult(tip, total, total / people, tip / people)
    }
}
