package com.example.ui.calculator

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.FeedbackHelper
import com.example.ui.theme.*

enum class CalcButtonType {
    NUMBER,
    OPERATOR,
    FUNCTION,
    EQUALS,
    DANGER,
    SCIENTIFIC
}

/**
 * Classic Flat UI Calculator Tile.
 * Characteristic flat design: 0.dp elevation, crisp 1.dp hairline border,
 * geometric rounded corners (4.dp), solid matte colors, and zero skeuomorphic shadows.
 */
@Composable
fun CalcButton(
    text: String,
    modifier: Modifier = Modifier,
    type: CalcButtonType = CalcButtonType.NUMBER,
    tag: String = text,
    height: Dp = 60.dp,
    feedbackHelper: FeedbackHelper? = null,
    hapticsEnabled: Boolean = true,
    soundEnabled: Boolean = false,
    onClick: () -> Unit
) {
    val view = LocalView.current
    val colors = LocalFlatThemeColors.current
    val isDark = colors.isDark
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Authentic Flat UI Palette Colors
    val (defaultBg, defaultText, borderStroke) = when (type) {
        CalcButtonType.NUMBER -> {
            val bg = colors.keyNum
            val txt = colors.keyNumText
            val border = BorderStroke(1.dp, colors.border)
            Triple(bg, txt, border)
        }
        CalcButtonType.OPERATOR -> {
            val border = BorderStroke(1.dp, if (colors.isCustom) colors.keyOp else FlatPumpkin)
            val txtColor = if (colors.isCustom) Color.White else Color(0xFF1A252F)
            Triple(colors.keyOp, txtColor, border)
        }
        CalcButtonType.EQUALS -> {
            val border = BorderStroke(1.dp, if (colors.isCustom) colors.keyEquals else FlatNephritis)
            val txtColor = if (colors.isCustom) Color(0xFF0A332C) else Color(0xFF0B331A)
            Triple(colors.keyEquals, txtColor, border)
        }
        CalcButtonType.DANGER -> {
            val border = BorderStroke(1.dp, FlatPomegranate)
            Triple(FlatKeyDanger, Color.White, border)
        }
        CalcButtonType.FUNCTION -> {
            val bg = colors.keyFn
            val txt = if (colors.isCustom) colors.mainText else (if (isDark) FlatClouds else FlatMidnightBlue)
            val border = BorderStroke(1.dp, colors.border)
            Triple(bg, txt, border)
        }
        CalcButtonType.SCIENTIFIC -> {
            val bg = if (colors.isCustom) Color(0xFFECEFF1) else (if (isDark) FlatKeySciDark else FlatKeySciLight)
            val txt = if (colors.isCustom) colors.header else (if (isDark) FlatPeterRiver else FlatBelizeHole)
            val border = BorderStroke(1.dp, colors.border.copy(alpha = 0.7f))
            Triple(bg, txt, border)
        }
    }

    // Flat UI interaction: clean matte color shift when pressed, no 3D bounce
    val currentBg = if (isPressed) {
        when (type) {
            CalcButtonType.OPERATOR -> if (colors.isCustom) Color(0xFF37474F) else FlatKeyOpPressed
            CalcButtonType.EQUALS -> if (colors.isCustom) Color(0xFF0F9C86) else FlatNephritis
            CalcButtonType.DANGER -> FlatPomegranate
            CalcButtonType.NUMBER -> if (colors.isCustom) Color(0xFFECEFF1) else (if (isDark) FlatDarkSurfaceVariant else FlatClouds)
            CalcButtonType.FUNCTION -> if (colors.isCustom) Color(0xFFCFD8DC) else (if (isDark) FlatMidnightBlue else FlatSilver)
            CalcButtonType.SCIENTIFIC -> if (colors.isCustom) Color(0xFFCFD8DC) else (if (isDark) FlatWetAsphalt else FlatClouds)
        }
    } else {
        defaultBg
    }

    val fontSize = when {
        type == CalcButtonType.SCIENTIFIC -> 14.sp
        text.length > 3 -> 15.sp
        text.length > 1 -> 19.sp
        else -> 23.sp
    }

    Surface(
        modifier = modifier
            .height(height)
            .testTag(tag)
            .clickable(
                interactionSource = interactionSource,
                indication = null, // Crisp color swap replaces blurry radial ripple for clean flat feel
                onClick = {
                    feedbackHelper?.triggerClick(view, hapticsEnabled, soundEnabled)
                    onClick()
                }
            ),
        shape = RoundedCornerShape(4.dp), // Classic Flat UI subtle geometric corner
        color = currentBg,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = borderStroke
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = defaultText,
                fontSize = fontSize,
                fontWeight = if (type == CalcButtonType.NUMBER) FontWeight.Bold else FontWeight.SemiBold,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center
            )
        }
    }
}
