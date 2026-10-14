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
import com.example.domain.equations.EquationSolution
import com.example.domain.equations.EquationSolverEngine
import com.example.domain.statistics.StatisticsEngine
import com.example.ui.theme.FlatAlizarin
import com.example.ui.theme.LocalFlatThemeColors
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

enum class StatEqTab(val title: String) {
    STATISTICS("Statistics"),
    LINEAR_EQ("Linear (ax+b=0)"),
    QUADRATIC_EQ("Quadratic (ax²+bx+c=0)"),
    SYSTEM_2X2("System 2×2")
}

@Composable
fun StatisticsEquationScreen() {
    val colors = LocalFlatThemeColors.current
    var tab by remember { mutableStateOf(StatEqTab.STATISTICS) }
    val df = remember { DecimalFormat("#,##0.####", DecimalFormatSymbols(Locale.US)) }

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
            StatEqTab.values().forEach { t ->
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
                    StatEqTab.STATISTICS -> {
                        var rawInput by remember { mutableStateOf("12, 15, 18, 20, 22, 25, 28, 30, 35") }
                        val parsed = remember(rawInput) { StatisticsEngine.parseDataset(rawInput) }
                        val stats = remember(parsed) { StatisticsEngine.compute(parsed.numbers) }

                        Text("Descriptive Statistics", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = colors.mainText)
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = rawInput,
                            onValueChange = { rawInput = it },
                            label = { Text("Numbers (comma or space separated)") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                            shape = RoundedCornerShape(4.dp)
                        )

                        if (parsed.invalidTokens.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "Ignored invalid non-numeric inputs: ${parsed.invalidTokens.joinToString(", ")}",
                                color = FlatAlizarin,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (stats != null) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                ResultSubBox(Modifier.weight(1f), "Count (N)", stats.count.toString())
                                ResultSubBox(Modifier.weight(1f), "Sum (∑x)", df.format(stats.sum))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                ResultSubBox(Modifier.weight(1f), "Mean (Average)", df.format(stats.mean))
                                ResultSubBox(Modifier.weight(1f), "Median (Q2)", df.format(stats.median))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            val modeStr = if (stats.mode.isEmpty()) "No Mode" else stats.mode.joinToString(", ") { df.format(it) }
                            ResultSubBox(Modifier.fillMaxWidth(), "Mode", modeStr)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                ResultSubBox(Modifier.weight(1f), "Min", df.format(stats.min))
                                ResultSubBox(Modifier.weight(1f), "Max", df.format(stats.max))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                ResultSubBox(Modifier.weight(1f), "Sample Std Dev (s)", df.format(stats.sampleStdDev))
                                ResultSubBox(Modifier.weight(1f), "Pop Std Dev (σ)", df.format(stats.popStdDev))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                ResultSubBox(Modifier.weight(1f), "Q1 (25th %)", df.format(stats.q1))
                                ResultSubBox(Modifier.weight(1f), "Q3 (75th %)", df.format(stats.q3))
                            }
                        } else {
                            Text("Please enter valid numbers to calculate statistics", color = colors.mutedText)
                        }
                    }

                    StatEqTab.LINEAR_EQ -> {
                        var aStr by remember { mutableStateOf("2") }
                        var bStr by remember { mutableStateOf("-6") }

                        val a = aStr.toDoubleOrNull() ?: 2.0
                        val b = bStr.toDoubleOrNull() ?: -6.0
                        val solution = EquationSolverEngine.solveLinear(a, b)

                        Text("Linear Equation: ax + b = 0", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = colors.mainText)
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(value = aStr, onValueChange = { aStr = it }, label = { Text("Coefficient a") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(4.dp))
                            OutlinedTextField(value = bStr, onValueChange = { bStr = it }, label = { Text("Constant b") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(4.dp))
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        when (solution) {
                            is EquationSolution.LinearResult -> {
                                ResultSubBox(Modifier.fillMaxWidth(), "Solution x", df.format(solution.x))
                                Spacer(modifier = Modifier.height(8.dp))
                                ResultSubBox(Modifier.fillMaxWidth(), "Steps", solution.steps)
                            }
                            is EquationSolution.Error -> {
                                Text(solution.message, color = FlatAlizarin)
                            }
                            else -> {}
                        }
                    }

                    StatEqTab.QUADRATIC_EQ -> {
                        var aStr by remember { mutableStateOf("1") }
                        var bStr by remember { mutableStateOf("-5") }
                        var cStr by remember { mutableStateOf("6") }

                        val a = aStr.toDoubleOrNull() ?: 1.0
                        val b = bStr.toDoubleOrNull() ?: -5.0
                        val c = cStr.toDoubleOrNull() ?: 6.0
                        val solution = EquationSolverEngine.solveQuadratic(a, b, c)

                        Text("Quadratic Equation: ax² + bx + c = 0", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = colors.mainText)
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = aStr, onValueChange = { aStr = it }, label = { Text("a") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(4.dp))
                            OutlinedTextField(value = bStr, onValueChange = { bStr = it }, label = { Text("b") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(4.dp))
                            OutlinedTextField(value = cStr, onValueChange = { cStr = it }, label = { Text("c") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(4.dp))
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        when (solution) {
                            is EquationSolution.QuadraticResult -> {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    ResultSubBox(Modifier.weight(1f), "Root 1", solution.root1)
                                    ResultSubBox(Modifier.weight(1f), "Root 2", solution.root2)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    ResultSubBox(Modifier.weight(1f), "Discriminant (Δ)", df.format(solution.discriminant))
                                    ResultSubBox(Modifier.weight(1f), "Nature", if (solution.isComplex) "Complex Roots" else "Real Roots")
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                ResultSubBox(Modifier.fillMaxWidth(), "Vertex (h, k)", "(${df.format(solution.vertex.first)}, ${df.format(solution.vertex.second)})")
                            }
                            is EquationSolution.Error -> {
                                Text(solution.message, color = FlatAlizarin)
                            }
                            else -> {}
                        }
                    }

                    StatEqTab.SYSTEM_2X2 -> {
                        var a1Str by remember { mutableStateOf("2") }
                        var b1Str by remember { mutableStateOf("3") }
                        var c1Str by remember { mutableStateOf("8") }

                        var a2Str by remember { mutableStateOf("1") }
                        var b2Str by remember { mutableStateOf("-1") }
                        var c2Str by remember { mutableStateOf("1") }

                        val a1 = a1Str.toDoubleOrNull() ?: 0.0
                        val b1 = b1Str.toDoubleOrNull() ?: 0.0
                        val c1 = c1Str.toDoubleOrNull() ?: 0.0
                        val a2 = a2Str.toDoubleOrNull() ?: 0.0
                        val b2 = b2Str.toDoubleOrNull() ?: 0.0
                        val c2 = c2Str.toDoubleOrNull() ?: 0.0

                        val solution = EquationSolverEngine.solveSystem2x2(a1, b1, c1, a2, b2, c2)

                        Text("Simultaneous 2×2 Equations", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = colors.mainText)
                        Text("a₁x + b₁y = c₁\na₂x + b₂y = c₂", style = MaterialTheme.typography.bodySmall, color = colors.primaryButton)
                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Equation 1", style = MaterialTheme.typography.labelSmall, color = colors.mutedText)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = a1Str, onValueChange = { a1Str = it }, label = { Text("a₁") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(4.dp))
                            OutlinedTextField(value = b1Str, onValueChange = { b1Str = it }, label = { Text("b₁") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(4.dp))
                            OutlinedTextField(value = c1Str, onValueChange = { c1Str = it }, label = { Text("c₁") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(4.dp))
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Equation 2", style = MaterialTheme.typography.labelSmall, color = colors.mutedText)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = a2Str, onValueChange = { a2Str = it }, label = { Text("a₂") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(4.dp))
                            OutlinedTextField(value = b2Str, onValueChange = { b2Str = it }, label = { Text("b₂") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(4.dp))
                            OutlinedTextField(value = c2Str, onValueChange = { c2Str = it }, label = { Text("c₂") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(4.dp))
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        when (solution) {
                            is EquationSolution.System2x2Result -> {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    ResultSubBox(Modifier.weight(1f), "x", df.format(solution.x))
                                    ResultSubBox(Modifier.weight(1f), "y", df.format(solution.y))
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                ResultSubBox(Modifier.fillMaxWidth(), "Determinant (D)", df.format(solution.determinant))
                            }
                            is EquationSolution.Error -> {
                                Text(solution.message, color = FlatAlizarin)
                            }
                            else -> {}
                        }
                    }
                }
            }
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
