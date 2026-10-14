package com.example.domain.converters

import java.text.DecimalFormat

data class UnitItem(val id: String, val name: String, val symbol: String, val toBaseFactor: Double)

enum class UnitCategory(val displayName: String) {
    CURRENCY("Currency"),
    LENGTH("Length"),
    WEIGHT("Weight & Mass"),
    AREA("Area"),
    VOLUME("Volume"),
    TEMPERATURE("Temperature"),
    SPEED("Speed"),
    TIME("Time"),
    DATA_STORAGE("Data Storage"),
    PRESSURE("Pressure"),
    ENERGY("Energy"),
    POWER("Power"),
    FREQUENCY("Frequency"),
    ANGLE("Angle")
}

object UnitConverterEngine {

    val categories = UnitCategory.values().toList()

    fun getUnitsForCategory(category: UnitCategory): List<UnitItem> {
        return when (category) {
            UnitCategory.CURRENCY -> CurrencyEngine.allCurrencies.map {
                UnitItem(it.code, "${it.name} (${it.country})", it.symbol, 1.0 / it.rateToUsd)
            }
            UnitCategory.LENGTH -> listOf(
                UnitItem("m", "Meter", "m", 1.0),
                UnitItem("km", "Kilometer", "km", 1000.0),
                UnitItem("cm", "Centimeter", "cm", 0.01),
                UnitItem("mm", "Millimeter", "mm", 0.001),
                UnitItem("in", "Inch", "in", 0.0254),
                UnitItem("ft", "Foot", "ft", 0.3048),
                UnitItem("yd", "Yard", "yd", 0.9144),
                UnitItem("mi", "Mile", "mi", 1609.344),
                UnitItem("nmi", "Nautical Mile", "nmi", 1852.0)
            )
            UnitCategory.WEIGHT -> listOf(
                UnitItem("kg", "Kilogram", "kg", 1.0),
                UnitItem("g", "Gram", "g", 0.001),
                UnitItem("mg", "Milligram", "mg", 0.000001),
                UnitItem("lb", "Pound", "lb", 0.45359237),
                UnitItem("oz", "Ounce", "oz", 0.028349523125),
                UnitItem("t", "Metric Ton", "t", 1000.0),
                UnitItem("st", "Stone", "st", 6.35029318)
            )
            UnitCategory.AREA -> listOf(
                UnitItem("m2", "Square Meter", "m²", 1.0),
                UnitItem("km2", "Square Kilometer", "km²", 1_000_000.0),
                UnitItem("cm2", "Square Centimeter", "cm²", 0.0001),
                UnitItem("ft2", "Square Foot", "ft²", 0.092903),
                UnitItem("yd2", "Square Yard", "yd²", 0.836127),
                UnitItem("ac", "Acre", "ac", 4046.8564),
                UnitItem("ha", "Hectare", "ha", 10000.0)
            )
            UnitCategory.VOLUME -> listOf(
                UnitItem("l", "Liter", "L", 1.0),
                UnitItem("ml", "Milliliter", "mL", 0.001),
                UnitItem("m3", "Cubic Meter", "m³", 1000.0),
                UnitItem("gal", "Gallon (US)", "gal", 3.78541),
                UnitItem("qt", "Quart (US)", "qt", 0.946353),
                UnitItem("pt", "Pint (US)", "pt", 0.473176),
                UnitItem("cup", "Cup (US)", "cup", 0.236588),
                UnitItem("floz", "Fluid Ounce (US)", "fl oz", 0.0295735)
            )
            UnitCategory.TEMPERATURE -> listOf(
                UnitItem("c", "Celsius", "°C", 1.0),
                UnitItem("f", "Fahrenheit", "°F", 1.0),
                UnitItem("k", "Kelvin", "K", 1.0)
            )
            UnitCategory.SPEED -> listOf(
                UnitItem("mps", "Meter per second", "m/s", 1.0),
                UnitItem("kmh", "Kilometer per hour", "km/h", 0.277778),
                UnitItem("mph", "Miles per hour", "mph", 0.44704),
                UnitItem("knot", "Knot", "kn", 0.514444),
                UnitItem("fps", "Feet per second", "ft/s", 0.3048)
            )
            UnitCategory.TIME -> listOf(
                UnitItem("s", "Second", "s", 1.0),
                UnitItem("min", "Minute", "min", 60.0),
                UnitItem("h", "Hour", "h", 3600.0),
                UnitItem("d", "Day", "d", 86400.0),
                UnitItem("wk", "Week", "wk", 604800.0),
                UnitItem("mo", "Month (30d)", "mo", 2592000.0),
                UnitItem("yr", "Year (365d)", "yr", 31536000.0)
            )
            UnitCategory.DATA_STORAGE -> listOf(
                UnitItem("b", "Byte", "B", 1.0),
                UnitItem("kb", "Kilobyte", "KB", 1024.0),
                UnitItem("mb", "Megabyte", "MB", 1024.0 * 1024.0),
                UnitItem("gb", "Gigabyte", "GB", 1024.0 * 1024.0 * 1024.0),
                UnitItem("tb", "Terabyte", "TB", 1024.0 * 1024.0 * 1024.0 * 1024.0),
                UnitItem("pb", "Petabyte", "PB", 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0)
            )
            UnitCategory.PRESSURE -> listOf(
                UnitItem("pa", "Pascal", "Pa", 1.0),
                UnitItem("kpa", "Kilopascal", "kPa", 1000.0),
                UnitItem("bar", "Bar", "bar", 100000.0),
                UnitItem("psi", "Pound/sq inch", "psi", 6894.757),
                UnitItem("atm", "Atmosphere", "atm", 101325.0)
            )
            UnitCategory.ENERGY -> listOf(
                UnitItem("j", "Joule", "J", 1.0),
                UnitItem("kj", "Kilojoule", "kJ", 1000.0),
                UnitItem("cal", "Calorie", "cal", 4.184),
                UnitItem("kcal", "Kilocalorie", "kcal", 4184.0),
                UnitItem("kwh", "Kilowatt-hour", "kWh", 3600000.0)
            )
            UnitCategory.POWER -> listOf(
                UnitItem("w", "Watt", "W", 1.0),
                UnitItem("kw", "Kilowatt", "kW", 1000.0),
                UnitItem("hp", "Horsepower", "hp", 745.7),
                UnitItem("mw", "Megawatt", "MW", 1000000.0)
            )
            UnitCategory.FREQUENCY -> listOf(
                UnitItem("hz", "Hertz", "Hz", 1.0),
                UnitItem("khz", "Kilohertz", "kHz", 1000.0),
                UnitItem("mhz", "Megahertz", "MHz", 1000000.0),
                UnitItem("ghz", "Gigahertz", "GHz", 1000000000.0)
            )
            UnitCategory.ANGLE -> listOf(
                UnitItem("deg", "Degree", "°", 1.0),
                UnitItem("rad", "Radian", "rad", 57.2957795),
                UnitItem("grad", "Gradian", "grad", 0.9),
                UnitItem("arcmin", "Arcminute", "′", 1.0 / 60.0),
                UnitItem("arcsec", "Arcsecond", "″", 1.0 / 3600.0)
            )
        }
    }

    fun convert(value: Double, from: UnitItem, to: UnitItem, category: UnitCategory): Double {
        if (category == UnitCategory.TEMPERATURE) {
            val celsius = when (from.id) {
                "c" -> value
                "f" -> (value - 32.0) * 5.0 / 9.0
                "k" -> value - 273.15
                else -> value
            }
            return when (to.id) {
                "c" -> celsius
                "f" -> celsius * 9.0 / 5.0 + 32.0
                "k" -> celsius + 273.15
                else -> celsius
            }
        }

        val baseValue = value * from.toBaseFactor
        return baseValue / to.toBaseFactor
    }

    fun formatNumber(value: Double): String {
        if (value.isNaN() || value.isInfinite()) return "-"
        val df = DecimalFormat("#,##0.######")
        return df.format(value)
    }
}
