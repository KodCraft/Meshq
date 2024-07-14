package az.kodcraft.core.presentation.composable.calendar.contract


sealed class CalendarIntent {
    data object NextMonthClicked : CalendarIntent()
    data object PreviousMonthClicked : CalendarIntent()
}