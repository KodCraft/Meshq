package az.kodcraft.core.presentation.composable.calendar.contract

import java.time.YearMonth

sealed class CalendarEvent {
    data class MonthChanged(val yearMonth:YearMonth):CalendarEvent()
}