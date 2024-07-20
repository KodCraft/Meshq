package az.kodcraft.client.presentation.clientDetails.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import az.kodcraft.client.domain.model.ClientDm
import az.kodcraft.core.presentation.theme.AccentBlue
import az.kodcraft.core.presentation.theme.PrimaryRed
import az.kodcraft.core.presentation.theme.PrimaryTurq
import az.kodcraft.core.presentation.theme.body
import az.kodcraft.core.utils.formatDateToWeeklyStringDayAndMonth
import az.kodcraft.core.utils.noRippleClickable
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun WeeklyWorkoutsSection(
    workoutSchedule: List<ClientDm.WorkoutSessionDm>,
    selectedDate: LocalDate,
    isScheduleLoading: Boolean,
    onAddWorkoutClicked: (LocalDate) -> Unit
) {

    Box {
        HorizontalDivider()
        if (isScheduleLoading)
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                color = PrimaryTurq,
                trackColor = AccentBlue
            )
    }
    Spacer(modifier = Modifier.height(10.dp))

    // Map workouts to corresponding DayOfWeek
    val workoutsByDay = DayOfWeek.entries.associateWith { day ->
        workoutSchedule.firstOrNull { it.date.dayOfWeek == day }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(workoutsByDay.entries.toList()) { (day, workout) ->
            val weekDay = selectedDate.with(day)
            val formattedDate = weekDay.formatDateToWeeklyStringDayAndMonth()
            DayWithWorkoutItem(formattedDate, workout) { onAddWorkoutClicked(weekDay) }
        }
    }
}

@Composable
fun DayWithWorkoutItem(
    formattedDate: String,
    workout: ClientDm.WorkoutSessionDm?,
    onAddWorkoutClicked: () -> Unit
) {
    Column(Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Bottom,
            ) {
                DateDisplay(dateString = formattedDate)
                Text(
                    text = workout?.workoutName ?: "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
            }
            Icon(
                painter = if (workout == null) painterResource(id = az.kodcraft.core.R.drawable.ic_add_circle) else painterResource(
                    id = az.kodcraft.core.R.drawable.ic_remove_circle
                ),
                contentDescription = null,
                modifier = Modifier
                    .noRippleClickable { if (workout == null) onAddWorkoutClicked() }
                    .size(18.dp),
                tint = if (workout == null) PrimaryTurq.copy(0.7f) else PrimaryRed
            )

        }
        HorizontalDivider(color = Color.White.copy(0.5f))
    }
}


@Composable
fun DateDisplay(
    modifier: Modifier = Modifier,
    dateString: String,
    color: Color = Color.White.copy(0.5f)
) {
    val parts = dateString.split(" ")
    val dayOfWeek = parts[0]
    val date = parts.subList(1, parts.size).joinToString(" ")

    Text(
        modifier = modifier.width(80.dp),
        style = MaterialTheme.typography.body,
        maxLines = 1,
        color = color,
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                ),
            ) {
                append(dayOfWeek)
            }
            append("  ")
            append(date)
        }
    )
}