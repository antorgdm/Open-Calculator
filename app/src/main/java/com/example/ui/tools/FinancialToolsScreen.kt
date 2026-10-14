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
import com.example.domain.finance.FinancialEngine
import com.example.ui.theme.FlatAlizarin
import com.example.ui.theme.LocalFlatThemeColors
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

enum class FinanceTab(val title: String) {
    LOAN_EMI("Loan EMI"),
    COMPOUND_INT("Compound Int."),
    SIMPLE_INT("Simple Int."),
    INVESTMENT_SIP("SIP Investment"),
    TIP_CALC("Tip & Split")
}

@Composable
fun FinancialToolsScreen() {
    val colors = LocalFlatThemeColors.current
    var tab by remember { mutableStateOf(FinanceTab.LOAN_EMI) }
    val df = remember { DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.US)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FinanceTab.values().forEach { t ->
                FilterChip(
                    selected = tab == t,
                    onClick = { tab = t },
                    label = { Text(t.title, fontSize = 13.sp) },
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
                when (tab) {
                    FinanceTab.LOAN_EMI -> {
                        var principal by remember { mutableStateOf("100000") }
                        var rate by remember { mutableStateOf("8.5") }
                        var tenureYears by remember { mutableStateOf("5") }

                        val p = principal.toDoubleOrNull()
                        val r = rate.toDoubleOrNull()
                        val y = tenureYears.toDoubleOrNull()

                        Text("Loan EMI Calculator", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = colors.mainText)
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = principal,
                            onValueChange = { principal = it },
                            label = { Text("Loan Amount / Principal") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = rate,
                            onValueChange = { rate = it },
                            label = { Text("Interest Rate (% per year)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = tenureYears,
                            onValueChange = { tenureYears = it },
                            label = { Text("Tenure (Years)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        if (p != null && r != null && y != null && p > 0 && y > 0) {
                            val tenureMonths = (y * 12).toInt()
                            val emi = FinancialEngine.loanEmi(p, r, tenureMonths)
                            ResultBox(label = "Monthly EMI", value = df.format(emi.monthlyEmi))
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                ResultSubBox(modifier = Modifier.weight(1f), label = "Total Interest", value = df.format(emi.totalInterest))
                                ResultSubBox(modifier = Modifier.weight(1f), label = "Total Payment", value = df.format(emi.totalPayment))
                            }
                        } else {
                            Text("Please enter valid positive loan details", color = FlatAlizarin, fontWeight = FontWeight.Medium)
                        }
                    }

                    FinanceTab.COMPOUND_INT -> {
                        var principal by remember { mutableStateOf("10000") }
                        var rate by remember { mutableStateOf("7.0") }
                        var time by remember { mutableStateOf("3") }

                        val p = principal.toDoubleOrNull()
                        val r = rate.toDoubleOrNull()
                        val t = time.toDoubleOrNull()

                        Text("Compound Interest Calculator", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = colors.mainText)
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = principal,
                            onValueChange = { principal = it },
                            label = { Text("Principal Amount") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = rate,
                            onValueChange = { rate = it },
                            label = { Text("Annual Interest Rate (%)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = time,
                            onValueChange = { time = it },
                            label = { Text("Time Period (Years)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        if (p != null && r != null && t != null && p > 0 && t > 0) {
                            val ci = FinancialEngine.compoundInterest(p, r, t, 12)
                            ResultBox(label = "Final Maturity Value", value = df.format(ci.totalAmount))
                            Spacer(modifier = Modifier.height(12.dp))
                            ResultSubBox(modifier = Modifier.fillMaxWidth(), label = "Interest Earned", value = df.format(ci.interest))
                        } else {
                            Text("Please enter valid principal and time period", color = FlatAlizarin, fontWeight = FontWeight.Medium)
                        }
                    }

                    FinanceTab.SIMPLE_INT -> {
                        var principal by remember { mutableStateOf("5000") }
                        var rate by remember { mutableStateOf("5.0") }
                        var time by remember { mutableStateOf("2") }

                        val p = principal.toDoubleOrNull()
                        val r = rate.toDoubleOrNull()
                        val t = time.toDoubleOrNull()

                        Text("Simple Interest Calculator", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = colors.mainText)
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = principal,
                            onValueChange = { principal = it },
                            label = { Text("Principal Amount") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = rate,
                            onValueChange = { rate = it },
                            label = { Text("Annual Rate (%)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = time,
                            onValueChange = { time = it },
                            label = { Text("Time (Years)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        if (p != null && r != null && t != null && p > 0 && t > 0) {
                            val si = FinancialEngine.simpleInterest(p, r, t)
                            ResultBox(label = "Total Amount", value = df.format(si.totalAmount))
                            Spacer(modifier = Modifier.height(12.dp))
                            ResultSubBox(modifier = Modifier.fillMaxWidth(), label = "Simple Interest", value = df.format(si.interest))
                        } else {
                            Text("Please enter valid principal and time period", color = FlatAlizarin, fontWeight = FontWeight.Medium)
                        }
                    }

                    FinanceTab.INVESTMENT_SIP -> {
                        var deposit by remember { mutableStateOf("500") }
                        var rate by remember { mutableStateOf("12.0") }
                        var years by remember { mutableStateOf("10") }

                        val d = deposit.toDoubleOrNull()
                        val r = rate.toDoubleOrNull()
                        val y = years.toDoubleOrNull()

                        Text("Systematic Investment Plan (SIP)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = colors.mainText)
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = deposit,
                            onValueChange = { deposit = it },
                            label = { Text("Monthly Deposit") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = rate,
                            onValueChange = { rate = it },
                            label = { Text("Expected Annual Return (%)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = years,
                            onValueChange = { years = it },
                            label = { Text("Tenure (Years)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        if (d != null && r != null && y != null && d > 0 && y > 0) {
                            val sip = FinancialEngine.sip(d, r, y)
                            ResultBox(label = "Expected Maturity Value", value = df.format(sip.maturityValue))
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                ResultSubBox(modifier = Modifier.weight(1f), label = "Total Invested", value = df.format(sip.totalInvested))
                                ResultSubBox(modifier = Modifier.weight(1f), label = "Wealth Gained", value = df.format(sip.wealthGained))
                            }
                        } else {
                            Text("Please enter valid deposit and tenure", color = FlatAlizarin, fontWeight = FontWeight.Medium)
                        }
                    }

                    FinanceTab.TIP_CALC -> {
                        var bill by remember { mutableStateOf("120") }
                        var tipPct by remember { mutableStateOf("15") }
                        var people by remember { mutableStateOf("3") }

                        val b = bill.toDoubleOrNull()
                        val t = tipPct.toDoubleOrNull()
                        val p = people.toIntOrNull()

                        Text("Tip & Bill Splitter", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = colors.mainText)
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = bill,
                            onValueChange = { bill = it },
                            label = { Text("Bill Amount") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = tipPct,
                            onValueChange = { tipPct = it },
                            label = { Text("Tip Percentage (%)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = people,
                            onValueChange = { people = it },
                            label = { Text("Number of People") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        if (b != null && t != null && p != null && b > 0 && p > 0) {
                            val tipRes = FinancialEngine.tip(b, t, p)
                            ResultBox(label = "Total Per Person", value = df.format(tipRes.perPersonAmount))
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                ResultSubBox(modifier = Modifier.weight(1f), label = "Total Tip", value = df.format(tipRes.tipAmount))
                                ResultSubBox(modifier = Modifier.weight(1f), label = "Total Bill", value = df.format(tipRes.totalBill))
                            }
                        } else {
                            Text("Please enter valid bill amount and split count", color = FlatAlizarin, fontWeight = FontWeight.Medium)
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
        shape = RoundedCornerShape(4.dp),
        color = colors.background,
        border = BorderStroke(1.dp, colors.border),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = colors.mutedText)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = colors.primaryButton)
        }
    }
}

@Composable
private fun ResultSubBox(modifier: Modifier = Modifier, label: String, value: String) {
    val colors = LocalFlatThemeColors.current
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = colors.background,
        border = BorderStroke(1.dp, colors.border),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = colors.mutedText)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = colors.mainText)
        }
    }
}
