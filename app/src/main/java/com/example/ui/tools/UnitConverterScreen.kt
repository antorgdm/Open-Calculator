package com.example.ui.tools

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.converters.UnitCategory
import com.example.domain.converters.UnitConverterEngine
import com.example.domain.converters.UnitItem
import com.example.ui.calculator.CalculatorViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitConverterScreen(
    calculatorViewModel: CalculatorViewModel
) {
    val context = LocalContext.current
    val colors = LocalFlatThemeColors.current
    val isDark = colors.isDark

    val surfaceColor = colors.surface
    val borderColor = colors.border
    val bgColor = colors.background

    var selectedCategory by remember { mutableStateOf(UnitCategory.LENGTH) }
    val units = remember(selectedCategory) { UnitConverterEngine.getUnitsForCategory(selectedCategory) }

    var fromUnit by remember(selectedCategory) { mutableStateOf(units[0]) }
    var toUnit by remember(selectedCategory) { mutableStateOf(if (units.size > 1) units[1] else units[0]) }
    var inputValue by remember { mutableStateOf("1") }

    val numericInput = inputValue.toDoubleOrNull() ?: 0.0
    val convertedResult = remember(numericInput, fromUnit, toUnit, selectedCategory) {
        UnitConverterEngine.convert(numericInput, fromUnit, toUnit, selectedCategory)
    }

    val catScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Horizontal Flat Category Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(catScrollState),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            UnitCategory.values().forEach { category ->
                val isSelected = selectedCategory == category
                Surface(
                    onClick = {
                        selectedCategory = category
                        val newUnits = UnitConverterEngine.getUnitsForCategory(category)
                        fromUnit = newUnits[0]
                        toUnit = if (newUnits.size > 1) newUnits[1] else newUnits[0]
                    },
                    shape = RoundedCornerShape(4.dp),
                    color = if (isSelected) FlatPeterRiver else (if (isDark) FlatWetAsphalt else FlatClouds),
                    border = BorderStroke(1.dp, if (isSelected) FlatPeterRiver else borderColor),
                    modifier = Modifier.height(32.dp)
                ) {
                    Box(modifier = Modifier.padding(horizontal = 10.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = category.displayName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else (if (isDark) FlatDarkText else FlatLightText)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Classic Flat Conversion Box
        Surface(
            shape = RoundedCornerShape(4.dp),
            color = surfaceColor,
            border = BorderStroke(1.dp, borderColor),
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // Input TextField
                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    label = { Text("Value to Convert") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FlatPeterRiver,
                        unfocusedBorderColor = borderColor
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // From Unit Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "From:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) FlatDarkText else FlatLightText
                    )
                    FlatUnitDropdown(selectedUnit = fromUnit, allUnits = units) { fromUnit = it }
                }

                // Flat Swap Action
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        onClick = {
                            val temp = fromUnit
                            fromUnit = toUnit
                            toUnit = temp
                        },
                        shape = RoundedCornerShape(3.dp),
                        color = if (isDark) FlatWetAsphalt else FlatClouds,
                        border = BorderStroke(1.dp, borderColor),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.SwapVert,
                                contentDescription = "Swap Units",
                                tint = FlatPeterRiver,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // To Unit Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "To:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) FlatDarkText else FlatLightText
                    )
                    FlatUnitDropdown(selectedUnit = toUnit, allUnits = units) { toUnit = it }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(thickness = 1.dp, color = borderColor)
                Spacer(modifier = Modifier.height(10.dp))

                // Output Result
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Converted Result",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted
                        )
                        Text(
                            text = "${UnitConverterEngine.formatNumber(convertedResult)} ${toUnit.symbol}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) FlatEmerald else FlatNephritis
                        )
                    }
                    Surface(
                        onClick = {
                            calculatorViewModel.copyToClipboard(
                                context,
                                "${UnitConverterEngine.formatNumber(convertedResult)} ${toUnit.symbol}"
                            )
                        },
                        shape = RoundedCornerShape(4.dp),
                        color = if (isDark) FlatWetAsphalt else FlatClouds,
                        border = BorderStroke(1.dp, borderColor),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.ContentCopy,
                                contentDescription = "Copy Result",
                                tint = FlatPeterRiver,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // All Units Overview Matrix
        Text(
            text = "EQUIVALENTS IN ALL UNITS",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = if (isDark) FlatDarkPrimary else FlatLightPrimary,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(6.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(units) { u ->
                val valInU = UnitConverterEngine.convert(numericInput, fromUnit, u, selectedCategory)
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = surfaceColor,
                    border = BorderStroke(1.dp, borderColor),
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            u.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = if (isDark) FlatDarkText else FlatLightText
                        )
                        Text(
                            "${UnitConverterEngine.formatNumber(valInU)} ${u.symbol}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) FlatEmerald else FlatNephritis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FlatUnitDropdown(
    selectedUnit: UnitItem,
    allUnits: List<UnitItem>,
    onSelect: (UnitItem) -> Unit
) {
    val colors = LocalFlatThemeColors.current
    val isDark = colors.isDark
    val borderColor = colors.border
    var expanded by remember { mutableStateOf(false) }

    Box {
        Surface(
            onClick = { expanded = true },
            shape = RoundedCornerShape(4.dp),
            color = if (isDark) FlatWetAsphalt else FlatClouds,
            border = BorderStroke(1.dp, borderColor),
            modifier = Modifier.height(34.dp)
        ) {
            Box(modifier = Modifier.padding(horizontal = 12.dp), contentAlignment = Alignment.Center) {
                Text(
                    text = "${selectedUnit.name} (${selectedUnit.symbol})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.mainText
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            allUnits.forEach { u ->
                DropdownMenuItem(
                    text = { Text("${u.name} (${u.symbol})", fontWeight = FontWeight.SemiBold) },
                    onClick = {
                        onSelect(u)
                        expanded = false
                    }
                )
            }
        }
    }
}
