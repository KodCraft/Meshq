package az.kodcraft.dashboard.presentation.traineeDashboard.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import az.kodcraft.core.presentation.theme.body
import az.kodcraft.core.utils.noRippleClickable
import az.kodcraft.dashboard.domain.model.DayOfWeekDm
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale


@Composable
fun WeekRow(modifier: Modifier = Modifier, data:List<DayOfWeekDm>, selectedDay: LocalDate, onDayClicked:(date:LocalDate) -> Unit) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        data.forEach {
            WeekDay(data = it, selectedDay = selectedDay) { onDayClicked(it.day) }
        }
    }
}

@Composable
fun WeekDay(data: DayOfWeekDm, selectedDay:LocalDate, onDayClicked:() -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.noRippleClickable { onDayClicked() }
            .width(30.dp)
            .background(
                if (data.day == selectedDay) MaterialTheme.colorScheme.secondary else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            ).padding(vertical = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = data.day.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                color = if (data.day == selectedDay) Color.Black else Color.White,

                style = MaterialTheme.typography.body
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = data.day.dayOfMonth.toString(),
                color = if (data.day == selectedDay) Color.Black else Color.White,
                style = MaterialTheme.typography.body
            )
        }
    }

}