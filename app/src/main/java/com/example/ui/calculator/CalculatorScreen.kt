package com.example.ui.calculator

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.preferences.AngleMode
import com.example.data.preferences.AppPreferences
import com.example.ui.components.FeedbackHelper
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    onNavigateToHistory: () -> Unit
) {
    val context = LocalContext.current
    val colors = LocalFlatThemeColors.current
    val isDark = colors.isDark
    val uiState by viewModel.uiState.collectAsState()
    val preferences = remember { AppPreferences.getInstance(context) }
    val hapticsEnabled by preferences.hapticsEnabled.collectAsState()
    val soundEnabled by preferences.soundEnabled.collectAsState()
    val languageCode by preferences.languageCode.collectAsState()
    val numeralScript by preferences.numeralScript.collectAsState()
    val precision by preferences.decimalPrecision.collectAsState()
    val feedbackHelper = remember { FeedbackHelper(context) }
    val exprScrollState = rememberScrollState()
    val mainScrollState = rememberScrollState()

    val borderColor = colors.border
    val surfaceColor = colors.inputBackground
    val bgColor = colors.background

    // Auto-scroll expression to end when it changes
    LaunchedEffect(uiState.expression) {
        exprScrollState.animateScrollTo(exprScrollState.maxValue)
    }

    Scaffold(
        containerColor = bgColor,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = com.example.R.drawable.app_logo),
                                contentDescription = "App Logo",
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(26.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )
                            Text(
                                text = com.example.domain.languages.LocalizationEngine.getString(
                                    com.example.domain.languages.LocalizationEngine.StringKey.APP_NAME,
                                    languageCode
                                ),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = colors.headerContent
                            )
                        }
                    },
                    actions = {
                        // Classic Flat DEG / RAD toggle button
                        Surface(
                            onClick = { viewModel.onToggleAngleMode() },
                            shape = RoundedCornerShape(4.dp),
                            color = if (uiState.angleMode == AngleMode.RADIAN) colors.primaryButton else (if (colors.isCustom) Color(0xFF37474F) else (if (isDark) FlatWetAsphalt else FlatClouds)),
                            border = BorderStroke(1.dp, if (colors.isCustom) colors.primaryButton.copy(alpha = 0.5f) else borderColor),
                            modifier = Modifier
                                .padding(end = 6.dp)
                                .height(36.dp)
                        ) {
                            Box(
                                modifier = Modifier.padding(horizontal = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (uiState.angleMode == AngleMode.DEGREE) "DEG" else "RAD",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = if (uiState.angleMode == AngleMode.RADIAN || colors.isCustom) Color.White else (if (isDark) FlatDarkText else FlatLightText)
                                )
                            }
                        }

                        // Scientific panel toggle (Flat style)
                        IconButton(
                            onClick = { viewModel.onScientificToggle() },
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("scientific_toggle_button")
                        ) {
                            Icon(
                                imageVector = if (uiState.isScientificExpanded) Icons.Default.Science else Icons.Default.Calculate,
                                contentDescription = "Scientific Functions",
                                tint = if (uiState.isScientificExpanded) colors.primaryButton else (if (colors.isCustom) Color.White.copy(alpha = 0.85f) else (if (isDark) FlatDarkTextMuted else FlatLightTextMuted))
                            )
                        }

                        // History shortcut
                        IconButton(
                            onClick = onNavigateToHistory,
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("history_shortcut_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = "History",
                                tint = if (colors.isCustom) Color.White.copy(alpha = 0.85f) else (if (isDark) FlatDarkTextMuted else FlatLightTextMuted)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colors.header
                    )
                )
                // Crisp flat hairline separator
                HorizontalDivider(thickness = 1.dp, color = borderColor)
            }
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val isShortScreen = maxHeight < 560.dp
            val scrollModifier = if (isShortScreen) Modifier.verticalScroll(mainScrollState) else Modifier

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .then(scrollModifier)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Display Section: Takes weight(1f) to fill all available top space and anchor keypad at bottom
                Surface(
                    modifier = if (isShortScreen) {
                        Modifier.fillMaxWidth().heightIn(min = 140.dp)
                    } else {
                        Modifier.fillMaxWidth().weight(1f)
                    },
                    shape = RoundedCornerShape(4.dp),
                    color = surfaceColor,
                    border = BorderStroke(1.dp, borderColor),
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                    // Top Status Row (Memory badge & Ans badge)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            if (uiState.hasMemory) {
                                Surface(
                                    shape = RoundedCornerShape(2.dp),
                                    color = if (isDark) FlatWetAsphalt else FlatClouds,
                                    border = BorderStroke(1.dp, borderColor)
                                ) {
                                    Text(
                                        text = "M = ${com.example.domain.evaluator.CalculatorEngine.formatResult(uiState.memory, precision)}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = FlatBelizeHole,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            if (uiState.hasAns) {
                                Surface(
                                    shape = RoundedCornerShape(2.dp),
                                    color = if (isDark) FlatWetAsphalt else FlatClouds,
                                    border = BorderStroke(1.dp, borderColor)
                                ) {
                                    Text(
                                        text = "Ans",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = FlatGreenSea,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        // Right Status Indicator
                        Text(
                            text = if (uiState.angleMode == AngleMode.DEGREE) "DEG" else "RAD",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.mutedText
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Expression Line
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(exprScrollState),
                        horizontalArrangement = Arrangement.End
                    ) {
                        val displayExpression = if (uiState.expression.isEmpty()) {
                            "0"
                        } else {
                            numeralScript.convert(uiState.expression)
                        }
                        Text(
                            text = displayExpression,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily.Monospace,
                            color = colors.mainText,
                            textAlign = TextAlign.End,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Result / Preview Line (Supports wrap & error handling)
                    val resultText = when {
                        uiState.errorMessage != null -> uiState.errorMessage!!
                        uiState.finalResult.isNotEmpty() -> "= ${numeralScript.convert(uiState.finalResult)}"
                        uiState.previewResult.isNotEmpty() -> numeralScript.convert(uiState.previewResult)
                        else -> ""
                    }

                    val isError = uiState.errorMessage != null
                    val resultColor = when {
                        isError -> FlatAlizarin
                        uiState.finalResult.isNotEmpty() -> if (colors.isCustom) colors.primaryButton else (if (isDark) FlatEmerald else FlatNephritis)
                        else -> colors.mutedText
                    }

                    Text(
                        text = resultText,
                        fontSize = if (isError) 18.sp else 32.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = resultColor,
                        textAlign = TextAlign.End,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("calculator_result_text")
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Bottom utility row inside display box: Copy, Paste, Backspace (minimum 48dp touch targets)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            IconButton(
                                onClick = {
                                    val textToCopy = uiState.finalResult.ifEmpty { uiState.expression }
                                    if (textToCopy.isNotEmpty()) {
                                        viewModel.copyToClipboard(context, textToCopy)
                                    }
                                },
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("copy_button")
                            ) {
                                Icon(
                                    Icons.Default.ContentCopy,
                                    contentDescription = "Copy",
                                    modifier = Modifier.size(18.dp),
                                    tint = colors.mutedText
                                )
                            }

                            IconButton(
                                onClick = { viewModel.pasteFromClipboard(context) },
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("paste_button")
                            ) {
                                Icon(
                                    Icons.Default.ContentPaste,
                                    contentDescription = "Paste",
                                    modifier = Modifier.size(18.dp),
                                    tint = colors.mutedText
                                )
                            }
                        }

                        IconButton(
                            onClick = { viewModel.onBackspace() },
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("backspace_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Backspace,
                                contentDescription = "Backspace",
                                modifier = Modifier.size(20.dp),
                                tint = FlatAlizarin
                            )
                        }
                    }
                }
            }

            // Keypad Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Flat Memory Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CalcButton("MC", Modifier.weight(1f), CalcButtonType.SCIENTIFIC, height = 40.dp, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onMemoryClear() }
                    CalcButton("MR", Modifier.weight(1f), CalcButtonType.SCIENTIFIC, height = 40.dp, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onMemoryRecall() }
                    CalcButton("M+", Modifier.weight(1f), CalcButtonType.SCIENTIFIC, height = 40.dp, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onMemoryAdd() }
                    CalcButton("M−", Modifier.weight(1f), CalcButtonType.SCIENTIFIC, height = 40.dp, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onMemorySub() }
                    CalcButton("MS", Modifier.weight(1f), CalcButtonType.SCIENTIFIC, height = 40.dp, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onMemoryStore() }
                }

                // Expandable Scientific Pad (Flat Style)
                AnimatedVisibility(
                    visible = uiState.isScientificExpanded,
                    enter = expandVertically(animationSpec = tween(200)) + fadeIn(),
                    exit = shrinkVertically(animationSpec = tween(150)) + fadeOut()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            CalcButton("sin", Modifier.weight(1f), CalcButtonType.SCIENTIFIC, height = 42.dp, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("sin(") }
                            CalcButton("cos", Modifier.weight(1f), CalcButtonType.SCIENTIFIC, height = 42.dp, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("cos(") }
                            CalcButton("tan", Modifier.weight(1f), CalcButtonType.SCIENTIFIC, height = 42.dp, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("tan(") }
                            CalcButton("ln", Modifier.weight(1f), CalcButtonType.SCIENTIFIC, height = 42.dp, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("ln(") }
                            CalcButton("log", Modifier.weight(1f), CalcButtonType.SCIENTIFIC, height = 42.dp, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("log(") }
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            CalcButton("sin⁻¹", Modifier.weight(1f), CalcButtonType.SCIENTIFIC, height = 42.dp, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("sin⁻¹(") }
                            CalcButton("cos⁻¹", Modifier.weight(1f), CalcButtonType.SCIENTIFIC, height = 42.dp, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("cos⁻¹(") }
                            CalcButton("tan⁻¹", Modifier.weight(1f), CalcButtonType.SCIENTIFIC, height = 42.dp, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("tan⁻¹(") }
                            CalcButton("sinh", Modifier.weight(1f), CalcButtonType.SCIENTIFIC, height = 42.dp, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("sinh(") }
                            CalcButton("cosh", Modifier.weight(1f), CalcButtonType.SCIENTIFIC, height = 42.dp, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("cosh(") }
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            CalcButton("x²", Modifier.weight(1f), CalcButtonType.SCIENTIFIC, height = 42.dp, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("^2") }
                            CalcButton("x³", Modifier.weight(1f), CalcButtonType.SCIENTIFIC, height = 42.dp, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("^3") }
                            CalcButton("xʸ", Modifier.weight(1f), CalcButtonType.SCIENTIFIC, height = 42.dp, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("^") }
                            CalcButton("√", Modifier.weight(1f), CalcButtonType.SCIENTIFIC, height = 42.dp, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("√(") }
                            CalcButton("∛", Modifier.weight(1f), CalcButtonType.SCIENTIFIC, height = 42.dp, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("∛(") }
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            CalcButton("1/x", Modifier.weight(1f), CalcButtonType.SCIENTIFIC, height = 42.dp, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onReciprocal() }
                            CalcButton("n!", Modifier.weight(1f), CalcButtonType.SCIENTIFIC, height = 42.dp, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("!") }
                            CalcButton("π", Modifier.weight(1f), CalcButtonType.SCIENTIFIC, height = 42.dp, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("π") }
                            CalcButton("e", Modifier.weight(1f), CalcButtonType.SCIENTIFIC, height = 42.dp, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("e") }
                            CalcButton("Ans", Modifier.weight(1f), CalcButtonType.SCIENTIFIC, height = 42.dp, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAns() }
                        }
                    }
                }

                // Basic Keypad Grid (4 columns, 5 rows)
                val buttonHeight = if (uiState.isScientificExpanded) 46.dp else 62.dp

                // Row 1: C, ( ), %, ÷
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    CalcButton("C", Modifier.weight(1f), CalcButtonType.DANGER, height = buttonHeight, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onClear() }
                    CalcButton("( )", Modifier.weight(1f), CalcButtonType.FUNCTION, height = buttonHeight, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onParenthesis() }
                    CalcButton("%", Modifier.weight(1f), CalcButtonType.FUNCTION, height = buttonHeight, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("%") }
                    CalcButton("÷", Modifier.weight(1f), CalcButtonType.OPERATOR, height = buttonHeight, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onOperator("÷") }
                }

                // Row 2: 7, 8, 9, ×
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    CalcButton("7", Modifier.weight(1f), CalcButtonType.NUMBER, height = buttonHeight, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("7") }
                    CalcButton("8", Modifier.weight(1f), CalcButtonType.NUMBER, height = buttonHeight, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("8") }
                    CalcButton("9", Modifier.weight(1f), CalcButtonType.NUMBER, height = buttonHeight, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("9") }
                    CalcButton("×", Modifier.weight(1f), CalcButtonType.OPERATOR, height = buttonHeight, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onOperator("×") }
                }

                // Row 3: 4, 5, 6, −
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    CalcButton("4", Modifier.weight(1f), CalcButtonType.NUMBER, height = buttonHeight, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("4") }
                    CalcButton("5", Modifier.weight(1f), CalcButtonType.NUMBER, height = buttonHeight, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("5") }
                    CalcButton("6", Modifier.weight(1f), CalcButtonType.NUMBER, height = buttonHeight, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("6") }
                    CalcButton("−", Modifier.weight(1f), CalcButtonType.OPERATOR, height = buttonHeight, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onOperator("−") }
                }

                // Row 4: 1, 2, 3, +
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    CalcButton("1", Modifier.weight(1f), CalcButtonType.NUMBER, height = buttonHeight, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("1") }
                    CalcButton("2", Modifier.weight(1f), CalcButtonType.NUMBER, height = buttonHeight, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("2") }
                    CalcButton("3", Modifier.weight(1f), CalcButtonType.NUMBER, height = buttonHeight, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("3") }
                    CalcButton("+", Modifier.weight(1f), CalcButtonType.OPERATOR, height = buttonHeight, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onOperator("+") }
                }

                // Row 5: ±, 0, ., =
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    CalcButton("±", Modifier.weight(1f), CalcButtonType.FUNCTION, height = buttonHeight, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onTogglePlusMinus() }
                    CalcButton("0", Modifier.weight(1f), CalcButtonType.NUMBER, height = buttonHeight, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend("0") }
                    CalcButton(".", Modifier.weight(1f), CalcButtonType.FUNCTION, height = buttonHeight, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onAppend(".") }
                    CalcButton("=", Modifier.weight(1f), CalcButtonType.EQUALS, height = buttonHeight, feedbackHelper = feedbackHelper, hapticsEnabled = hapticsEnabled, soundEnabled = soundEnabled) { viewModel.onEquals() }
                }
            }
        }
    }
}
}
