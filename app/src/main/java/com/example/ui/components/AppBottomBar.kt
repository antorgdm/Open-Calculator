package com.example.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

enum class AppScreen(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    CALCULATOR("Calculator", Icons.Filled.Calculate, Icons.Outlined.Calculate, "nav_calculator"),
    TOOLS("Tools", Icons.Filled.Widgets, Icons.Outlined.Widgets, "nav_tools"),
    HISTORY("History", Icons.Filled.History, Icons.Outlined.History, "nav_history"),
    SETTINGS("Settings", Icons.Filled.Settings, Icons.Outlined.Settings, "nav_settings")
}

@Composable
fun AppBottomBar(
    currentScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val colors = LocalFlatThemeColors.current
    val isDark = colors.isDark
    val preferences = androidx.compose.runtime.remember { com.example.data.preferences.AppPreferences.getInstance(context) }
    val languageCode by preferences.languageCode.collectAsState()

    val surfaceColor = colors.surface
    val borderColor = colors.border

    Column {
        // Classic Flat hairline top border
        HorizontalDivider(thickness = 1.dp, color = borderColor)

        NavigationBar(
            containerColor = surfaceColor,
            tonalElevation = 0.dp
        ) {
            AppScreen.values().forEach { screen ->
                val isSelected = currentScreen == screen
                val localizedTitle = when (screen) {
                    AppScreen.CALCULATOR -> com.example.domain.languages.LocalizationEngine.getString(
                        com.example.domain.languages.LocalizationEngine.StringKey.CALCULATOR,
                        languageCode
                    )
                    AppScreen.TOOLS -> com.example.domain.languages.LocalizationEngine.getString(
                        com.example.domain.languages.LocalizationEngine.StringKey.TOOLS,
                        languageCode
                    )
                    AppScreen.HISTORY -> com.example.domain.languages.LocalizationEngine.getString(
                        com.example.domain.languages.LocalizationEngine.StringKey.HISTORY,
                        languageCode
                    )
                    AppScreen.SETTINGS -> com.example.domain.languages.LocalizationEngine.getString(
                        com.example.domain.languages.LocalizationEngine.StringKey.SETTINGS,
                        languageCode
                    )
                }

                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onScreenSelected(screen) },
                    icon = {
                        Icon(
                            imageVector = if (isSelected) screen.selectedIcon else screen.unselectedIcon,
                            contentDescription = localizedTitle,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    label = { Text(localizedTitle) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = colors.primaryButton,
                        indicatorColor = colors.primaryButton,
                        unselectedIconColor = colors.mutedText,
                        unselectedTextColor = colors.mutedText
                    ),
                    modifier = Modifier.testTag(screen.testTag)
                )
            }
        }
    }
}
