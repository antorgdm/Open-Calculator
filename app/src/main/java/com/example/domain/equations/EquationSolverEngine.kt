package com.example.domain.equations

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.sqrt

sealed class EquationSolution {
    data class LinearResult(val x: Double, val steps: String) : EquationSolution()
    data class QuadraticResult(
        val discriminant: Double,
        val isComplex: Boolean,
        val root1: String,
        val root2: String,
        val vertex: Pair<Double, Double>
    ) : EquationSolution()
    data class System2x2Result(
        val x: Double,
        val y: Double,
        val determinant: Double
    ) : EquationSolution()
    data class Error(val message: String) : EquationSolution()
}

object EquationSolverEngine {
    private val df = DecimalFormat("#.######", DecimalFormatSymbols(Locale.US))

    private fun fmt(n: Double): String {
        val snapped = if (abs(n) < 1e-12) 0.0 else n
        return if (snapped == floor(snapped) && abs(snapped) < 1e14) {
            snapped.toLong().toString()
        } else {
            df.format(snapped)
        }
    }

    fun solveLinear(a: Double, b: Double): EquationSolution {
        if (a == 0.0) {
            return if (b == 0.0) {
                EquationSolution.Error("Infinite solutions (0 = 0)")
            } else {
                EquationSolution.Error("No solution (${fmt(b)} = 0 is impossible)")
            }
        }
        val x = if (abs(-b / a) < 1e-12) 0.0 else -b / a
        val negB = if (abs(-b) < 1e-12) 0.0 else -b
        val steps = "ax + b = 0  ⇒  ${fmt(a)}x = ${fmt(negB)}  ⇒  x = ${fmt(x)}"
        return EquationSolution.LinearResult(x, steps)
    }

    fun solveQuadratic(a: Double, b: Double, c: Double): EquationSolution {
        if (a == 0.0) {
            return solveLinear(b, c)
        }
        val discriminant = b * b - 4 * a * c
        val vertexX = if (abs(-b / (2 * a)) < 1e-12) 0.0 else -b / (2 * a)
        val rawVertexY = a * vertexX * vertexX + b * vertexX + c
        val vertexY = if (abs(rawVertexY) < 1e-12) 0.0 else rawVertexY

        return if (discriminant >= 0.0) {
            val sqrtD = sqrt(discriminant)
            val r1 = if (abs((-b + sqrtD) / (2 * a)) < 1e-12) 0.0 else (-b + sqrtD) / (2 * a)
            val r2 = if (abs((-b - sqrtD) / (2 * a)) < 1e-12) 0.0 else (-b - sqrtD) / (2 * a)
            EquationSolution.QuadraticResult(
                discriminant = discriminant,
                isComplex = false,
                root1 = "x₁ = ${fmt(r1)}",
                root2 = "x₂ = ${fmt(r2)}",
                vertex = Pair(vertexX, vertexY)
            )
        } else {
            val realPart = if (abs(-b / (2 * a)) < 1e-12) 0.0 else -b / (2 * a)
            val imagPart = sqrt(abs(discriminant)) / (2 * a)
            EquationSolution.QuadraticResult(
                discriminant = discriminant,
                isComplex = true,
                root1 = "x₁ = ${fmt(realPart)} + ${fmt(abs(imagPart))}i",
                root2 = "x₂ = ${fmt(realPart)} - ${fmt(abs(imagPart))}i",
                vertex = Pair(vertexX, vertexY)
            )
        }
    }

    fun solveSystem2x2(
        a1: Double, b1: Double, c1: Double,
        a2: Double, b2: Double, c2: Double
    ): EquationSolution {
        val det = a1 * b2 - a2 * b1
        if (abs(det) < 1e-12) {
            return EquationSolution.Error("No unique solution (Determinant = 0, parallel or coincident lines)")
        }
        val detX = c1 * b2 - c2 * b1
        val detY = a1 * c2 - a2 * c1
        val x = if (abs(detX / det) < 1e-12) 0.0 else detX / det
        val y = if (abs(detY / det) < 1e-12) 0.0 else detY / det
        return EquationSolution.System2x2Result(x, y, det)
    }
}
