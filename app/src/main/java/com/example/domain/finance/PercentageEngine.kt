package com.example.domain.finance

import kotlin.math.abs

object PercentageEngine {
    data class DiscountResult(val finalPrice: Double, val savings: Double)
    data class ChangeResult(val difference: Double, val percentChange: Double, val isIncrease: Boolean, val isUndefined: Boolean = false)
    data class ProfitLossResult(val amount: Double, val percentage: Double, val isProfit: Boolean)
    data class MarkupMarginResult(val sellingPrice: Double, val profit: Double, val marginPercent: Double)
    data class TaxResult(val netAmount: Double, val taxAmount: Double, val grossAmount: Double)

    fun percentOf(percent: Double, total: Double): Double = (percent / 100.0) * total

    fun percentChange(initial: Double, final: Double): ChangeResult {
        val diff = final - initial
        if (initial == 0.0) {
            return ChangeResult(diff, 0.0, final >= 0, isUndefined = true)
        }
        val pct = (diff / abs(initial)) * 100.0
        return ChangeResult(diff, abs(pct), diff >= 0, isUndefined = false)
    }

    fun discount(originalPrice: Double, discountPercent: Double): DiscountResult {
        val savings = originalPrice * (discountPercent / 100.0)
        val finalPrice = originalPrice - savings
        return DiscountResult(finalPrice, savings)
    }

    fun profitLoss(costPrice: Double, sellingPrice: Double): ProfitLossResult {
        val diff = sellingPrice - costPrice
        val pct = if (costPrice != 0.0) (diff / costPrice) * 100.0 else 0.0
        return ProfitLossResult(diff, abs(pct), diff >= 0)
    }

    fun markup(cost: Double, markupPercent: Double): MarkupMarginResult {
        val profit = cost * (markupPercent / 100.0)
        val selling = cost + profit
        val margin = if (selling != 0.0) (profit / selling) * 100.0 else 0.0
        return MarkupMarginResult(selling, profit, margin)
    }

    fun addTax(amount: Double, taxPercent: Double): TaxResult {
        val tax = amount * (taxPercent / 100.0)
        return TaxResult(amount, tax, amount + tax)
    }

    fun removeTax(grossAmount: Double, taxPercent: Double): TaxResult {
        val net = grossAmount / (1.0 + (taxPercent / 100.0))
        val tax = grossAmount - net
        return TaxResult(net, tax, grossAmount)
    }
}
