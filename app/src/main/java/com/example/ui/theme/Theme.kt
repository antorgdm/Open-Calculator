package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.example.data.preferences.ThemeMode

// Classic Flat Dark Color Scheme
private val FlatDarkColorScheme = darkColorScheme(
    primary = FlatDarkPrimary,
    onPrimary = Color.White,
    primaryContainer = FlatWetAsphalt,
    onPrimaryContainer = FlatClouds,
    secondary = FlatDarkSecondary,
    onSecondary = Color.White,
    secondaryContainer = FlatGreenSea,
    onSecondaryContainer = FlatClouds,
    tertiary = FlatCarrot,
    onTertiary = Color.White,
    tertiaryContainer = FlatPumpkin,
    onTertiaryContainer = FlatClouds,
    background = FlatDarkBg,
    onBackground = FlatDarkText,
    surface = FlatDarkSurface,
    onSurface = FlatDarkText,
    surfaceVariant = FlatDarkSurfaceVariant,
    onSurfaceVariant = FlatDarkTextMuted,
    outline = FlatDarkBorder,
    outlineVariant = FlatDarkBorder.copy(alpha = 0.6f),
    error = FlatAlizarin,
    onError = Color.White,
    errorContainer = FlatPomegranate,
    onErrorContainer = FlatClouds
)

// Classic Flat Light Color Scheme
private val FlatLightColorScheme = lightColorScheme(
    primary = FlatLightPrimary,
    onPrimary = Color.White,
    primaryContainer = FlatClouds,
    onPrimaryContainer = FlatBelizeHole,
    secondary = FlatLightSecondary,
    onSecondary = Color.White,
    secondaryContainer = FlatClouds,
    onSecondaryContainer = FlatGreenSea,
    tertiary = FlatCarrot,
    onTertiary = Color.White,
    tertiaryContainer = FlatClouds,
    onTertiaryContainer = FlatPumpkin,
    background = FlatLightBg,
    onBackground = FlatLightText,
    surface = FlatLightSurface,
    onSurface = FlatLightText,
    surfaceVariant = FlatLightSurfaceVariant,
    onSurfaceVariant = FlatLightTextMuted,
    outline = FlatLightBorder,
    outlineVariant = FlatLightBorder.copy(alpha = 0.6f),
    error = FlatAlizarin,
    onError = Color.White,
    errorContainer = FlatClouds,
    onErrorContainer = FlatPomegranate
)

// User-Defined Custom Classic Flat Color Scheme
// Background: #F5F5F5, Header: #455A64, Primary button: #16BFA5,
// Input background: #FFFFFF, Border: #D5D5D5, Main text: #34495E
private val CustomFlatColorScheme = lightColorScheme(
    primary = CustomFlatPrimaryBtn,            // #16BFA5 Primary button
    onPrimary = Color.White,
    primaryContainer = CustomFlatHeader,       // #455A64 Header
    onPrimaryContainer = Color.White,
    secondary = CustomFlatPrimaryBtn,          // #16BFA5
    onSecondary = Color.White,
    secondaryContainer = CustomFlatHeader,
    onSecondaryContainer = Color.White,
    tertiary = CustomFlatPrimaryBtn,
    onTertiary = Color.White,
    tertiaryContainer = CustomFlatHeader,
    onTertiaryContainer = Color.White,
    background = CustomFlatBg,                 // #F5F5F5 Background
    onBackground = CustomFlatMainText,         // #34495E Main text
    surface = CustomFlatInputBg,               // #FFFFFF Input background
    onSurface = CustomFlatMainText,            // #34495E Main text
    surfaceVariant = CustomFlatBg,             // #F5F5F5
    onSurfaceVariant = CustomFlatMutedText,
    outline = CustomFlatBorder,                // #D5D5D5 Border
    outlineVariant = CustomFlatBorder,
    error = FlatAlizarin,
    onError = Color.White,
    errorContainer = CustomFlatBg,
    onErrorContainer = FlatPomegranate
)

data class FlatThemeColors(
    val background: Color,
    val header: Color,
    val headerContent: Color,
    val primaryButton: Color,
    val primaryButtonContent: Color,
    val inputBackground: Color,
    val border: Color,
    val mainText: Color,
    val mutedText: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val keyEquals: Color,
    val keyOp: Color,
    val keyNum: Color,
    val keyNumText: Color,
    val keyFn: Color,
    val isDark: Boolean,
    val isCustom: Boolean
)

val FlatDarkColors = FlatThemeColors(
    background = FlatDarkBg,
    header = FlatDarkSurface,
    headerContent = FlatDarkText,
    primaryButton = FlatDarkPrimary,
    primaryButtonContent = Color.White,
    inputBackground = FlatDarkSurface,
    border = FlatDarkBorder,
    mainText = FlatDarkText,
    mutedText = FlatDarkTextMuted,
    surface = FlatDarkSurface,
    surfaceVariant = FlatDarkSurfaceVariant,
    keyEquals = FlatEmerald,
    keyOp = FlatKeyOp,
    keyNum = FlatKeyNumDark,
    keyNumText = FlatKeyNumTextDark,
    keyFn = FlatKeyFnDark,
    isDark = true,
    isCustom = false
)

val FlatLightColors = FlatThemeColors(
    background = FlatLightBg,
    header = FlatLightSurface,
    headerContent = FlatLightText,
    primaryButton = FlatLightPrimary,
    primaryButtonContent = Color.White,
    inputBackground = FlatLightSurface,
    border = FlatLightBorder,
    mainText = FlatLightText,
    mutedText = FlatLightTextMuted,
    surface = FlatLightSurface,
    surfaceVariant = FlatLightSurfaceVariant,
    keyEquals = FlatEmerald,
    keyOp = FlatKeyOp,
    keyNum = FlatKeyNumLight,
    keyNumText = FlatKeyNumTextLight,
    keyFn = FlatKeyFnLight,
    isDark = false,
    isCustom = false
)

val FlatCustomThemeColors = FlatThemeColors(
    background = CustomFlatBg,                // #F5F5F5
    header = CustomFlatHeader,                // #455A64
    headerContent = Color.White,
    primaryButton = CustomFlatPrimaryBtn,     // #16BFA5
    primaryButtonContent = Color.White,
    inputBackground = CustomFlatInputBg,      // #FFFFFF
    border = CustomFlatBorder,                // #D5D5D5
    mainText = CustomFlatMainText,            // #34495E
    mutedText = CustomFlatMutedText,
    surface = CustomFlatInputBg,              // #FFFFFF
    surfaceVariant = CustomFlatBg,
    keyEquals = CustomFlatPrimaryBtn,         // #16BFA5
    keyOp = CustomFlatHeader,                 // #455A64
    keyNum = CustomFlatInputBg,               // #FFFFFF
    keyNumText = CustomFlatMainText,          // #34495E
    keyFn = CustomFlatKeyFn,                  // #ECEFF1
    isDark = false,
    isCustom = true
)

val LocalFlatThemeColors = staticCompositionLocalOf { FlatLightColors }

@Composable
fun MyApplicationTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val (colorScheme, flatColors) = when (themeMode) {
        ThemeMode.DARK -> Pair(FlatDarkColorScheme, FlatDarkColors)
        ThemeMode.LIGHT -> Pair(FlatLightColorScheme, FlatLightColors)
        ThemeMode.SYSTEM -> if (darkTheme) Pair(FlatDarkColorScheme, FlatDarkColors) else Pair(FlatLightColorScheme, FlatLightColors)
    }

    CompositionLocalProvider(LocalFlatThemeColors provides flatColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
