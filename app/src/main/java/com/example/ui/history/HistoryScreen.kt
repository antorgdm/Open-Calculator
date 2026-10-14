package com.example.ui.history

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.AppDatabase
import com.example.data.database.CalculationHistoryEntity
import com.example.data.database.HistoryRepository
import com.example.ui.calculator.CalculatorViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    calculatorViewModel: CalculatorViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val colors = LocalFlatThemeColors.current
    val isDark = colors.isDark
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val database = remember { AppDatabase.getDatabase(context) }
    val repository = remember { HistoryRepository(database.calculationHistoryDao()) }

    val surfaceColor = colors.surface
    val borderColor = colors.border
    val bgColor = colors.background

    var searchQuery by remember { mutableStateOf("") }
    var filterFavoritesOnly by remember { mutableStateOf(false) }
    var showClearConfirmation by remember { mutableStateOf(false) }

    val historyList by remember(searchQuery, filterFavoritesOnly) {
        repository.filter(searchQuery, filterFavoritesOnly)
    }.collectAsState(initial = emptyList())

    Scaffold(
        containerColor = bgColor,
        contentWindowInsets = WindowInsets(0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            "Calculation History",
                            fontWeight = FontWeight.Bold,
                            color = colors.headerContent
                        )
                    },
                    actions = {
                        // Filter favorites toggle
                        IconButton(
                            onClick = { filterFavoritesOnly = !filterFavoritesOnly },
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("filter_favorites_button")
                        ) {
                            Icon(
                                imageVector = if (filterFavoritesOnly) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "Filter Favorites",
                                tint = if (filterFavoritesOnly) FlatSunFlower else colors.headerContent.copy(alpha = 0.85f)
                            )
                        }

                        // Clear all
                        IconButton(
                            onClick = { showClearConfirmation = true },
                            enabled = historyList.isNotEmpty(),
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("clear_history_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteSweep,
                                contentDescription = "Clear All History",
                                tint = if (historyList.isNotEmpty()) FlatAlizarin else colors.headerContent.copy(alpha = 0.5f)
                            )
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 12.dp)
        ) {
            // Classic Flat Search Field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .testTag("history_search_input"),
                placeholder = {
                    Text(
                        "Search calculations...",
                        color = colors.mutedText
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = colors.mutedText
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { searchQuery = "" },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear search", tint = colors.mutedText)
                        }
                    }
                },
                shape = RoundedCornerShape(4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = surfaceColor,
                    unfocusedContainerColor = surfaceColor,
                    focusedBorderColor = colors.primaryButton,
                    unfocusedBorderColor = borderColor,
                    focusedTextColor = colors.mainText,
                    unfocusedTextColor = colors.mainText
                ),
                singleLine = true
            )

            if (historyList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.HistoryToggleOff,
                            contentDescription = null,
                            modifier = Modifier.size(54.dp),
                            tint = colors.mutedText.copy(alpha = 0.6f)
                        )
                        Text(
                            text = if (filterFavoritesOnly) "No favorite calculations found" else "No calculation history yet",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = colors.mutedText
                        )
                        Text(
                            text = if (filterFavoritesOnly) "Star items to see them here" else "Results will appear here as you calculate",
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.mutedText.copy(alpha = 0.8f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(historyList, key = { it.id }) { item ->
                        FlatHistoryItemCard(
                            item = item,
                            onReuse = {
                                calculatorViewModel.reuseCalculation(item.expression, item.result)
                                onNavigateBack()
                            },
                            onCopy = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Result", item.result)
                                clipboard.setPrimaryClip(clip)
                                scope.launch {
                                    snackbarHostState.showSnackbar("Copied ${item.result} to clipboard")
                                }
                            },
                            onToggleFavorite = {
                                scope.launch {
                                    repository.setFavorite(item.id, !item.isFavorite)
                                }
                            },
                            onDelete = {
                                scope.launch {
                                    repository.deleteById(item.id)
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Calculation deleted",
                                        actionLabel = "Undo",
                                        duration = SnackbarDuration.Short
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        repository.insert(item.expression, item.result)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    if (showClearConfirmation) {
        AlertDialog(
            onDismissRequest = { showClearConfirmation = false },
            shape = RoundedCornerShape(4.dp),
            containerColor = surfaceColor,
            tonalElevation = 0.dp,
            title = {
                Text(
                    "Clear All History?",
                    fontWeight = FontWeight.Bold,
                    color = colors.mainText
                )
            },
            text = {
                Text(
                    "This will permanently delete all saved calculation records.",
                    color = colors.mutedText
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            repository.clearAll()
                        }
                        showClearConfirmation = false
                    }
                ) {
                    Text("Clear All", color = FlatAlizarin, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmation = false }) {
                    Text("Cancel", color = colors.mainText)
                }
            }
        )
    }
}

/**
 * Classic Flat UI Calculation History Card: 0.dp elevation, 1.dp border, 4.dp corners.
 */
@Composable
private fun FlatHistoryItemCard(
    item: CalculationHistoryEntity,
    onReuse: () -> Unit,
    onCopy: () -> Unit,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit
) {
    val colors = LocalFlatThemeColors.current
    val isDark = colors.isDark
    val surfaceColor = colors.surface
    val borderColor = colors.border

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onReuse),
        color = surfaceColor,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, borderColor),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.expression,
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily.Monospace,
                    color = colors.mutedText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "= ${item.result}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (colors.isCustom) colors.primaryButton else (if (isDark) FlatEmerald else FlatNephritis),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                val dateStr = SimpleDateFormat("MMM d, yyyy • h:mm a", Locale.US).format(Date(item.timestamp))
                Text(
                    text = dateStr,
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.mutedText.copy(alpha = 0.7f)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = if (item.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = "Favorite",
                        tint = if (item.isFavorite) FlatSunFlower else colors.mutedText,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = onCopy,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy",
                        modifier = Modifier.size(18.dp),
                        tint = colors.mutedText
                    )
                }
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = FlatAlizarin,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
