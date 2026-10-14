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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.datetime.DateTimeEngine
import com.example.ui.theme.FlatAlizarin
import com.example.ui.theme.LocalFlatThemeColors
import java.util.Calendar

enum class DateTab(val title: String) {
    AGE("Age Calculator"),
    DIFFERENCE("Date Difference"),
    ADD_SUB("Add / Subtract"),
    WORKING_DAYS("Working Days")
}

@Composable
fun DateTimeToolsScreen() {
    val colors = LocalFlatThemeColors.current
    var tab by remember { mutableStateOf(DateTab.AGE) }

    fun parseStrictDate(yStr: String, mStr: String, dStr: String): Calendar? {
        val y = yStr.toIntOrNull() ?: return null
        val m = mStr.toIntOrNull() ?: return null
        val d = dStr.toIntOrNull() ?: return null
        if (m !in 1..12 || d < 1 || d > 31) return null

        return try {
            val cal = Calendar.getInstance().apply {
                isLenient = false
                set(Calendar.YEAR, y)
                set(Calendar.MONTH, m - 1)
                set(Calendar.DAY_OF_MONTH, d)
                set(Calendar.HOUR_OF_DAY, 12)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            cal.time // validates
            cal
        } catch (_: Exception) {
            null
        }
    }

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
            DateTab.values().forEach { t ->
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
                    DateTab.AGE -> {
                        var birthYear by remember { mutableStateOf("2000") }
                        var birthMonth by remember { mutableStateOf("5") }
                        var birthDay by remember { mutableStateOf("15") }

                        val birthCal = parseStrictDate(birthYear, birthMonth, birthDay)

                        Text("Age Calculator", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = colors.mainText)
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = birthDay,
                                onValueChange = { birthDay = it },
                                label = { Text("Day") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            OutlinedTextField(
                                value = birthMonth,
                                onValueChange = { birthMonth = it },
                                label = { Text("Month (1-12)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.weight(1.3f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            OutlinedTextField(
                                value = birthYear,
                                onValueChange = { birthYear = it },
                                label = { Text("Year") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.weight(1.3f),
                                shape = RoundedCornerShape(4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        if (birthCal != null) {
                            val age = DateTimeEngine.calculateAge(birthCal)
                            ResultBox(label = "Current Age", value = "${age.years} Years, ${age.months} Months, ${age.days} Days")
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                ResultSubBox(modifier = Modifier.weight(1f), label = "Total Days Lived", value = "${age.totalDays} Days")
                                ResultSubBox(modifier = Modifier.weight(1f), label = "Next Birthday", value = "${age.nextBirthdayDays} Days (${age.nextBirthdayWeekday})")
                            }
                        } else {
                            Text(
                                "Invalid calendar date (e.g. February 31 does not exist)",
                                color = FlatAlizarin,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    DateTab.DIFFERENCE -> {
                        var startYear by remember { mutableStateOf("2024") }
                        var startMonth by remember { mutableStateOf("1") }
                        var startDay by remember { mutableStateOf("1") }

                        var endYear by remember { mutableStateOf("2026") }
                        var endMonth by remember { mutableStateOf("10") }
                        var endDay by remember { mutableStateOf("6") }

                        val cal1 = parseStrictDate(startYear, startMonth, startDay)
                        val cal2 = parseStrictDate(endYear, endMonth, endDay)

                        Text("Date Difference", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = colors.mainText)
                        Spacer(modifier = Modifier.height(14.dp))
                        Text("Start Date (D / M / Y)", style = MaterialTheme.typography.labelSmall, color = colors.mutedText)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = startDay, onValueChange = { startDay = it }, label = { Text("D") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(4.dp))
                            OutlinedTextField(value = startMonth, onValueChange = { startMonth = it }, label = { Text("M") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(4.dp))
                            OutlinedTextField(value = startYear, onValueChange = { startYear = it }, label = { Text("Y") }, modifier = Modifier.weight(1.5f), shape = RoundedCornerShape(4.dp))
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Text("End Date (D / M / Y)", style = MaterialTheme.typography.labelSmall, color = colors.mutedText)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = endDay, onValueChange = { endDay = it }, label = { Text("D") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(4.dp))
                            OutlinedTextField(value = endMonth, onValueChange = { endMonth = it }, label = { Text("M") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(4.dp))
                            OutlinedTextField(value = endYear, onValueChange = { endYear = it }, label = { Text("Y") }, modifier = Modifier.weight(1.5f), shape = RoundedCornerShape(4.dp))
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        if (cal1 != null && cal2 != null) {
                            val diff = DateTimeEngine.calculateDateDiff(cal1, cal2)
                            ResultBox(label = "Total Days", value = "${diff.totalDays} Days")
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                ResultSubBox(modifier = Modifier.weight(1f), label = "Weeks & Days", value = "${diff.weeks} w, ${diff.remainingDays} d")
                                ResultSubBox(modifier = Modifier.weight(1f), label = "Y / M / D", value = "${diff.years}y ${diff.months}m ${diff.days}d")
                            }
                        } else {
                            Text("Please enter valid calendar dates", color = FlatAlizarin, fontWeight = FontWeight.Medium)
                        }
                    }

                    DateTab.ADD_SUB -> {
                        var baseYear by remember { mutableStateOf("2026") }
                        var baseMonth by remember { mutableStateOf("10") }
                        var baseDay by remember { mutableStateOf("6") }
                        var daysOffset by remember { mutableStateOf("45") }
                        var isAdd by remember { mutableStateOf(true) }

                        val cal = parseStrictDate(baseYear, baseMonth, baseDay)
                        val offset = (daysOffset.toIntOrNull() ?: 0) * (if (isAdd) 1 else -1)

                        Text("Add or Subtract Days", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = colors.mainText)
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(selected = isAdd, onClick = { isAdd = true }, label = { Text("+ Add Days") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(4.dp))
                            FilterChip(selected = !isAdd, onClick = { isAdd = false }, label = { Text("− Subtract Days") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(4.dp))
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Base Date", style = MaterialTheme.typography.labelSmall, color = colors.mutedText)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = baseDay, onValueChange = { baseDay = it }, label = { Text("D") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(4.dp))
                            OutlinedTextField(value = baseMonth, onValueChange = { baseMonth = it }, label = { Text("M") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(4.dp))
                            OutlinedTextField(value = baseYear, onValueChange = { baseYear = it }, label = { Text("Y") }, modifier = Modifier.weight(1.5f), shape = RoundedCornerShape(4.dp))
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = daysOffset,
                            onValueChange = { daysOffset = it },
                            label = { Text("Number of Days") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        if (cal != null) {
                            val targetCal = DateTimeEngine.addDaysToDate(cal, offset)
                            ResultBox(label = if (isAdd) "Date After $daysOffset Days" else "Date Before $daysOffset Days", value = DateTimeEngine.formatDate(targetCal))
                        } else {
                            Text("Please enter a valid base calendar date", color = FlatAlizarin, fontWeight = FontWeight.Medium)
                        }
                    }

                    DateTab.WORKING_DAYS -> {
                        var startYear by remember { mutableStateOf("2026") }
                        var startMonth by remember { mutableStateOf("1") }
                        var startDay by remember { mutableStateOf("1") }

                        var endYear by remember { mutableStateOf("2026") }
                        var endMonth by remember { mutableStateOf("12") }
                        var endDay by remember { mutableStateOf("31") }

                        val cal1 = parseStrictDate(startYear, startMonth, startDay)
                        val cal2 = parseStrictDate(endYear, endMonth, endDay)

                        Text("Working / Business Days (Excl. Sat & Sun)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = colors.mainText)
                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Start Date", style = MaterialTheme.typography.labelSmall, color = colors.mutedText)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = startDay, onValueChange = { startDay = it }, label = { Text("D") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(4.dp))
                            OutlinedTextField(value = startMonth, onValueChange = { startMonth = it }, label = { Text("M") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(4.dp))
                            OutlinedTextField(value = startYear, onValueChange = { startYear = it }, label = { Text("Y") }, modifier = Modifier.weight(1.5f), shape = RoundedCornerShape(4.dp))
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text("End Date", style = MaterialTheme.typography.labelSmall, color = colors.mutedText)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(value = endDay, onValueChange = { endDay = it }, label = { Text("D") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(4.dp))
                            OutlinedTextField(value = endMonth, onValueChange = { endMonth = it }, label = { Text("M") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(4.dp))
                            OutlinedTextField(value = endYear, onValueChange = { endYear = it }, label = { Text("Y") }, modifier = Modifier.weight(1.5f), shape = RoundedCornerShape(4.dp))
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        if (cal1 != null && cal2 != null) {
                            val workDays = DateTimeEngine.calculateWorkingDays(cal1, cal2)
                            ResultBox(label = "Total Working Days", value = "$workDays Days")
                        } else {
                            Text("Please enter valid calendar dates", color = FlatAlizarin, fontWeight = FontWeight.Medium)
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
