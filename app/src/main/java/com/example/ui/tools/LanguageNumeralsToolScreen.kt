package com.example.ui.tools

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.preferences.AppPreferences
import com.example.domain.languages.LanguageEngine
import com.example.domain.languages.NumeralScript
import com.example.ui.theme.*

enum class LanguageToolTab(val title: String) {
    LANGUAGES("World Languages"),
    NUMERALS("Numeral Converter"),
    NUMBER_TO_WORDS("Number in Words")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageNumeralsToolScreen() {
    val context = LocalContext.current
    val colors = LocalFlatThemeColors.current
    val isDark = colors.isDark
    val preferences = remember { AppPreferences.getInstance(context) }
    val clipboardManager = LocalClipboardManager.current

    val surfaceColor = colors.surface
    val borderColor = colors.border
    val bgColor = colors.background

    var selectedTab by remember { mutableStateOf(LanguageToolTab.LANGUAGES) }
    var searchQuery by remember { mutableStateOf("") }
    var numberInput by remember { mutableStateOf("2024") }

    val currentAppLang by preferences.languageCode.collectAsState()

    val filteredLanguages = remember(searchQuery) {
        LanguageEngine.searchLanguages(searchQuery)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        // Classic Flat Tab Segmented Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            LanguageToolTab.values().forEach { tab ->
                val isSelected = selectedTab == tab
                Surface(
                    onClick = { selectedTab = tab },
                    shape = RoundedCornerShape(4.dp),
                    color = if (isSelected) FlatGreenSea else (if (isDark) FlatWetAsphalt else FlatClouds),
                    border = BorderStroke(1.dp, if (isSelected) FlatGreenSea else borderColor),
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = tab.title,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else (if (isDark) FlatDarkText else FlatLightText),
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        when (selectedTab) {
            LanguageToolTab.LANGUAGES -> {
                // Classic Flat Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search 140+ languages by name, code, or country...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FlatGreenSea,
                        unfocusedBorderColor = borderColor
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${filteredLanguages.size} LANGUAGES FOUND",
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
                    items(filteredLanguages, key = { it.code + it.englishName }) { lang ->
                        val isSelected = currentAppLang == lang.code
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = surfaceColor,
                            border = BorderStroke(1.dp, if (isSelected) FlatGreenSea else borderColor),
                            tonalElevation = 0.dp,
                            shadowElevation = 0.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = lang.englishName,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = if (isDark) FlatDarkText else FlatLightText
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            shape = RoundedCornerShape(2.dp),
                                            color = if (isDark) FlatWetAsphalt else FlatClouds,
                                            border = BorderStroke(1.dp, borderColor)
                                        ) {
                                            Text(
                                                text = lang.code.uppercase(),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = FlatGreenSea,
                                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = lang.nativeName,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = FlatBelizeHole,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.height(1.dp))
                                    Text(
                                        text = "${lang.family} • ${lang.countryOrRegion}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted
                                    )
                                }

                                Surface(
                                    onClick = {
                                        preferences.setLanguageCode(lang.code)
                                        preferences.setNumeralScript(lang.numeralScript)
                                    },
                                    enabled = !isSelected,
                                    shape = RoundedCornerShape(3.dp),
                                    color = if (isSelected) FlatGreenSea else (if (isDark) FlatWetAsphalt else FlatClouds),
                                    border = BorderStroke(1.dp, borderColor),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Box(modifier = Modifier.padding(horizontal = 10.dp), contentAlignment = Alignment.Center) {
                                        Text(
                                            text = if (isSelected) "Active" else "Apply",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else (if (isDark) FlatDarkText else FlatLightText)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            LanguageToolTab.NUMERALS -> {
                OutlinedTextField(
                    value = numberInput,
                    onValueChange = { numberInput = it.filter { ch -> ch.isDigit() || ch == '.' || ch == '-' } },
                    label = { Text("Enter Number to Transliterate") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FlatGreenSea,
                        unfocusedBorderColor = borderColor
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "WORLD NUMERAL SCRIPTS",
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
                    val parsedLong = numberInput.toLongOrNull()
                    if (parsedLong != null && parsedLong in 1..3999) {
                        item {
                            val roman = LanguageEngine.toRomanNumerals(parsedLong)
                            FlatNumeralCard(
                                title = "Roman Numerals",
                                sample = "I, V, X, L, C, D, M",
                                result = roman,
                                onCopy = { clipboardManager.setText(AnnotatedString(roman)) }
                            )
                        }
                    }

                    items(NumeralScript.values()) { script ->
                        val converted = LanguageEngine.convertDigits(numberInput, script)
                        FlatNumeralCard(
                            title = script.displayName,
                            sample = script.zeroToNine,
                            result = converted,
                            onCopy = { clipboardManager.setText(AnnotatedString(converted)) }
                        )
                    }
                }
            }

            LanguageToolTab.NUMBER_TO_WORDS -> {
                OutlinedTextField(
                    value = numberInput,
                    onValueChange = { numberInput = it.filter { ch -> ch.isDigit() || ch == '-' } },
                    label = { Text("Enter Integer (up to billions)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FlatGreenSea,
                        unfocusedBorderColor = borderColor
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                val parsedNum = numberInput.toLongOrNull() ?: 0L

                val sampleLangs = listOf(
                    Triple("English", "en", "Global"),
                    Triple("বাংলা (Bengali)", "bn", "Bangladesh / India"),
                    Triple("हिन्दी (Hindi)", "hi", "India"),
                    Triple("Español (Spanish)", "es", "Spain / Latin America"),
                    Triple("Français (French)", "fr", "France / Francophone"),
                    Triple("中文 (Chinese)", "zh", "China"),
                    Triple("Deutsch (German)", "de", "Germany"),
                    Triple("العربية (Arabic)", "ar", "Middle East"),
                    Triple("Русский (Russian)", "ru", "Russia"),
                    Triple("日本語 (Japanese)", "ja", "Japan"),
                    Triple("Português (Portuguese)", "pt", "Brazil / Portugal"),
                    Triple("Türkçe (Turkish)", "tr", "Turkey"),
                    Triple("Italiano (Italian)", "it", "Italy"),
                    Triple("Bahasa Indonesia", "id", "Indonesia")
                )

                Text(
                    text = "NUMBER IN WORLD LANGUAGES",
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
                    items(sampleLangs) { (langTitle, code, region) ->
                        val word = LanguageEngine.numberToWords(parsedNum, code)
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
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = langTitle,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = if (isDark) FlatDarkText else FlatLightText
                                    )
                                    Text(
                                        text = region,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = word,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = if (isDark) FlatEmerald else FlatNephritis,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Surface(
                                    onClick = { clipboardManager.setText(AnnotatedString(word)) },
                                    shape = RoundedCornerShape(3.dp),
                                    color = if (isDark) FlatWetAsphalt else FlatClouds,
                                    border = BorderStroke(1.dp, borderColor),
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FlatNumeralCard(
    title: String,
    sample: String,
    result: String,
    onCopy: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val surfaceColor = if (isDark) FlatDarkSurface else FlatLightSurface
    val borderColor = if (isDark) FlatDarkBorder else FlatLightBorder

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
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    color = if (isDark) FlatDarkText else FlatLightText
                )
                Text(
                    text = "Digits: $sample",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isDark) FlatDarkTextMuted else FlatLightTextMuted
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = result,
                    style = MaterialTheme.typography.titleLarge,
                    color = if (isDark) FlatEmerald else FlatNephritis,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            Surface(
                onClick = onCopy,
                shape = RoundedCornerShape(3.dp),
                color = if (isDark) FlatWetAsphalt else FlatClouds,
                border = BorderStroke(1.dp, borderColor),
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy Numeral", modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
