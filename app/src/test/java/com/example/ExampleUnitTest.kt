package com.example

import com.example.data.preferences.AngleMode
import com.example.domain.converters.UnitCategory
import com.example.domain.converters.UnitConverterEngine
import com.example.domain.datetime.DateTimeEngine
import com.example.domain.equations.EquationSolution
import com.example.domain.equations.EquationSolverEngine
import com.example.domain.evaluator.CalculationResult
import com.example.domain.evaluator.CalculatorEngine
import com.example.domain.finance.FinancialEngine
import com.example.domain.finance.PercentageEngine
import com.example.domain.geometry.GeometryEngine
import com.example.domain.programmer.BitSize
import com.example.domain.programmer.NumberBase
import com.example.domain.programmer.ProgrammerEngine
import com.example.domain.statistics.StatisticsEngine
import org.junit.Assert.*
import org.junit.Test
import java.util.Calendar

class ExampleUnitTest {

    // --- Phase 1: Core Calculator Tests ---
    @Test
    fun testPhase1_BasicArithmeticAndPrecedence() {
        val res1 = CalculatorEngine.evaluate("250*15", AngleMode.DEGREE)
        assertTrue(res1 is CalculationResult.Success)
        assertEquals("3750", (res1 as CalculationResult.Success).formatted)

        val res2 = CalculatorEngine.evaluate("100 + 50 / 2", AngleMode.DEGREE)
        assertTrue(res2 is CalculationResult.Success)
        assertEquals("125", (res2 as CalculationResult.Success).formatted)

        val resBrackets = CalculatorEngine.evaluate("(10 + 5) * 4", AngleMode.DEGREE)
        assertTrue(resBrackets is CalculationResult.Success)
        assertEquals("60", (resBrackets as CalculationResult.Success).formatted)

        val resNegative = CalculatorEngine.evaluate("-5 * 6", AngleMode.DEGREE)
        assertTrue(resNegative is CalculationResult.Success)
        assertEquals("-30", (resNegative as CalculationResult.Success).formatted)
    }

    @Test
    fun testPhase1_DivisionByZeroAndInvalidExpressions() {
        val divZero = CalculatorEngine.evaluate("10 / 0", AngleMode.DEGREE)
        assertTrue(divZero is CalculationResult.Error)
        assertEquals("Division by zero", (divZero as CalculationResult.Error).message)

        val invalid = CalculatorEngine.evaluate("5 ++ * 2", AngleMode.DEGREE)
        assertTrue(invalid is CalculationResult.Error)
    }

    // --- Phase 2: Scientific Calculator Tests ---
    @Test
    fun testPhase2_TrigonometricAndPowers() {
        val sin90 = CalculatorEngine.evaluate("sin(90)", AngleMode.DEGREE)
        assertTrue(sin90 is CalculationResult.Success)
        assertEquals("1", (sin90 as CalculationResult.Success).formatted)

        val cos0 = CalculatorEngine.evaluate("cos(0)", AngleMode.DEGREE)
        assertTrue(cos0 is CalculationResult.Success)
        assertEquals("1", (cos0 as CalculationResult.Success).formatted)

        val sqrt16 = CalculatorEngine.evaluate("sqrt(16)", AngleMode.DEGREE)
        assertTrue(sqrt16 is CalculationResult.Success)
        assertEquals("4", (sqrt16 as CalculationResult.Success).formatted)

        val power = CalculatorEngine.evaluate("2^8", AngleMode.DEGREE)
        assertTrue(power is CalculationResult.Success)
        assertEquals("256", (power as CalculationResult.Success).formatted)

        val fact5 = CalculatorEngine.evaluate("5!", AngleMode.DEGREE)
        assertTrue(fact5 is CalculationResult.Success)
        assertEquals("120", (fact5 as CalculationResult.Success).formatted)
    }

    // --- Phase 3: Tools (Units, Percentage, Dates, Geometry) ---
    @Test
    fun testPhase3_UnitConverter() {
        val units = UnitConverterEngine.getUnitsForCategory(UnitCategory.LENGTH)
        val km = units.first { it.id == "km" }
        val m = units.first { it.id == "m" }
        val meters = UnitConverterEngine.convert(2.5, km, m, UnitCategory.LENGTH)
        assertEquals(2500.0, meters, 0.001)

        val tempUnits = UnitConverterEngine.getUnitsForCategory(UnitCategory.TEMPERATURE)
        val c = tempUnits.first { it.id == "c" }
        val f = tempUnits.first { it.id == "f" }
        val fahrenheit = UnitConverterEngine.convert(100.0, c, f, UnitCategory.TEMPERATURE)
        assertEquals(212.0, fahrenheit, 0.001)
    }

    @Test
    fun testPhase3_PercentageTools() {
        assertEquals(37.5, PercentageEngine.percentOf(15.0, 250.0), 0.001)
        val disc = PercentageEngine.discount(100.0, 25.0)
        assertEquals(75.0, disc.finalPrice, 0.001)
        assertEquals(25.0, disc.savings, 0.001)

        val change = PercentageEngine.percentChange(50.0, 75.0)
        assertTrue(change.isIncrease)
        assertEquals(50.0, change.percentChange, 0.001)
    }

    @Test
    fun testPhase3_DateEngineLeapYear() {
        val cal1 = Calendar.getInstance().apply { set(2024, Calendar.FEBRUARY, 28) }
        val cal2 = Calendar.getInstance().apply { set(2024, Calendar.MARCH, 1) }
        val diff = DateTimeEngine.calculateDateDiff(cal1, cal2)
        assertEquals(2L, diff.totalDays) // 2024 was leap year: Feb 28 to Feb 29 to Mar 1
    }

    @Test
    fun testPhase3_Geometry() {
        val sq = GeometryEngine.calculateSquare(5.0)
        assertEquals(25.0, sq.primaryValue, 0.001)
        assertEquals(20.0, sq.secondaryValue, 0.001)

        val circle = GeometryEngine.calculateCircle(7.0)
        assertEquals(Math.PI * 49.0, circle.primaryValue, 0.001)
    }

    // --- Phase 4: Finance, Statistics, Programmer & Equations ---
    @Test
    fun testPhase4_FinancialEngine() {
        val si = FinancialEngine.simpleInterest(1000.0, 5.0, 2.0)
        assertEquals(100.0, si.interest, 0.001)
        assertEquals(1100.0, si.totalAmount, 0.001)

        val emi = FinancialEngine.loanEmi(120000.0, 10.0, 12)
        assertTrue(emi.monthlyEmi > 10000.0)
    }

    @Test
    fun testPhase4_StatisticsEngine() {
        val data = listOf(10.0, 20.0, 30.0, 40.0, 50.0)
        val stats = StatisticsEngine.compute(data)
        assertNotNull(stats)
        assertEquals(5, stats?.count)
        assertEquals(30.0, stats?.mean ?: 0.0, 0.001)
        assertEquals(30.0, stats?.median ?: 0.0, 0.001)
        assertEquals(10.0, stats?.min ?: 0.0, 0.001)
        assertEquals(50.0, stats?.max ?: 0.0, 0.001)
    }

    @Test
    fun testPhase4_ProgrammerEngine() {
        val hexStr = ProgrammerEngine.formatInBase(255uL, NumberBase.HEX, BitSize.BYTE)
        assertEquals("FF", hexStr)

        val binStr = ProgrammerEngine.formatInBase(15uL, NumberBase.BIN, BitSize.BYTE)
        assertEquals("0000 1111", binStr)

        val andRes = ProgrammerEngine.bitwiseAnd(0x0FuL, 0xF0uL, BitSize.BYTE)
        assertEquals(0uL, andRes)

        val orRes = ProgrammerEngine.bitwiseOr(0x0FuL, 0xF0uL, BitSize.BYTE)
        assertEquals(0xFFuL, orRes)
    }

    @Test
    fun testPhase4_EquationSolvers() {
        val linear = EquationSolverEngine.solveLinear(2.0, -8.0)
        assertTrue(linear is EquationSolution.LinearResult)
        assertEquals(4.0, (linear as EquationSolution.LinearResult).x, 0.001)

        val quadReal = EquationSolverEngine.solveQuadratic(1.0, -5.0, 6.0)
        assertTrue(quadReal is EquationSolution.QuadraticResult)
        assertFalse((quadReal as EquationSolution.QuadraticResult).isComplex)

        val quadComplex = EquationSolverEngine.solveQuadratic(1.0, 0.0, 4.0)
        assertTrue(quadComplex is EquationSolution.QuadraticResult)
        assertTrue((quadComplex as EquationSolution.QuadraticResult).isComplex)

        val sys = EquationSolverEngine.solveSystem2x2(1.0, 1.0, 5.0, 1.0, -1.0, 1.0)
        assertTrue(sys is EquationSolution.System2x2Result)
        assertEquals(3.0, (sys as EquationSolution.System2x2Result).x, 0.001)
        assertEquals(2.0, (sys as EquationSolution.System2x2Result).y, 0.001)
    }

    @Test
    fun testRoomEntities_CalculationHistory() {
        val calcEntity = com.example.data.database.CalculationHistoryEntity(
            id = 1L,
            expression = "15 * 10",
            result = "150",
            timestamp = 1000L,
            isFavorite = true
        )
        assertEquals("15 * 10", calcEntity.expression)
        assertEquals("150", calcEntity.result)
        assertTrue(calcEntity.isFavorite)
    }

    @Test
    fun testDateTypeConverters() {
        val converters = com.example.data.database.converters.DateTypeConverters()
        val now = java.util.Date()
        val timestamp = converters.dateToTimestamp(now)
        assertNotNull(timestamp)
        val reconstructed = converters.fromTimestamp(timestamp)
        assertEquals(now.time, reconstructed?.time)

        val calendar = java.util.Calendar.getInstance()
        val calTimestamp = converters.calendarToTimestamp(calendar)
        assertNotNull(calTimestamp)
        val reconstructedCal = converters.fromCalendarTimestamp(calTimestamp)
        assertEquals(calendar.timeInMillis, reconstructedCal?.timeInMillis)

        assertNull(converters.dateToTimestamp(null))
        assertNull(converters.fromTimestamp(null))
        assertNull(converters.calendarToTimestamp(null))
        assertNull(converters.fromCalendarTimestamp(null))
    }

    @Test
    fun testDatabaseMigrationsDefinitions() {
        assertEquals(1, com.example.data.database.DatabaseMigrations.MIGRATION_1_2.startVersion)
        assertEquals(2, com.example.data.database.DatabaseMigrations.MIGRATION_1_2.endVersion)
        assertEquals(2, com.example.data.database.DatabaseMigrations.ALL_MIGRATIONS.size)
    }

    @Test
    fun testCurrencyEngine_GlobalCurrenciesAndConversions() {
        val all = com.example.domain.converters.CurrencyEngine.allCurrencies
        assertTrue("Should contain over 160 world currencies", all.size >= 160)

        // Check key major and regional world currencies exist
        val usd = all.first { it.code == "USD" }
        val eur = all.first { it.code == "EUR" }
        val gbp = all.first { it.code == "GBP" }
        val jpy = all.first { it.code == "JPY" }
        val inr = all.first { it.code == "INR" }
        val bdt = all.first { it.code == "BDT" }
        val sar = all.first { it.code == "SAR" }
        val aed = all.first { it.code == "AED" }

        assertEquals(1.0, usd.rateToUsd, 0.0001)

        // Test conversion: 100 USD to EUR
        val eurAmount = com.example.domain.converters.CurrencyEngine.convert(100.0, usd, eur)
        assertEquals(92.0, eurAmount, 0.001)

        // Test conversion: 100 EUR to USD
        val usdAmount = com.example.domain.converters.CurrencyEngine.convert(92.0, eur, usd)
        assertEquals(100.0, usdAmount, 0.001)

        // Test cross-currency conversion: BDT to INR
        val inrFromBdt = com.example.domain.converters.CurrencyEngine.convert(119.5, bdt, inr)
        assertEquals(84.08, inrFromBdt, 0.05)

        // Test search
        val euroSearch = com.example.domain.converters.CurrencyEngine.searchCurrencies("euro")
        assertTrue(euroSearch.any { it.code == "EUR" })

        val takaSearch = com.example.domain.converters.CurrencyEngine.searchCurrencies("taka")
        assertTrue(takaSearch.any { it.code == "BDT" })
    }

    @Test
    fun testPhaseLanguagesAndLocalization() {
        val languages = com.example.domain.languages.LanguageEngine.allLanguages
        assertTrue("Should contain over 100 world languages", languages.size >= 100)

        // Check major languages across different continents exist
        assertTrue(languages.any { it.code == "en" && it.englishName == "English" })
        assertTrue(languages.any { it.code == "bn" && it.englishName == "Bengali" })
        assertTrue(languages.any { it.code == "hi" && it.englishName == "Hindi" })
        assertTrue(languages.any { it.code == "es" && it.englishName == "Spanish" })
        assertTrue(languages.any { it.code == "ar" && it.englishName == "Arabic" })
        assertTrue(languages.any { it.code == "zh" && it.englishName.contains("Chinese") })
        assertTrue(languages.any { it.code == "fr" && it.englishName == "French" })
        assertTrue(languages.any { it.code == "ru" && it.englishName == "Russian" })
        assertTrue(languages.any { it.code == "sw" && it.englishName == "Swahili" })
        assertTrue(languages.any { it.code == "ja" && it.englishName == "Japanese" })

        // Test search
        val bengaliSearch = com.example.domain.languages.LanguageEngine.searchLanguages("bengali")
        assertTrue(bengaliSearch.any { it.code == "bn" })

        val arabicSearch = com.example.domain.languages.LanguageEngine.searchLanguages("العربية")
        assertTrue(arabicSearch.any { it.code == "ar" })

        // Test numeral transliteration
        val bengaliDigits = com.example.domain.languages.LanguageEngine.convertDigits(
            "2024",
            com.example.domain.languages.NumeralScript.BENGALI
        )
        assertEquals("২০২৪", bengaliDigits)

        val arabicDigits = com.example.domain.languages.LanguageEngine.convertDigits(
            "2024",
            com.example.domain.languages.NumeralScript.ARABIC_INDIC
        )
        assertEquals("٢٠٢٤", arabicDigits)

        val devanagariDigits = com.example.domain.languages.LanguageEngine.convertDigits(
            "2024",
            com.example.domain.languages.NumeralScript.DEVANAGARI
        )
        assertEquals("२०२४", devanagariDigits)

        // Test Roman numerals
        assertEquals("MMXXIV", com.example.domain.languages.LanguageEngine.toRomanNumerals(2024))

        // Test number to words
        val engWords = com.example.domain.languages.LanguageEngine.numberToWords(105, "en")
        assertEquals("one hundred and five", engWords)

        val bnWords = com.example.domain.languages.LanguageEngine.numberToWords(0, "bn")
        assertEquals("শূন্য", bnWords)

        // Test LocalizationEngine
        val calcLabelEn = com.example.domain.languages.LocalizationEngine.getString(
            com.example.domain.languages.LocalizationEngine.StringKey.CALCULATOR,
            "en"
        )
        assertEquals("Calculator", calcLabelEn)

        val calcLabelBn = com.example.domain.languages.LocalizationEngine.getString(
            com.example.domain.languages.LocalizationEngine.StringKey.CALCULATOR,
            "bn"
        )
        assertEquals("ক্যালকুলেটর", calcLabelBn)

        val calcLabelEs = com.example.domain.languages.LocalizationEngine.getString(
            com.example.domain.languages.LocalizationEngine.StringKey.CALCULATOR,
            "es"
        )
        assertEquals("Calculadora", calcLabelEs)
    }

    @Test
    fun testThemeModes() {
        val modes = com.example.data.preferences.ThemeMode.values()
        assertEquals(3, modes.size)
        assertTrue(modes.contains(com.example.data.preferences.ThemeMode.SYSTEM))
        assertTrue(modes.contains(com.example.data.preferences.ThemeMode.LIGHT))
        assertTrue(modes.contains(com.example.data.preferences.ThemeMode.DARK))
    }
}
