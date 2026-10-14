package com.example.domain.evaluator

import com.example.data.preferences.AngleMode
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale
import kotlin.math.*

sealed class CalculationResult {
    data class Success(val value: Double, val formatted: String) : CalculationResult()
    data class Error(val message: String) : CalculationResult()
}

object CalculatorEngine {

    fun evaluate(
        expression: String,
        angleMode: AngleMode,
        precision: Int = 8,
        variables: Map<String, Double> = emptyMap()
    ): CalculationResult {
        if (expression.isBlank()) return CalculationResult.Error("Empty expression")

        try {
            val sanitized = sanitize(expression)
            val tokens = tokenize(sanitized)
            val tokensWithImplicitMult = insertImplicitMultiplication(tokens)
            val rpn = shuntingYard(tokensWithImplicitMult)
            val result = evaluateRpn(rpn, angleMode, variables)

            if (result.isNaN()) return CalculationResult.Error("Undefined result")
            if (result.isInfinite()) return CalculationResult.Error("Overflow / Division by zero")

            val snappedResult = if (abs(result) < 1e-14) 0.0 else result
            val formatted = formatResult(snappedResult, precision)
            return CalculationResult.Success(snappedResult, formatted)
        } catch (e: ArithmeticException) {
            return CalculationResult.Error(e.message ?: "Math error")
        } catch (e: IllegalArgumentException) {
            return CalculationResult.Error(e.message ?: "Invalid syntax")
        } catch (_: Exception) {
            return CalculationResult.Error("Invalid syntax")
        }
    }

    private fun sanitize(expr: String): String {
        return expr
            .replace("×", "*")
            .replace("÷", "/")
            .replace("−", "-")
            .replace("π", "PI")
            .replace("sin⁻¹", "asin")
            .replace("cos⁻¹", "acos")
            .replace("tan⁻¹", "atan")
            .replace("√", "sqrt")
            .replace("∛", "cbrt")
    }

    private fun tokenize(expr: String): List<String> {
        val tokens = mutableListOf<String>()
        var i = 0
        val n = expr.length

        while (i < n) {
            val c = expr[i]

            if (c.isWhitespace()) {
                i++
                continue
            }

            // Number or decimal
            if (c.isDigit() || (c == '.' && i + 1 < n && expr[i + 1].isDigit())) {
                val sb = StringBuilder()
                while (i < n && (expr[i].isDigit() || expr[i] == '.')) {
                    sb.append(expr[i])
                    i++
                }
                // Check for scientific notation exponent like 1e5, 2.5E-3
                if (i < n && (expr[i] == 'E' || expr[i] == 'e')) {
                    val nextIdx = i + 1
                    val hasSign = nextIdx < n && (expr[nextIdx] == '+' || expr[nextIdx] == '-')
                    val digitIdx = if (hasSign) nextIdx + 1 else nextIdx
                    if (digitIdx < n && expr[digitIdx].isDigit()) {
                        sb.append(expr[i]) // E
                        i++
                        if (hasSign) {
                            sb.append(expr[i])
                            i++
                        }
                        while (i < n && expr[i].isDigit()) {
                            sb.append(expr[i])
                            i++
                        }
                    }
                }
                tokens.add(sb.toString())
                continue
            }

            // Word tokens (functions and constants: e, PI, Ans, sin, etc.)
            if (c.isLetter()) {
                val sb = StringBuilder()
                while (i < n && (expr[i].isLetter() || expr[i].isDigit())) {
                    sb.append(expr[i])
                    i++
                }
                val word = sb.toString()
                if (word.equals("e", ignoreCase = true)) {
                    tokens.add("EULER")
                } else {
                    tokens.add(word)
                }
                continue
            }

            // Operators and punctuation
            when (c) {
                '+', '-', '*', '/', '%', '^', '!', '(', ')' -> {
                    // Check for unary minus: at start, after binary operator, after left paren, after another unary minus, or after a function name
                    if (c == '-') {
                        val isUnary = tokens.isEmpty() ||
                                tokens.last() in listOf("+", "-", "*", "/", "(", "^", "%", "neg") ||
                                isFunction(tokens.last())
                        if (isUnary) {
                            tokens.add("neg")
                            i++
                            continue
                        }
                    }
                    tokens.add(c.toString())
                    i++
                }
                else -> {
                    throw IllegalArgumentException("Invalid character: $c")
                }
            }
        }
        return tokens
    }

    private fun insertImplicitMultiplication(tokens: List<String>): List<String> {
        if (tokens.size < 2) return tokens
        val result = mutableListOf<String>()

        fun canEndOperand(t: String): Boolean =
            t.toDoubleOrNull() != null || t == "PI" || t == "EULER" || t == "Ans" ||
                    t == ")" || t == "!" || t == "%"

        fun canStartOperand(t: String): Boolean =
            t.toDoubleOrNull() != null || t == "PI" || t == "EULER" || t == "Ans" ||
                    t == "(" || isFunction(t)

        for (i in tokens.indices) {
            val curr = tokens[i]
            if (i > 0) {
                val prev = tokens[i - 1]
                if (canEndOperand(prev) && canStartOperand(curr)) {
                    result.add("*")
                }
            }
            result.add(curr)
        }
        return result
    }

    private fun shuntingYard(tokens: List<String>): List<String> {
        val output = mutableListOf<String>()
        val stack = ArrayDeque<String>()

        fun precedence(op: String): Int = when (op) {
            "+", "-" -> 1
            "*", "/" -> 2
            "%" -> 3
            "^" -> 4
            "neg" -> 5
            "!" -> 6
            else -> 0
        }

        fun isRightAssociative(op: String): Boolean = op == "^" || op == "neg"

        for (token in tokens) {
            when {
                token.toDoubleOrNull() != null || token == "PI" || token == "EULER" || token == "Ans" -> {
                    output.add(token)
                }
                isFunction(token) -> {
                    stack.addLast(token)
                }
                token == "(" -> {
                    stack.addLast(token)
                }
                token == ")" -> {
                    var foundLeft = false
                    while (stack.isNotEmpty()) {
                        val top = stack.removeLast()
                        if (top == "(") {
                            foundLeft = true
                            break
                        }
                        output.add(top)
                    }
                    if (!foundLeft) {
                        throw IllegalArgumentException("Mismatched parentheses")
                    }
                    if (stack.isNotEmpty() && isFunction(stack.last())) {
                        output.add(stack.removeLast())
                    }
                }
                isOperator(token) -> {
                    while (stack.isNotEmpty() && isOperator(stack.last())) {
                        val top = stack.last()
                        val pCurrent = precedence(token)
                        val pTop = precedence(top)
                        if ((!isRightAssociative(token) && pCurrent <= pTop) ||
                            (isRightAssociative(token) && pCurrent < pTop)
                        ) {
                            output.add(stack.removeLast())
                        } else {
                            break
                        }
                    }
                    stack.addLast(token)
                }
                else -> {
                    throw IllegalArgumentException("Unknown token: $token")
                }
            }
        }

        while (stack.isNotEmpty()) {
            val top = stack.removeLast()
            if (top == "(" || top == ")") {
                throw IllegalArgumentException("Mismatched parentheses")
            }
            output.add(top)
        }

        return output
    }

    private fun isOperator(token: String): Boolean =
        token in listOf("+", "-", "*", "/", "%", "^", "!", "neg")

    private fun isFunction(token: String): Boolean =
        token in listOf(
            "sin", "cos", "tan", "asin", "acos", "atan",
            "sinh", "cosh", "tanh", "log", "ln", "sqrt", "cbrt", "abs"
        )

    private fun evaluateRpn(
        rpn: List<String>,
        angleMode: AngleMode,
        variables: Map<String, Double>
    ): Double {
        val stack = ArrayDeque<Double>()

        for (i in rpn.indices) {
            val token = rpn[i]
            when {
                token == "PI" -> stack.addLast(Math.PI)
                token == "EULER" -> stack.addLast(Math.E)
                token == "Ans" -> stack.addLast(variables["Ans"] ?: 0.0)
                token.toDoubleOrNull() != null -> stack.addLast(token.toDouble())
                token == "neg" -> {
                    val a = stack.removeLastOrNull() ?: throw IllegalArgumentException("Missing operand")
                    stack.addLast(-a)
                }
                token == "!" -> {
                    val a = stack.removeLastOrNull() ?: throw IllegalArgumentException("Missing operand")
                    stack.addLast(factorial(a))
                }
                token == "%" -> {
                    // Postfix percent operator
                    val b = stack.removeLastOrNull() ?: throw IllegalArgumentException("Missing operand")
                    // Check if next token in RPN is + or - with an existing base value on stack
                    val nextOp = if (i + 1 < rpn.size) rpn[i + 1] else null
                    if (stack.isNotEmpty() && (nextOp == "+" || nextOp == "-")) {
                        val base = stack.last()
                        stack.addLast(base * (b / 100.0))
                    } else {
                        stack.addLast(b / 100.0)
                    }
                }
                token in listOf("+", "-", "*", "/", "^") -> {
                    val b = stack.removeLastOrNull() ?: throw IllegalArgumentException("Missing operand")
                    val a = stack.removeLastOrNull() ?: throw IllegalArgumentException("Missing operand")
                    val res = when (token) {
                        "+" -> a + b
                        "-" -> a - b
                        "*" -> a * b
                        "/" -> {
                            if (b == 0.0) throw ArithmeticException("Division by zero")
                            a / b
                        }
                        "^" -> a.pow(b)
                        else -> throw IllegalArgumentException("Invalid operator")
                    }
                    stack.addLast(res)
                }
                isFunction(token) -> {
                    val a = stack.removeLastOrNull() ?: throw IllegalArgumentException("Missing operand")
                    val res = evaluateFunction(token, a, angleMode)
                    stack.addLast(res)
                }
                else -> throw IllegalArgumentException("Unknown token: $token")
            }
        }

        return stack.singleOrNull() ?: throw IllegalArgumentException("Malformed expression")
    }

    private fun evaluateFunction(func: String, a: Double, angleMode: AngleMode): Double {
        return when (func) {
            "sin" -> {
                if (angleMode == AngleMode.DEGREE) {
                    val norm = ((a % 360.0) + 360.0) % 360.0
                    when {
                        abs(norm - 0.0) < 1e-9 || abs(norm - 180.0) < 1e-9 || abs(norm - 360.0) < 1e-9 -> 0.0
                        abs(norm - 90.0) < 1e-9 -> 1.0
                        abs(norm - 270.0) < 1e-9 -> -1.0
                        else -> sin(Math.toRadians(a))
                    }
                } else {
                    val s = sin(a)
                    if (abs(s) < 1e-14) 0.0 else s
                }
            }
            "cos" -> {
                if (angleMode == AngleMode.DEGREE) {
                    val norm = ((a % 360.0) + 360.0) % 360.0
                    when {
                        abs(norm - 90.0) < 1e-9 || abs(norm - 270.0) < 1e-9 -> 0.0
                        abs(norm - 0.0) < 1e-9 || abs(norm - 360.0) < 1e-9 -> 1.0
                        abs(norm - 180.0) < 1e-9 -> -1.0
                        else -> cos(Math.toRadians(a))
                    }
                } else {
                    val c = cos(a)
                    if (abs(c) < 1e-14) 0.0 else c
                }
            }
            "tan" -> {
                if (angleMode == AngleMode.DEGREE) {
                    val norm = ((a % 360.0) + 360.0) % 360.0
                    when {
                        abs(norm - 90.0) < 1e-9 || abs(norm - 270.0) < 1e-9 -> throw ArithmeticException("Undefined (tan 90°)")
                        abs(norm - 0.0) < 1e-9 || abs(norm - 180.0) < 1e-9 || abs(norm - 360.0) < 1e-9 -> 0.0
                        abs(norm - 45.0) < 1e-9 || abs(norm - 225.0) < 1e-9 -> 1.0
                        abs(norm - 135.0) < 1e-9 || abs(norm - 315.0) < 1e-9 -> -1.0
                        else -> tan(Math.toRadians(a))
                    }
                } else {
                    val t = tan(a)
                    if (abs(t) < 1e-14) 0.0 else t
                }
            }
            "asin" -> {
                if (a < -1.0 || a > 1.0) throw ArithmeticException("Domain error: asin [-1, 1]")
                val rad = asin(a)
                if (angleMode == AngleMode.DEGREE) Math.toDegrees(rad) else rad
            }
            "acos" -> {
                if (a < -1.0 || a > 1.0) throw ArithmeticException("Domain error: acos [-1, 1]")
                val rad = acos(a)
                if (angleMode == AngleMode.DEGREE) Math.toDegrees(rad) else rad
            }
            "atan" -> {
                val rad = atan(a)
                if (angleMode == AngleMode.DEGREE) Math.toDegrees(rad) else rad
            }
            "sinh" -> sinh(a)
            "cosh" -> cosh(a)
            "tanh" -> tanh(a)
            "log" -> {
                if (a <= 0.0) throw ArithmeticException("Domain error: log <= 0")
                log10(a)
            }
            "ln" -> {
                if (a <= 0.0) throw ArithmeticException("Domain error: ln <= 0")
                ln(a)
            }
            "sqrt" -> {
                if (a < 0.0) throw ArithmeticException("Domain error: sqrt < 0")
                sqrt(a)
            }
            "cbrt" -> cbrt(a)
            "abs" -> abs(a)
            else -> throw IllegalArgumentException("Unknown function: $func")
        }
    }

    private fun factorial(n: Double): Double {
        if (n < 0 || n != floor(n)) throw ArithmeticException("Factorial requires non-negative integer")
        if (n > 170) throw ArithmeticException("Factorial overflow")
        val intN = n.toInt()
        var result = 1.0
        for (i in 2..intN) {
            result *= i
        }
        return result
    }

    fun formatResult(value: Double, precision: Int = 8): String {
        if (value.isNaN()) return "NaN"
        if (value.isInfinite()) return if (value > 0) "Infinity" else "-Infinity"

        val effectiveValue = if (abs(value) < 1e-14) 0.0 else value

        // Handle exact integers within safe representation range
        if (effectiveValue == floor(effectiveValue) && abs(effectiveValue) < 1e14) {
            return effectiveValue.toLong().toString()
        }

        // Handle very small or very large magnitudes with scientific notation, trimming trailing zeros
        if (abs(effectiveValue) >= 1e12 || (abs(effectiveValue) > 0 && abs(effectiveValue) < 1e-6)) {
            val raw = String.format(Locale.US, "%.${min(precision, 8)}E", effectiveValue)
            val parts = raw.split("E")
            if (parts.size == 2) {
                val significand = parts[0].trimEnd('0').trimEnd('.')
                return "${significand}E${parts[1]}"
            }
            return raw
        }

        val rounded = BigDecimal(effectiveValue).setScale(precision, RoundingMode.HALF_UP).stripTrailingZeros()
        return rounded.toPlainString()
    }
}
