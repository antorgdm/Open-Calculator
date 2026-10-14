package com.example.domain.datetime

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object DateTimeEngine {
    data class AgeResult(
        val years: Int,
        val months: Int,
        val days: Int,
        val totalDays: Long,
        val nextBirthdayDays: Long,
        val nextBirthdayWeekday: String
    )

    data class DateDiffResult(
        val totalDays: Long,
        val weeks: Long,
        val remainingDays: Long,
        val years: Int,
        val months: Int,
        val days: Int
    )

    fun calculateAge(birthDate: Calendar, today: Calendar = Calendar.getInstance()): AgeResult {
        var years = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
        var months = today.get(Calendar.MONTH) - birthDate.get(Calendar.MONTH)
        var days = today.get(Calendar.DAY_OF_MONTH) - birthDate.get(Calendar.DAY_OF_MONTH)

        if (days < 0) {
            val prevMonth = today.clone() as Calendar
            prevMonth.add(Calendar.MONTH, -1)
            days += prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
            months--
        }

        if (months < 0) {
            years--
            months += 12
        }

        val diffMillis = kotlin.math.max(0L, today.timeInMillis - birthDate.timeInMillis)
        val totalDays = TimeUnit.MILLISECONDS.toDays(diffMillis)

        // Next Birthday
        val nextBirthday = Calendar.getInstance().apply {
            set(Calendar.MONTH, birthDate.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, birthDate.get(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val currentZero = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (nextBirthday.before(currentZero)) {
            nextBirthday.add(Calendar.YEAR, 1)
        }
        val daysToNext = TimeUnit.MILLISECONDS.toDays(nextBirthday.timeInMillis - currentZero.timeInMillis)
        val weekdayName = SimpleDateFormat("EEEE", Locale.US).format(nextBirthday.time)

        return AgeResult(
            years = kotlin.math.max(0, years),
            months = kotlin.math.max(0, months),
            days = kotlin.math.max(0, days),
            totalDays = totalDays,
            nextBirthdayDays = daysToNext,
            nextBirthdayWeekday = weekdayName
        )
    }

    fun calculateDateDiff(startDate: Calendar, endDate: Calendar): DateDiffResult {
        val (first, second) = if (startDate.before(endDate)) startDate to endDate else endDate to startDate
        
        // Normalize to noon to prevent DST 23/25 hour transition offsets
        val c1 = (first.clone() as Calendar).apply {
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val c2 = (second.clone() as Calendar).apply {
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val diffMillis = kotlin.math.abs(c2.timeInMillis - c1.timeInMillis)
        val totalDays = ((diffMillis + TimeUnit.HOURS.toMillis(12)) / TimeUnit.DAYS.toMillis(1))
        val weeks = totalDays / 7
        val remDays = totalDays % 7

        var years = second.get(Calendar.YEAR) - first.get(Calendar.YEAR)
        var months = second.get(Calendar.MONTH) - first.get(Calendar.MONTH)
        var days = second.get(Calendar.DAY_OF_MONTH) - first.get(Calendar.DAY_OF_MONTH)

        if (days < 0) {
            val prevMonth = second.clone() as Calendar
            prevMonth.add(Calendar.MONTH, -1)
            days += prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
            months--
        }

        if (months < 0) {
            years--
            months += 12
        }

        return DateDiffResult(totalDays, weeks, remDays, years, months, days)
    }

    fun addDaysToDate(baseDate: Calendar, daysToAdd: Int): Calendar {
        val result = baseDate.clone() as Calendar
        result.add(Calendar.DAY_OF_MONTH, daysToAdd)
        return result
    }

    fun calculateWorkingDays(startDate: Calendar, endDate: Calendar): Long {
        val (first, second) = if (startDate.before(endDate)) {
            (startDate.clone() as Calendar) to (endDate.clone() as Calendar)
        } else {
            (endDate.clone() as Calendar) to (startDate.clone() as Calendar)
        }

        var count = 0L
        while (first.before(second) || (first.get(Calendar.YEAR) == second.get(Calendar.YEAR) && first.get(Calendar.DAY_OF_YEAR) == second.get(Calendar.DAY_OF_YEAR))) {
            val dayOfWeek = first.get(Calendar.DAY_OF_WEEK)
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                count++
            }
            first.add(Calendar.DAY_OF_MONTH, 1)
        }
        return count
    }

    fun formatDate(cal: Calendar): String {
        return SimpleDateFormat("MMM dd, yyyy", Locale.US).format(cal.time)
    }
}
