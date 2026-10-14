package com.example.ui.tools

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.finance.PercentageEngine
import com.example.ui.theme.FlatAlizarin
import com.example.ui.theme.LocalFlatThemeColors
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

enum class PctMode(val title: String) {
    BASIC("X% of Y"),
    CHANGE("% Change"),
    DISCOUNT("Discount"),
    PROFIT_LOSS("Profit / Loss"),
    MARKUP_MARGIN("Markup / Margin"),
    TAX("VAT & Tax")
}

@Composable
fun PercentageToolsScreen() {
    val colors = LocalFlatThemeColors.current
    var mode by remember { mutableStateOf(PctMode.BASIC) }
    val scrollState = rememberScrollState()
    val df = remember { DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.US)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        // Tab / chip selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PctMode.values().forEach { m ->
                FilterChip(
                    selected = mode == m,
                    onClick = { mode = m },
                    label = { Text(m.title, fontSize = 13.sp) },
                    shape = RoundedCornerShape(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            shape = RoundedCornerShape(4.dp),
            color = colors.surface,
            border = BorderStroke(1.dp, colors.border),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                when (mode) {
                    PctMode.BASIC -> {
                        var percent by remember { mutableStateOf("15") }
                        var total by remember { mutableStateOf("250") }

                        val pVal = percent.toDoubleOrNull()
                        val tVal = total.toDoubleOrNull()

                        Text("Calculate X% of Number", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = colors.mainText)
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = percent,
                            onValueChange = { percent = it },
                            label = { Text("Percentage (%)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = total,
                            onValueChange = { total = it },
                            label = { Text("Total Number (Y)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        if (pVal != null && tVal != null) {
                            val res = PercentageEngine.percentOf(pVal, tVal)
                            ResultBox(label = "$percent% of $total", value = df.format(res))
                        } else {
                            Text("Please enter valid numbers", color = FlatAlizarin, fontWeight = FontWeight.Medium)
                        }
                    }

                    PctMode.CHANGE -> {
                        var initial by remember { mutableStateOf("100") }
                        var finalVal by remember { mutableStateOf("150") }

                        val iVal = initial.toDoubleOrNull()
                        val fVal = finalVal.toDoubleOrNull()

                        Text("Percentage Increase / Decrease", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = colors.mainText)
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = initial,
                            onValueChange = { initial = it },
                            label = { Text("Initial Value") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = finalVal,
                            onValueChange = { finalVal = it },
                            label = { Text("Final Value") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        if (iVal != null && fVal != null) {
                            val change = PercentageEngine.percentChange(iVal, fVal)
                            val tag = if (change.isIncrease) "Increase" else "Decrease"
                            val valueDisplay = if (change.isUndefined) {
                                if (change.difference == 0.0) "0% (No change)" else "Undefined (base value is 0)"
                            } else {
                                "${df.format(change.percentChange)}% $tag"
                            }
                            ResultBox(
                                label = "$tag of ${df.format(change.difference)}",
                                value = valueDisplay
                            )
                        } else {
                            Text("Please enter valid initial and final values", color = FlatAlizarin, fontWeight = FontWeight.Medium)
                        }
                    }

                    PctMode.DISCOUNT -> {
                        var price by remember { mutableStateOf("80") }
                        var discountPct by remember { mutableStateOf("20") }

                        val pVal = price.toDoubleOrNull()
                        val dVal = discountPct.toDoubleOrNull()

                        Text("Discount Calculator", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = colors.mainText)
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = price,
                            onValueChange = { price = it },
                            label = { Text("Original Price") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = discountPct,
                            onValueChange = { discountPct = it },
                            label = { Text("Discount (%)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        if (pVal != null && dVal != null && pVal >= 0 && dVal >= 0) {
                            val disc = PercentageEngine.discount(pVal, dVal)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                ResultSubBox(modifier = Modifier.weight(1f), label = "Final Price", value = df.format(disc.finalPrice))
                                ResultSubBox(modifier = Modifier.weight(1f), label = "You Save", value = df.format(disc.savings))
                            }
                        } else {
                            Text("Please enter positive values for price and discount", color = FlatAlizarin, fontWeight = FontWeight.Medium)
                        }
                    }

                    PctMode.PROFIT_LOSS -> {
                        var cost by remember { mutableStateOf("200") }
                        var sell by remember { mutableStateOf("260") }

                        val cVal = cost.toDoubleOrNull()
                        val sVal = sell.toDoubleOrNull()

                        Text("Profit & Loss Calculator", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = colors.mainText)
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = cost,
                            onValueChange = { cost = it },
                            label = { Text("Cost Price") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = sell,
                            onValueChange = { sell = it },
                            label = { Text("Selling Price") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        if (cVal != null && sVal != null) {
                            val pl = PercentageEngine.profitLoss(cVal, sVal)
                            val state = if (pl.isProfit) "Profit" else "Loss"
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                ResultSubBox(modifier = Modifier.weight(1f), label = "$state Amount", value = df.format(pl.amount))
                                ResultSubBox(modifier = Modifier.weight(1f), label = "$state %", value = "${df.format(pl.percentage)}%")
                            }
                        } else {
                            Text("Please enter valid cost and selling prices", color = FlatAlizarin, fontWeight = FontWeight.Medium)
                        }
                    }

                    PctMode.MARKUP_MARGIN -> {
                        var cost by remember { mutableStateOf("150") }
                        var markup by remember { mutableStateOf("30") }

                        val cVal = cost.toDoubleOrNull()
                        val mVal = markup.toDoubleOrNull()

                        Text("Markup & Margin Calculator", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = colors.mainText)
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = cost,
                            onValueChange = { cost = it },
                            label = { Text("Cost Price") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = markup,
                            onValueChange = { markup = it },
                            label = { Text("Markup (%)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        if (cVal != null && mVal != null) {
                            val mm = PercentageEngine.markup(cVal, mVal)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                ResultSubBox(modifier = Modifier.weight(1f), label = "Selling Price", value = df.format(mm.sellingPrice))
                                ResultSubBox(modifier = Modifier.weight(1f), label = "Gross Margin", value = "${df.format(mm.marginPercent)}%")
                            }
                        } else {
                            Text("Please enter valid cost price and markup percentage", color = FlatAlizarin, fontWeight = FontWeight.Medium)
                        }
                    }

                    PctMode.TAX -> {
                        var amount by remember { mutableStateOf("100") }
                        var taxRate by remember { mutableStateOf("15") }
                        var isAddTax by remember { mutableStateOf(true) }

                        val aVal = amount.toDoubleOrNull()
                        val rVal = taxRate.toDoubleOrNull()

                        Text("VAT & Sales Tax Calculator", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = colors.mainText)
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(
                                selected = isAddTax,
                                onClick = { isAddTax = true },
                                label = { Text("+ Add VAT/Tax") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            FilterChip(
                                selected = !isAddTax,
                                onClick = { isAddTax = false },
                                label = { Text("− Extract VAT") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it },
                            label = { Text(if (isAddTax) "Net Amount" else "Gross Amount (Inc. Tax)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = taxRate,
                            onValueChange = { taxRate = it },
                            label = { Text("Tax Rate (%)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        if (aVal != null && rVal != null && aVal >= 0 && rVal >= 0) {
                            val taxRes = if (isAddTax) PercentageEngine.addTax(aVal, rVal) else PercentageEngine.removeTax(aVal, rVal)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                ResultSubBox(modifier = Modifier.weight(1f), label = "Tax Amount", value = df.format(taxRes.taxAmount))
                                ResultSubBox(
                                    modifier = Modifier.weight(1f),
                                    label = if (isAddTax) "Total (Gross)" else "Net Amount",
                                    value = df.format(if (isAddTax) taxRes.grossAmount else taxRes.netAmount)
                                )
                            }
                        } else {
                            Text("Please enter positive values for amount and tax rate", color = FlatAlizarin, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultBox(label: String, value: String) {
    val colors = LocalFlatThemeColors.current
    Surface(
        color = colors.background,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, colors.border),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = colors.mutedText)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colors.primaryButton
            )
        }
    }
}

@Composable
private fun ResultSubBox(modifier: Modifier = Modifier, label: String, value: String) {
    val colors = LocalFlatThemeColors.current
    Surface(
        color = colors.background,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, colors.border),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = colors.mutedText)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colors.mainText
            )
        }
    }
}
