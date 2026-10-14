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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.converters.CurrencyEngine
import com.example.domain.converters.CurrencyItem
import com.example.ui.calculator.CalculatorViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyConverterScreen(
    calculatorViewModel: CalculatorViewModel
) {
    val context = LocalContext.current
    val colors = LocalFlatThemeColors.current
    val isDark = colors.isDark

    val surfaceColor = colors.surface
    val borderColor = colors.border
    val bgColor = colors.background

    // Default From USD to EUR
    var fromCurrency by remember { mutableStateOf(CurrencyEngine.allCurrencies.first { it.code == "USD" }) }
    var toCurrency by remember { mutableStateOf(CurrencyEngine.allCurrencies.first { it.code == "EUR" }) }
    var amountInput by remember { mutableStateOf("100") }

    var pickerTarget by remember { mutableStateOf<String?>(null) } // "FROM" or "TO"
    var searchQuery by remember { mutableStateOf("") }

    val numericAmount = amountInput.toDoubleOrNull() ?: 0.0
    val convertedAmount = remember(numericAmount, fromCurrency, toCurrency) {
        CurrencyEngine.convert(numericAmount, fromCurrency, toCurrency)
    }

    val singleUnitRate = remember(fromCurrency, toCurrency) {
        CurrencyEngine.convert(1.0, fromCurrency, toCurrency)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Classic Flat Currency Calculation Panel
        Surface(
            shape = RoundedCornerShape(4.dp),
            color = surfaceColor,
            border = BorderStroke(1.dp, borderColor),
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // Input Amount
                OutlinedTextField(
                    value = amountInput,
                    onValueChange = { amountInput = it },
                    label = { Text("Amount to Convert") },
                    leadingIcon = {
                        Text(
                            text = fromCurrency.symbol,
                            fontWeight = FontWeight.Bold,
                            color = FlatEmerald,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("currency_amount_input"),
                    shape = RoundedCornerShape(4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FlatBelizeHole,
                        unfocusedBorderColor = borderColor
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Preset Amount Chips (Classic Flat)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("1", "10", "50", "100", "500", "1000", "5000").forEach { preset ->
                        Surface(
                            onClick = { amountInput = preset },
                            shape = RoundedCornerShape(3.dp),
                            color = if (isDark) FlatWetAsphalt else FlatClouds,
                            border = BorderStroke(1.dp, borderColor),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Box(modifier = Modifier.padding(horizontal = 10.dp), contentAlignment = Alignment.Center) {
                                Text(preset, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Source Currency Selector
                FlatCurrencySelectorRow(
                    label = "Source",
                    currency = fromCurrency,
                    onClick = {
                        searchQuery = ""
                        pickerTarget = "FROM"
                    }
                )

                // Flat Swap Action
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        onClick = {
                            val temp = fromCurrency
                            fromCurrency = toCurrency
                            toCurrency = temp
                        },
                        shape = RoundedCornerShape(3.dp),
                        color = if (isDark) FlatWetAsphalt else FlatClouds,
                        border = BorderStroke(1.dp, borderColor),
                        modifier = Modifier.size(36.dp).testTag("swap_currency_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.SwapVert,
                                contentDescription = "Swap Currencies",
                                tint = FlatBelizeHole,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Target Currency Selector
                FlatCurrencySelectorRow(
                    label = "Target",
                    currency = toCurrency,
                    onClick = {
                        searchQuery = ""
                        pickerTarget = "TO"
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(thickness = 1.dp, color = borderColor)
                Spacer(modifier = Modifier.height(10.dp))

                // Result Display
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "1 ${fromCurrency.code} = ${CurrencyEngine.formatAmount(singleUnitRate)} ${toCurrency.code}",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${toCurrency.symbol} ${CurrencyEngine.formatAmount(convertedAmount)}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) FlatEmerald else FlatNephritis,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${toCurrency.code} • ${toCurrency.name}",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted
                        )
                    }

                    Surface(
                        onClick = {
                            calculatorViewModel.copyToClipboard(
                                context,
                                "${CurrencyEngine.formatAmount(convertedAmount)} ${toCurrency.code}"
                            )
                        },
                        shape = RoundedCornerShape(4.dp),
                        color = if (isDark) FlatWetAsphalt else FlatClouds,
                        border = BorderStroke(1.dp, borderColor),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy Converted Amount",
                                tint = FlatBelizeHole,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // World Currencies Live Comparison Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "GLOBAL EQUIVALENTS (${CurrencyEngine.allCurrencies.size} CURRENCIES)",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = if (isDark) FlatDarkPrimary else FlatLightPrimary,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // All World Currencies Flat List
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(CurrencyEngine.allCurrencies, key = { it.code }) { item ->
                val eqAmount = CurrencyEngine.convert(numericAmount, fromCurrency, item)
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = surfaceColor,
                    border = BorderStroke(1.dp, borderColor),
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { toCurrency = item }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Surface(
                                shape = RoundedCornerShape(3.dp),
                                color = FlatEmerald,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = item.symbol.take(3),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = Color.White
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "${item.code} • ${item.country}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) FlatDarkText else FlatLightText
                                )
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Text(
                            text = "${item.symbol} ${CurrencyEngine.formatAmount(eqAmount)}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) FlatEmerald else FlatNephritis
                        )
                    }
                }
            }
        }
    }

    // Classic Flat Currency Picker Dialog
    if (pickerTarget != null) {
        val filteredList = remember(searchQuery) {
            CurrencyEngine.searchCurrencies(searchQuery)
        }

        AlertDialog(
            onDismissRequest = { pickerTarget = null },
            shape = RoundedCornerShape(4.dp),
            containerColor = surfaceColor,
            tonalElevation = 0.dp,
            title = {
                Column {
                    Text(
                        text = if (pickerTarget == "FROM") "Select Source Currency" else "Select Target Currency",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isDark) FlatDarkText else FlatLightText
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search by name, code, or country...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear search")
                                }
                            }
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(4.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FlatBelizeHole,
                            unfocusedBorderColor = borderColor
                        )
                    )
                }
            },
            text = {
                Box(modifier = Modifier.height(360.dp)) {
                    if (filteredList.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No currencies found", color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted)
                        }
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            items(filteredList, key = { it.code }) { currency ->
                                Surface(
                                    shape = RoundedCornerShape(3.dp),
                                    color = if (isDark) FlatWetAsphalt else FlatClouds,
                                    border = BorderStroke(1.dp, borderColor),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (pickerTarget == "FROM") {
                                                fromCurrency = currency
                                            } else {
                                                toCurrency = currency
                                            }
                                            pickerTarget = null
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp, vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = currency.code,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isDark) FlatDarkText else FlatLightText
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "(${currency.symbol})",
                                                    color = FlatEmerald,
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            Text(
                                                text = "${currency.name} • ${currency.country}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }

                                        Text(
                                            text = "1 USD = ${CurrencyEngine.formatAmount(currency.rateToUsd)}",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontFamily = FontFamily.Monospace,
                                            color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { pickerTarget = null }) {
                    Text("Close", color = if (isDark) FlatDarkText else FlatLightText)
                }
            }
        )
    }
}

@Composable
private fun FlatCurrencySelectorRow(
    label: String,
    currency: CurrencyItem,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val borderColor = if (isDark) FlatDarkBorder else FlatLightBorder

    Surface(
        shape = RoundedCornerShape(4.dp),
        color = if (isDark) FlatWetAsphalt else FlatClouds,
        border = BorderStroke(1.dp, borderColor),
        tonalElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(3.dp),
                    color = FlatEmerald,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = currency.symbol.take(3),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "$label: ${currency.code} (${currency.country})",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) FlatDarkText else FlatLightText
                    )
                    Text(
                        text = currency.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Select currency",
                tint = if (isDark) FlatDarkTextMuted else FlatLightTextMuted
            )
        }
    }
}
