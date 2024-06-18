package az.kodcraft.dashboard.presentation.traineeDashboard.utils

import az.kodcraft.dashboard.domain.model.DashboardWeekDm
import az.kodcraft.dashboard.domain.model.DayOfWeekDm
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale

fun Int.getWeekData(): DashboardWeekDm {
    val today = LocalDate.now()
    val weekFields = WeekFields.of(Locale.getDefault())
    val firstDayOfYear = LocalDate.of(today.year, 1, 1)

    // Find the first day of the first week of the year according to the locale
    val firstDayOfFirstWeek = if (firstDayOfYear.dayOfWeek == weekFields.firstDayOfWeek) {
        firstDayOfYear
    } else {
        firstDayOfYear.with(TemporalAdjusters.previous(weekFields.firstDayOfWeek))
    }

    // Calculate the start date of the given week number
    val startDate = if (this == 1) {
        firstDayOfFirstWeek
    } else {
        val daysToAdjust = (this - firstDayOfYear.get(weekFields.weekOfWeekBasedYear())) * 7L
        firstDayOfFirstWeek.plusDays(daysToAdjust)
    }

    // Generate the list of dates for the week
    val weekDays = (0..6).map { offset ->
        val date = startDate.plusDays(offset.toLong())

        DayOfWeekDm(
            day = date,
            workoutId = "" // Populate as needed
        )
    }

    return DashboardWeekDm(
        weekDays = weekDays
    )


}
