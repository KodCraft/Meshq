package az.kodcraft.core.presentation.composable.calendar

import androidx.lifecycle.SavedStateHandle
import az.kodcraft.core.presentation.bases.BaseViewModel
import az.kodcraft.core.presentation.composable.calendar.contract.CalendarEvent
import az.kodcraft.core.presentation.composable.calendar.contract.CalendarIntent
import az.kodcraft.core.presentation.composable.calendar.contract.CalendarUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    initialState: CalendarUiState,
) : BaseViewModel<CalendarUiState, CalendarUiState.PartialState, CalendarEvent, CalendarIntent>(
    savedStateHandle,
    initialState
) {


    override fun mapIntents(intent: CalendarIntent): Flow<CalendarUiState.PartialState> =
        when (intent) {
            CalendarIntent.NextMonthClicked -> flowOf(
                CalendarUiState.PartialState.NextMonth
            )

            CalendarIntent.PreviousMonthClicked -> flowOf(
                CalendarUiState.PartialState.PreviousMonth
            )
        }


    override fun reduceUiState(
        previousState: CalendarUiState,
        partialState: CalendarUiState.PartialState
    ): CalendarUiState {
        when (partialState) {
            CalendarUiState.PartialState.PreviousMonth -> {
                val prevMonth = uiState.value.yearMonth.minusMonths(1)
                publishEvent(CalendarEvent.MonthChanged(prevMonth))
                return previousState.copy(
                    yearMonth = prevMonth,
                    dates = CalendarDataSource.getDates(prevMonth)
                )
            }

            CalendarUiState.PartialState.NextMonth -> {
                val nextMonth = uiState.value.yearMonth.plusMonths(1)
                publishEvent(CalendarEvent.MonthChanged(nextMonth))
                return previousState.copy(
                    yearMonth = nextMonth,
                    dates = CalendarDataSource.getDates(nextMonth)
                )
            }

        }
    }
}