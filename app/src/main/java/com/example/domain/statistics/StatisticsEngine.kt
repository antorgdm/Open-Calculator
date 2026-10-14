package com.example.domain.statistics

import kotlin.math.pow
import kotlin.math.sqrt

data class StatisticsSummary(
    val count: Int,
    val sum: Double,
    val mean: Double,
    val median: Double,
    val mode: List<Double>,
    val min: Double,
    val max: Double,
    val range: Double,
    val variance: Double,
    val sampleStdDev: Double,
    val popStdDev: Double,
    val q1: Double,
    val q3: Double,
    val iqr: Double
)

data class DatasetParseResult(
    val numbers: List<Double>,
    val invalidTokens: List<String>
)

object StatisticsEngine {

    fun parseDataset(input: String): DatasetParseResult {
        val numbers = mutableListOf<Double>()
        val invalidTokens = mutableListOf<String>()

        input.split(",", " ", "\n", "\t")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .forEach { token ->
                val num = token.toDoubleOrNull()
                if (num != null) {
                    numbers.add(num)
                } else {
                    invalidTokens.add(token)
                }
            }

        return DatasetParseResult(numbers, invalidTokens)
    }

    fun compute(data: List<Double>): StatisticsSummary? {
        if (data.isEmpty()) return null
        val sorted = data.sorted()
        val n = sorted.size
        val sum = sorted.sum()
        val mean = sum / n

        val median = if (n % 2 == 1) {
            sorted[n / 2]
        } else {
            (sorted[n / 2 - 1] + sorted[n / 2]) / 2.0
        }

        // Mode
        val frequencyMap = sorted.groupingBy { it }.eachCount()
        val maxFreq = frequencyMap.values.maxOrNull() ?: 1
        val modes = if (maxFreq > 1) {
            frequencyMap.filter { it.value == maxFreq }.keys.toList()
        } else {
            emptyList()
        }

        val min = sorted.first()
        val max = sorted.last()
        val range = max - min

        // Variance & StdDev
        val sumSquaredDiff = sorted.sumOf { (it - mean).pow(2) }
        val popVariance = sumSquaredDiff / n
        val popStdDev = sqrt(popVariance)
        val sampleVariance = if (n > 1) sumSquaredDiff / (n - 1) else 0.0
        val sampleStdDev = sqrt(sampleVariance)

        // Quartiles
        val q1 = percentile(sorted, 25.0)
        val q3 = percentile(sorted, 75.0)
        val iqr = q3 - q1

        return StatisticsSummary(
            count = n,
            sum = sum,
            mean = mean,
            median = median,
            mode = modes,
            min = min,
            max = max,
            range = range,
            variance = sampleVariance,
            sampleStdDev = sampleStdDev,
            popStdDev = popStdDev,
            q1 = q1,
            q3 = q3,
            iqr = iqr
        )
    }

    private fun percentile(sorted: List<Double>, p: Double): Double {
        if (sorted.isEmpty()) return 0.0
        val rank = (p / 100.0) * (sorted.size - 1)
        val lowerIndex = rank.toInt()
        val upperIndex = (lowerIndex + 1).coerceAtMost(sorted.size - 1)
        val fraction = rank - lowerIndex
        return sorted[lowerIndex] + fraction * (sorted[upperIndex] - sorted[lowerIndex])
    }
}
