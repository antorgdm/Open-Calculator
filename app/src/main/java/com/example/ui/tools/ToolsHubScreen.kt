package com.example.ui.tools

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.calculator.CalculatorViewModel
import com.example.ui.theme.*

enum class ToolDestination(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val flatColor: Color
) {
    CURRENCY("Currency Converter", "Every world currency & live conversions", Icons.Default.Paid, FlatEmerald),
    UNIT_CONVERTER("Unit Converter", "13 categories & live tables", Icons.Default.SwapHoriz, FlatPeterRiver),
    PERCENTAGE("Percentage Tools", "Discounts, markup, VAT & changes", Icons.Default.Percent, FlatCarrot),
    FINANCIAL("Financial Calculator", "Loan EMI, SIP, compound interest", Icons.Default.AccountBalance, FlatTurquoise),
    DATE_TIME("Date & Time", "Age, difference, working days", Icons.Default.CalendarToday, FlatAmethyst),
    GEOMETRY("Geometry", "2D & 3D shapes, volume, area", Icons.Default.SquareFoot, FlatOrange),
    PROGRAMMER("Programmer Calc", "HEX, DEC, OCT, BIN & bit toggles", Icons.Default.Code, FlatWetAsphalt),
    STATISTICS_EQUATION("Stats & Equations", "Mean, SD, quadratic & linear solvers", Icons.Default.Functions, FlatBelizeHole),
    WORLD_LANGUAGES("World Languages & Numerals", "140+ languages, scripts & number translator", Icons.Default.Language, FlatGreenSea)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsHubScreen(
    calculatorViewModel: CalculatorViewModel
) {
    val colors = LocalFlatThemeColors.current
    val isDark = colors.isDark
    var activeTool by remember { mutableStateOf<ToolDestination?>(null) }

    val surfaceColor = colors.surface
    val borderColor = colors.border
    val bgColor = colors.background

    BackHandler(enabled = activeTool != null) {
        activeTool = null
    }

    Scaffold(
        containerColor = bgColor,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = activeTool?.title ?: "Calculation Tools",
                            fontWeight = FontWeight.Bold,
                            color = colors.headerContent
                        )
                    },
                    navigationIcon = {
                        if (activeTool != null) {
                            IconButton(onClick = { activeTool = null }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back to Tools Hub",
                                    tint = colors.headerContent
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colors.header
                    )
                )
                HorizontalDivider(thickness = 1.dp, color = borderColor)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (activeTool == null) {
                // Classic Flat Grid of Tools
                LazyVerticalGrid(
                    columns = GridCells.Fixed(1),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(ToolDestination.values()) { tool ->
                        FlatToolHubCard(
                            tool = tool,
                            onClick = { activeTool = tool }
                        )
                    }
                }
            } else {
                // Active Tool Screen
                when (activeTool) {
                    ToolDestination.CURRENCY -> CurrencyConverterScreen(calculatorViewModel)
                    ToolDestination.UNIT_CONVERTER -> UnitConverterScreen(calculatorViewModel)
                    ToolDestination.PERCENTAGE -> PercentageToolsScreen()
                    ToolDestination.FINANCIAL -> FinancialToolsScreen()
                    ToolDestination.DATE_TIME -> DateTimeToolsScreen()
                    ToolDestination.GEOMETRY -> GeometryToolsScreen()
                    ToolDestination.PROGRAMMER -> ProgrammerCalculatorScreen()
                    ToolDestination.STATISTICS_EQUATION -> StatisticsEquationScreen()
                    ToolDestination.WORLD_LANGUAGES -> LanguageNumeralsToolScreen()
                    null -> {}
                }
            }
        }
    }
}

/**
 * Classic Flat UI Tool Card: 0.dp elevation, crisp 1.dp border, 4.dp corner, solid flat badge.
 */
@Composable
private fun FlatToolHubCard(
    tool: ToolDestination,
    onClick: () -> Unit
) {
    val colors = LocalFlatThemeColors.current
    val surfaceColor = colors.surface
    val borderColor = colors.border

    Surface(
        shape = RoundedCornerShape(4.dp),
        color = surfaceColor,
        border = BorderStroke(1.dp, borderColor),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("tool_card_${tool.name.lowercase()}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Flat Color Badge Tile
            Surface(
                shape = RoundedCornerShape(3.dp),
                color = tool.flatColor,
                modifier = Modifier.size(42.dp),
                tonalElevation = 0.dp,
                shadowElevation = 0.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = tool.icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tool.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.mainText
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = tool.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.mutedText
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = colors.mutedText
            )
        }
    }
}
