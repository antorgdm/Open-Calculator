package com.example.ui.calculator

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.database.CalculationHistoryEntity
import com.example.data.preferences.AngleMode
import com.example.data.preferences.AppPreferences
import com.example.domain.evaluator.CalculationResult
import com.example.domain.evaluator.CalculatorEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CalculatorUiState(
    val expression: String = "",
    val previewResult: String = "",
    val finalResult: String = "",
    val isScientificExpanded: Boolean = false,
    val angleMode: AngleMode = AngleMode.DEGREE,
    val memory: Double = 0.0,
    val hasMemory: Boolean = false,
    val hasAns: Boolean = false,
    val variables: Map<String, Double> = mapOf("X" to 0.0, "Y" to 0.0, "Z" to 0.0, "Ans" to 0.0),
    val errorMessage: String? = null
)

class CalculatorViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val historyDao = database.calculationHistoryDao()
    private val preferences = AppPreferences.getInstance(application)

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferences.angleMode.collect { mode ->
                _uiState.value = _uiState.value.copy(angleMode = mode)
                updatePreview()
            }
        }
        viewModelScope.launch {
            preferences.decimalPrecision.collect { _ ->
                updatePreview()
            }
        }
    }

    fun onAppend(char: String) {
        val state = _uiState.value
        val current = state.expression

        // If typing a digit/symbol right after a final result was evaluated, start a new expression
        val updated = if (state.finalResult.isNotEmpty()) {
            if (char == ".") "0." else char
        } else {
            // Prevent multiple decimals in the same number segment
            if (char == ".") {
                val lastSegment = current.takeLastWhile { it.isDigit() || it == '.' }
                if (lastSegment.contains('.')) {
                    return
                }
                if (current.isEmpty() || isOperatorChar(current.last()) || current.last() == '(') {
                    current + "0."
                } else {
                    current + "."
                }
            } else {
                current + char
            }
        }

        _uiState.value = state.copy(
            expression = updated,
            errorMessage = null,
            finalResult = ""
        )
        updatePreview()
    }

    fun onOperator(op: String) {
        val state = _uiState.value
        val current = state.expression

        // Chaining from a final result: e.g. 2+3=5, then typing ×2 becomes 5×2
        val updated = if (state.finalResult.isNotEmpty()) {
            state.finalResult + op
        } else if (current.isNotEmpty() && isOperatorChar(current.last())) {
            // Operator replacement: replace previous operator with new one
            current.dropLast(1) + op
        } else if (current.isEmpty()) {
            if (op == "−" || op == "-") {
                "-"
            } else {
                return
            }
        } else {
            current + op
        }

        _uiState.value = state.copy(
            expression = updated,
            errorMessage = null,
            finalResult = ""
        )
        updatePreview()
    }

    private fun isOperatorChar(c: Char): Boolean =
        c in listOf('+', '-', '×', '*', '÷', '/', '^')

    fun onBackspace() {
        val current = _uiState.value.expression
        if (current.isNotEmpty()) {
            // Check for multi-character function tokens to delete atomically in 1 press
            val multiCharTokens = listOf(
                "sin⁻¹(", "cos⁻¹(", "tan⁻¹(",
                "sinh(", "cosh(", "tanh(",
                "sqrt(", "cbrt(",
                "sin(", "cos(", "tan(",
                "log(", "ln(", "abs(",
                "1/(", "√(", "∛(",
                "Ans", "PI", "(-"
            )

            var dropped = false
            var updated = current
            for (token in multiCharTokens) {
                if (current.endsWith(token)) {
                    updated = current.dropLast(token.length)
                    dropped = true
                    break
                }
            }
            if (!dropped) {
                updated = current.dropLast(1)
            }

            _uiState.value = _uiState.value.copy(
                expression = updated,
                errorMessage = null,
                finalResult = ""
            )
            updatePreview()
        }
    }

    fun onClear() {
        _uiState.value = _uiState.value.copy(
            expression = "",
            previewResult = "",
            finalResult = "",
            errorMessage = null
        )
    }

    fun onTogglePlusMinus() {
        val state = _uiState.value
        if (state.finalResult.isNotEmpty()) {
            val res = state.finalResult
            val toggled = if (res.startsWith("-") || res.startsWith("−")) {
                res.drop(1)
            } else {
                "-$res"
            }
            _uiState.value = state.copy(
                expression = toggled,
                finalResult = "",
                errorMessage = null
            )
            updatePreview()
            return
        }

        val current = state.expression
        if (current.isEmpty()) return

        // If simple signed number
        val isSimpleNumber = current.toDoubleOrNull() != null
        val updated = if (isSimpleNumber) {
            if (current.startsWith("-")) current.removePrefix("-") else "-$current"
        } else if (current.startsWith("-(") && current.endsWith(")")) {
            current.removeSurrounding("-(", ")")
        } else {
            "-($current)"
        }

        _uiState.value = state.copy(expression = updated, finalResult = "")
        updatePreview()
    }

    fun onParenthesis() {
        val current = _uiState.value.expression
        val openCount = current.count { it == '(' }
        val closeCount = current.count { it == ')' }

        val canClose = openCount > closeCount && current.isNotEmpty() && (
                current.last().isDigit() || current.last() in listOf(')', 'π', '!', 'e', 'E', 's') // 's' for Ans
                )

        val toAdd = if (canClose) ")" else "("
        onAppend(toAdd)
    }

    fun onScientificToggle() {
        _uiState.value = _uiState.value.copy(
            isScientificExpanded = !_uiState.value.isScientificExpanded
        )
    }

    fun onToggleAngleMode() {
        val newMode = if (_uiState.value.angleMode == AngleMode.DEGREE) {
            AngleMode.RADIAN
        } else {
            AngleMode.DEGREE
        }
        preferences.setAngleMode(newMode)
    }

    fun onReciprocal() {
        val state = _uiState.value
        val target = if (state.finalResult.isNotEmpty()) {
            state.finalResult
        } else if (state.expression.isNotEmpty()) {
            state.expression
        } else {
            ""
        }

        if (target.isEmpty()) {
            onAppend("1/(")
        } else {
            _uiState.value = state.copy(
                expression = "1/($target)",
                finalResult = "",
                errorMessage = null
            )
            updatePreview()
        }
    }

    fun onAns() {
        onAppend("Ans")
    }

    fun onEquals() {
        val expr = _uiState.value.expression
        if (expr.isBlank()) return

        val precision = preferences.decimalPrecision.value
        when (val result = CalculatorEngine.evaluate(
            expr,
            _uiState.value.angleMode,
            precision,
            _uiState.value.variables
        )) {
            is CalculationResult.Success -> {
                val formatted = result.formatted
                _uiState.value = _uiState.value.copy(
                    finalResult = formatted,
                    previewResult = "",
                    errorMessage = null,
                    hasAns = true,
                    variables = _uiState.value.variables + ("Ans" to result.value)
                )

                // Save to history
                viewModelScope.launch {
                    historyDao.insert(
                        CalculationHistoryEntity(
                            expression = expr,
                            result = formatted,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }
            }
            is CalculationResult.Error -> {
                _uiState.value = _uiState.value.copy(
                    errorMessage = result.message
                )
            }
        }
    }

    private fun updatePreview() {
        val expr = _uiState.value.expression
        if (expr.length < 2) {
            _uiState.value = _uiState.value.copy(previewResult = "")
            return
        }

        val precision = preferences.decimalPrecision.value
        when (val result = CalculatorEngine.evaluate(
            expr,
            _uiState.value.angleMode,
            precision,
            _uiState.value.variables
        )) {
            is CalculationResult.Success -> {
                _uiState.value = _uiState.value.copy(previewResult = result.formatted)
            }
            is CalculationResult.Error -> {
                _uiState.value = _uiState.value.copy(previewResult = "")
            }
        }
    }

    // Memory operations
    fun onMemoryAdd() {
        val value = getCurrentNumber() ?: return
        val newMem = _uiState.value.memory + value
        _uiState.value = _uiState.value.copy(memory = newMem, hasMemory = true)
    }

    fun onMemorySub() {
        val value = getCurrentNumber() ?: return
        val newMem = _uiState.value.memory - value
        _uiState.value = _uiState.value.copy(memory = newMem, hasMemory = true)
    }

    fun onMemoryRecall() {
        if (_uiState.value.hasMemory) {
            val precision = preferences.decimalPrecision.value
            val formatted = CalculatorEngine.formatResult(_uiState.value.memory, precision)
            val toAppend = if (_uiState.value.memory < 0 && _uiState.value.expression.isNotEmpty()) {
                "($formatted)"
            } else {
                formatted
            }
            onAppend(toAppend)
        }
    }

    fun onMemoryClear() {
        _uiState.value = _uiState.value.copy(memory = 0.0, hasMemory = false)
    }

    fun onMemoryStore() {
        val value = getCurrentNumber() ?: return
        _uiState.value = _uiState.value.copy(memory = value, hasMemory = true)
    }

    fun storeVariable(name: String) {
        val value = getCurrentNumber() ?: return
        _uiState.value = _uiState.value.copy(
            variables = _uiState.value.variables + (name to value)
        )
    }

    fun recallVariable(name: String) {
        val value = _uiState.value.variables[name] ?: return
        val precision = preferences.decimalPrecision.value
        onAppend(CalculatorEngine.formatResult(value, precision))
    }

    private fun getCurrentNumber(): Double? {
        val finalRes = _uiState.value.finalResult.toDoubleOrNull()
        if (finalRes != null) return finalRes
        val preview = _uiState.value.previewResult.toDoubleOrNull()
        if (preview != null) return preview
        val expr = _uiState.value.expression
        return expr.toDoubleOrNull()
    }

    fun copyToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Calculator Result", text)
        clipboard.setPrimaryClip(clip)
    }

    fun pasteFromClipboard(context: Context) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = clipboard.primaryClip
        if (clip != null && clip.itemCount > 0) {
            val text = clip.getItemAt(0).text?.toString() ?: ""
            if (text.isNotBlank()) {
                onAppend(text)
            }
        }
    }

    fun reuseCalculation(expression: String, result: String) {
        _uiState.value = _uiState.value.copy(
            expression = expression,
            finalResult = result,
            previewResult = "",
            errorMessage = null
        )
    }
}
