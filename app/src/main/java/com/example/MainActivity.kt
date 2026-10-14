package com.example

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.preferences.AppPreferences
import com.example.data.preferences.ThemeMode
import com.example.ui.calculator.CalculatorScreen
import com.example.ui.calculator.CalculatorViewModel
import com.example.ui.components.AppBottomBar
import com.example.ui.components.AppScreen
import com.example.ui.history.HistoryScreen
import com.example.ui.settings.SettingsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.tools.ToolsHubScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val preferences = remember { AppPreferences.getInstance(applicationContext) }
            val themeMode by preferences.themeMode.collectAsState()

            val isDarkTheme = when (themeMode) {
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                ThemeMode.DARK -> true
                ThemeMode.LIGHT -> false
            }

            val view = LocalView.current
            if (!view.isInEditMode) {
                SideEffect {
                    val window = (view.context as? Activity)?.window
                    if (window != null) {
                        val insetsController = WindowCompat.getInsetsController(window, view)
                        insetsController.isAppearanceLightStatusBars = !isDarkTheme
                        insetsController.isAppearanceLightNavigationBars = !isDarkTheme
                    }
                }
            }

            MyApplicationTheme(
                themeMode = themeMode,
                darkTheme = isDarkTheme
            ) {
                MainAppContainer()
            }
        }
    }
}

@Composable
fun MainAppContainer(
    calculatorViewModel: CalculatorViewModel = viewModel()
) {
    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.CALCULATOR) }

    // If on subscreen, Android back button pops back to Calculator
    BackHandler(enabled = currentScreen != AppScreen.CALCULATOR) {
        currentScreen = AppScreen.CALCULATOR
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            AppBottomBar(
                currentScreen = currentScreen,
                onScreenSelected = { selected ->
                    currentScreen = selected
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.CALCULATOR -> {
                    CalculatorScreen(
                        viewModel = calculatorViewModel,
                        onNavigateToHistory = { currentScreen = AppScreen.HISTORY }
                    )
                }
                AppScreen.TOOLS -> {
                    ToolsHubScreen(
                        calculatorViewModel = calculatorViewModel
                    )
                }
                AppScreen.HISTORY -> {
                    HistoryScreen(
                        calculatorViewModel = calculatorViewModel,
                        onNavigateBack = { currentScreen = AppScreen.CALCULATOR }
                    )
                }
                AppScreen.SETTINGS -> {
                    SettingsScreen()
                }
            }
        }
    }
}
