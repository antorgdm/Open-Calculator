package com.example.ui.tools

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.preferences.AppPreferences
import com.example.domain.programmer.BitSize
import com.example.domain.programmer.NumberBase
import com.example.domain.programmer.ProgrammerEngine
import com.example.ui.components.FeedbackHelper
import com.example.ui.theme.*

@Composable
fun ProgrammerCalculatorScreen() {
    val context = LocalContext.current
    val colors = LocalFlatThemeColors.current
    val preferences = remember { AppPreferences.getInstance(context) }
    val haptics by preferences.hapticsEnabled.collectAsState()
    val sound by preferences.soundEnabled.collectAsState()
    val feedbackHelper = remember { FeedbackHelper(context) }

    var bitSize by remember { mutableStateOf(BitSize.QWORD) }
    var activeBase by remember { mutableStateOf(NumberBase.DEC) }
    var currentValue by remember { mutableStateOf(0uL) }
    var pendingOp by remember { mutableStateOf<String?>(null) }
    var operandA by remember { mutableStateOf<ULong?>(null) }
    var isNewEntry by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Bit size chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BitSize.values().forEach { size ->
                FilterChip(
                    selected = bitSize == size,
                    onClick = {
                        bitSize = size
                        currentValue = ProgrammerEngine.clampToBitSize(currentValue, size)
                    },
                    label = { Text(size.label.split(" ")[1]) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(4.dp)
                )
            }
        }

        // Simultaneous Base Readout Cards
        Surface(
            shape = RoundedCornerShape(4.dp),
            color = colors.surface,
            border = BorderStroke(1.dp, colors.border),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                BaseReadoutRow(
                    base = NumberBase.HEX,
                    isActive = activeBase == NumberBase.HEX,
                    value = ProgrammerEngine.formatInBase(currentValue, NumberBase.HEX, bitSize)
                ) { activeBase = NumberBase.HEX }

                BaseReadoutRow(
                    base = NumberBase.DEC,
                    isActive = activeBase == NumberBase.DEC,
                    value = ProgrammerEngine.formatInBase(currentValue, NumberBase.DEC, bitSize)
                ) { activeBase = NumberBase.DEC }

                BaseReadoutRow(
                    base = NumberBase.OCT,
                    isActive = activeBase == NumberBase.OCT,
                    value = ProgrammerEngine.formatInBase(currentValue, NumberBase.OCT, bitSize)
                ) { activeBase = NumberBase.OCT }

                BaseReadoutRow(
                    base = NumberBase.BIN,
                    isActive = activeBase == NumberBase.BIN,
                    value = ProgrammerEngine.formatInBase(currentValue, NumberBase.BIN, bitSize)
                ) { activeBase = NumberBase.BIN }
            }
        }

        // Interactive Full Bit Board (displays all bits up to 64)
        Surface(
            shape = RoundedCornerShape(4.dp),
            color = colors.surface,
            border = BorderStroke(1.dp, colors.border),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    "Interactive Bit Board (${bitSize.bits} bits — Tap to toggle)",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.mutedText,
                    fontWeight = FontWeight.Bold
                )

                val rows = when (bitSize) {
                    BitSize.QWORD -> listOf(63 downTo 48, 47 downTo 32, 31 downTo 16, 15 downTo 0)
                    BitSize.DWORD -> listOf(31 downTo 16, 15 downTo 0)
                    BitSize.WORD -> listOf(15 downTo 0)
                    BitSize.BYTE -> listOf(7 downTo 0)
                }

                rows.forEach { bitRange ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${bitRange.first}",
                            fontSize = 9.sp,
                            color = colors.mutedText,
                            modifier = Modifier.width(18.dp)
                        )
                        for (i in bitRange) {
                            val isSet = ProgrammerEngine.isBitSet(currentValue, i)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(24.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(if (isSet) colors.primaryButton else colors.background)
                                    .clickable {
                                        currentValue = ProgrammerEngine.toggleBit(currentValue, i, bitSize)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isSet) "1" else "0",
                                    fontSize = 11.sp,
                                    color = if (isSet) Color(0xFF07332C) else colors.mutedText,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Programmer Keypad
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Operator row: AND, OR, XOR, NOT, <<, >>
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                ProgKey("AND", Modifier.weight(1f), isOp = true) {
                    operandA = currentValue
                    pendingOp = "AND"
                    isNewEntry = true
                }
                ProgKey("OR", Modifier.weight(1f), isOp = true) {
                    operandA = currentValue
                    pendingOp = "OR"
                    isNewEntry = true
                }
                ProgKey("XOR", Modifier.weight(1f), isOp = true) {
                    operandA = currentValue
                    pendingOp = "XOR"
                    isNewEntry = true
                }
                ProgKey("NOT", Modifier.weight(1f), isOp = true) {
                    currentValue = ProgrammerEngine.bitwiseNot(currentValue, bitSize)
                }
                ProgKey("<<", Modifier.weight(1f), isOp = true) {
                    currentValue = ProgrammerEngine.shiftLeft(currentValue, 1, bitSize)
                }
                ProgKey(">>", Modifier.weight(1f), isOp = true) {
                    currentValue = ProgrammerEngine.shiftRight(currentValue, 1, bitSize)
                }
            }

            // Hex A-F / Digits
            val hexEnabled = activeBase == NumberBase.HEX
            val octEnabled = activeBase == NumberBase.HEX || activeBase == NumberBase.DEC || activeBase == NumberBase.OCT
            val decEnabled = activeBase == NumberBase.HEX || activeBase == NumberBase.DEC

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                ProgKey("A", Modifier.weight(1f), enabled = hexEnabled) { onAppendDigit("A", activeBase, currentValue, isNewEntry, bitSize) { v, n -> currentValue = v; isNewEntry = n } }
                ProgKey("B", Modifier.weight(1f), enabled = hexEnabled) { onAppendDigit("B", activeBase, currentValue, isNewEntry, bitSize) { v, n -> currentValue = v; isNewEntry = n } }
                ProgKey("7", Modifier.weight(1f), enabled = octEnabled) { onAppendDigit("7", activeBase, currentValue, isNewEntry, bitSize) { v, n -> currentValue = v; isNewEntry = n } }
                ProgKey("8", Modifier.weight(1f), enabled = decEnabled) { onAppendDigit("8", activeBase, currentValue, isNewEntry, bitSize) { v, n -> currentValue = v; isNewEntry = n } }
                ProgKey("9", Modifier.weight(1f), enabled = decEnabled) { onAppendDigit("9", activeBase, currentValue, isNewEntry, bitSize) { v, n -> currentValue = v; isNewEntry = n } }
                ProgKey("CLR", Modifier.weight(1f), isDanger = true) { currentValue = 0uL; operandA = null; pendingOp = null; isNewEntry = true }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                ProgKey("C", Modifier.weight(1f), enabled = hexEnabled) { onAppendDigit("C", activeBase, currentValue, isNewEntry, bitSize) { v, n -> currentValue = v; isNewEntry = n } }
                ProgKey("D", Modifier.weight(1f), enabled = hexEnabled) { onAppendDigit("D", activeBase, currentValue, isNewEntry, bitSize) { v, n -> currentValue = v; isNewEntry = n } }
                ProgKey("4", Modifier.weight(1f), enabled = octEnabled) { onAppendDigit("4", activeBase, currentValue, isNewEntry, bitSize) { v, n -> currentValue = v; isNewEntry = n } }
                ProgKey("5", Modifier.weight(1f), enabled = octEnabled) { onAppendDigit("5", activeBase, currentValue, isNewEntry, bitSize) { v, n -> currentValue = v; isNewEntry = n } }
                ProgKey("6", Modifier.weight(1f), enabled = octEnabled) { onAppendDigit("6", activeBase, currentValue, isNewEntry, bitSize) { v, n -> currentValue = v; isNewEntry = n } }
                ProgKey("⌫", Modifier.weight(1f)) {
                    val str = currentValue.toString(activeBase.radix)
                    if (str.length > 1) {
                        currentValue = ProgrammerEngine.parseInput(str.dropLast(1), activeBase, bitSize) ?: 0uL
                    } else {
                        currentValue = 0uL
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                ProgKey("E", Modifier.weight(1f), enabled = hexEnabled) { onAppendDigit("E", activeBase, currentValue, isNewEntry, bitSize) { v, n -> currentValue = v; isNewEntry = n } }
                ProgKey("F", Modifier.weight(1f), enabled = hexEnabled) { onAppendDigit("F", activeBase, currentValue, isNewEntry, bitSize) { v, n -> currentValue = v; isNewEntry = n } }
                ProgKey("1", Modifier.weight(1f)) { onAppendDigit("1", activeBase, currentValue, isNewEntry, bitSize) { v, n -> currentValue = v; isNewEntry = n } }
                ProgKey("2", Modifier.weight(1f), enabled = octEnabled) { onAppendDigit("2", activeBase, currentValue, isNewEntry, bitSize) { v, n -> currentValue = v; isNewEntry = n } }
                ProgKey("3", Modifier.weight(1f), enabled = octEnabled) { onAppendDigit("3", activeBase, currentValue, isNewEntry, bitSize) { v, n -> currentValue = v; isNewEntry = n } }
                ProgKey("±", Modifier.weight(1f)) {
                    // Two's complement negate
                    val signed = currentValue.toLong()
                    currentValue = ProgrammerEngine.clampToBitSize((-signed).toULong(), bitSize)
                }
            }

            // Bottom row: 0 and =
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                ProgKey("0", Modifier.weight(2f)) { onAppendDigit("0", activeBase, currentValue, isNewEntry, bitSize) { v, n -> currentValue = v; isNewEntry = n } }
                Surface(
                    onClick = {
                        val a = operandA
                        val op = pendingOp
                        if (a != null && op != null) {
                            val res = when (op) {
                                "AND" -> ProgrammerEngine.bitwiseAnd(a, currentValue, bitSize)
                                "OR" -> ProgrammerEngine.bitwiseOr(a, currentValue, bitSize)
                                "XOR" -> ProgrammerEngine.bitwiseXor(a, currentValue, bitSize)
                                else -> currentValue
                            }
                            currentValue = res
                            operandA = null
                            pendingOp = null
                            isNewEntry = true
                        }
                    },
                    modifier = Modifier
                        .weight(4f)
                        .height(44.dp),
                    shape = RoundedCornerShape(4.dp),
                    color = colors.primaryButton,
                    border = BorderStroke(1.dp, colors.primaryButton)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("=", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF07332C))
                    }
                }
            }
        }
    }
}

private fun onAppendDigit(
    digit: String,
    base: NumberBase,
    current: ULong,
    isNew: Boolean,
    bitSize: BitSize,
    onResult: (ULong, Boolean) -> Unit
) {
    val currentStr = if (isNew || current == 0uL) "" else current.toString(base.radix)
    val updatedStr = currentStr + digit
    val parsed = ProgrammerEngine.parseInput(updatedStr, base, bitSize)
    if (parsed != null) {
        onResult(parsed, false)
    }
}

@Composable
private fun BaseReadoutRow(
    base: NumberBase,
    isActive: Boolean,
    value: String,
    onSelect: () -> Unit
) {
    val colors = LocalFlatThemeColors.current
    Surface(
        onClick = onSelect,
        shape = RoundedCornerShape(4.dp),
        color = if (isActive) colors.background else colors.surface,
        border = BorderStroke(1.dp, if (isActive) colors.primaryButton else colors.border),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = base.label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isActive) colors.primaryButton else colors.mutedText,
                modifier = Modifier.width(36.dp)
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                fontFamily = FontFamily.Monospace,
                color = colors.mainText,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun ProgKey(
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isOp: Boolean = false,
    isDanger: Boolean = false,
    onClick: () -> Unit
) {
    val colors = LocalFlatThemeColors.current
    val isDark = colors.isDark

    val bg = when {
        !enabled -> colors.surface.copy(alpha = 0.4f)
        isDanger -> FlatAlizarin
        isOp -> if (isDark) FlatWetAsphalt else FlatClouds
        else -> colors.surface
    }

    val textCol = when {
        !enabled -> colors.mutedText.copy(alpha = 0.4f)
        isDanger -> Color.White
        isOp -> colors.primaryButton
        else -> colors.mainText
    }

    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(4.dp),
        color = bg,
        border = BorderStroke(1.dp, colors.border.copy(alpha = if (enabled) 1f else 0.4f)),
        modifier = modifier.height(44.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = textCol
            )
        }
    }
}
