package az.kodcraft.client.presentation.clientDetails.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import az.kodcraft.client.domain.model.ClientDm
import az.kodcraft.core.presentation.composable.calendar.MonthlyCalendar
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarSection(
    onMonthChanged: (YearMonth) -> Unit,
    onDateClick: (LocalDate) -> Unit,
    workoutSchedule: List<ClientDm.WorkoutSessionDm>,
    selectedDate: LocalDate
) {
    Column(modifier = Modifier.padding(horizontal = 26.dp).height(300.dp)) {
        MonthlyCalendar(
            onDateClick = onDateClick,
            onMonthChanged = onMonthChanged,
            labels = workoutSchedule.map { it.date },
            selectedDate = selectedDate
        )
    }
}