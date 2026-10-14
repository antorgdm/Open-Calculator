package com.example

import com.example.data.preferences.AngleMode
import com.example.domain.evaluator.CalculationResult
import com.example.domain.evaluator.CalculatorEngine
import com.example.domain.finance.PercentageEngine
import com.example.domain.geometry.GeometryEngine
import com.example.domain.programmer.BitSize
import com.example.domain.programmer.NumberBase
import com.example.domain.programmer.ProgrammerEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.math.PI
import kotlin.math.abs

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class CalculatorEngineRobolectricTest {

    private fun eval(expr: String, angleMode: AngleMode = AngleMode.DEGREE): CalculationResult =
        CalculatorEngine.evaluate(expr, angleMode)

    @Test
    fun testUnaryMinusAndPower() {
        // 2^-2 must evaluate to 0.25 without popping ^ prematurely
        val res = eval("2^-2")
        assertTrue(res is CalculationResult.Success)
        assertEquals(0.25, (res as CalculationResult.Success).value, 1e-9)

        // --3 must evaluate to 3
        val res2 = eval("--3")
        assertTrue(res2 is CalculationResult.Success)
        assertEquals(3.0, (res2 as CalculationResult.Success).value, 1e-9)
    }

    @Test
    fun testTrigAndNoiseSnapping() {
        // sin(180) in DEGREE mode must snap to 0.0
        val sin180 = eval("sin(180)")
        assertTrue(sin180 is CalculationResult.Success)
        assertEquals(0.0, (sin180 as CalculationResult.Success).value, 1e-9)
        assertEquals("0", (sin180 as CalculationResult.Success).formatted)

        // cos(90) in DEGREE mode must snap to 0.0
        val cos90 = eval("cos(90)")
        assertTrue(cos90 is CalculationResult.Success)
        assertEquals(0.0, (cos90 as CalculationResult.Success).value, 1e-9)
        assertEquals("0", (cos90 as CalculationResult.Success).formatted)

        // sin(-30) in DEGREE mode
        val sinNeg30 = eval("sin(-30)")
        assertTrue(sinNeg30 is CalculationResult.Success)
        assertEquals(-0.5, (sinNeg30 as CalculationResult.Success).value, 1e-9)
    }

    @Test
    fun testImplicitMultiplication() {
        // 2π
        val res2Pi = eval("2π")
        assertTrue(res2Pi is CalculationResult.Success)
        assertEquals(2 * PI, (res2Pi as CalculationResult.Success).value, 1e-9)

        // 2(3)
        val res2x3 = eval("2(3)")
        assertTrue(res2x3 is CalculationResult.Success)
        assertEquals(6.0, (res2x3 as CalculationResult.Success).value, 1e-9)

        // (2+3)(4)
        val res5x4 = eval("(2+3)(4)")
        assertTrue(res5x4 is CalculationResult.Success)
        assertEquals(20.0, (res5x4 as CalculationResult.Success).value, 1e-9)

        // 9√(4)
        val res9Sqrt4 = eval("9√(4)")
        assertTrue(res9Sqrt4 is CalculationResult.Success)
        assertEquals(18.0, (res9Sqrt4 as CalculationResult.Success).value, 1e-9)

        // 2e
        val res2e = eval("2e")
        assertTrue(res2e is CalculationResult.Success)
        assertEquals(2 * Math.E, (res2e as CalculationResult.Success).value, 1e-9)
    }

    @Test
    fun testPercentageOperations() {
        // 50%
        val res50Pct = eval("50%")
        assertTrue(res50Pct is CalculationResult.Success)
        assertEquals(0.5, (res50Pct as CalculationResult.Success).value, 1e-9)

        // 200 + 10%
        val res200Plus10Pct = eval("200+10%")
        assertTrue(res200Plus10Pct is CalculationResult.Success)
        assertEquals(220.0, (res200Plus10Pct as CalculationResult.Success).value, 1e-9)

        // 200 - 10%
        val res200Minus10Pct = eval("200-10%")
        assertTrue(res200Minus10Pct is CalculationResult.Success)
        assertEquals(180.0, (res200Minus10Pct as CalculationResult.Success).value, 1e-9)
    }

    @Test
    fun testMismatchedParentheses() {
        // 2+3) must fail with error, not silently ignore )
        val res = eval("2+3)")
        assertTrue(res is CalculationResult.Error)

        // (2+3
        val res2 = eval("(2+3")
        assertTrue(res2 is CalculationResult.Error)
    }

    @Test
    fun testScientificFormattingTrimmed() {
        val formatted = CalculatorEngine.formatResult(1e15)
        assertEquals("1E+15", formatted)
    }

    @Test
    fun testProgrammerShiftsAndNegatives() {
        // Shift >= 64 bits must be 0
        val shifted = ProgrammerEngine.shiftLeft(1uL, 64, BitSize.QWORD)
        assertEquals(0uL, shifted)

        // Parse negative in DEC
        val parsedNeg = ProgrammerEngine.parseInput("-5", NumberBase.DEC, BitSize.QWORD)
        assertEquals((-5L).toULong(), parsedNeg)
    }

    @Test
    fun testPercentageChangeFromZero() {
        val change = PercentageEngine.percentChange(0.0, 50.0)
        assertTrue(change.isUndefined)
    }

    @Test
    fun testGeometryTriangleValidation() {
        // Violating triangle inequality: 1 + 2 <= 5
        var failed = false
        try {
            GeometryEngine.calculateTriangle(base = 5.0, height = 3.0, sideA = 1.0, sideB = 2.0)
        } catch (_: IllegalArgumentException) {
            failed = true
        }
        assertTrue(failed)
    }
}
