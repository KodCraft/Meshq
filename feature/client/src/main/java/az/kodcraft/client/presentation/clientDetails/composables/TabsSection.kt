package az.kodcraft.client.presentation.clientDetails.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import az.kodcraft.client.domain.model.ClientDm
import az.kodcraft.client.presentation.clientDetails.contract.ClientDetailsIntent
import az.kodcraft.core.presentation.theme.PrimaryBlue
import az.kodcraft.core.presentation.theme.body
import java.time.LocalDate

@Composable
fun TabsSection(
    clientDetails: ClientDm,
    selectedDate: LocalDate,
    onIntent: (ClientDetailsIntent) -> Unit
) {
    // Tabs
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Calendar", "Progress", "Goals")

    TabRow(
        selectedTabIndex = selectedTabIndex,
        contentColor = Color.White,
        containerColor = PrimaryBlue,
        indicator = {}
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { selectedTabIndex = index },
                text = {
                    Text(
                        title,
                        style = if (selectedTabIndex == index) MaterialTheme.typography.body.copy(
                            fontWeight = FontWeight.Bold
                        ) else MaterialTheme.typography.body
                    )
                }
            )
        }
    }

    // Content
    Spacer(modifier = Modifier.height(10.dp))
    when (selectedTabIndex) {
        0 -> CalendarSection(
            onDateClick = { onIntent.invoke(ClientDetailsIntent.SelectDate(it)) },
            onMonthChanged = {
                onIntent.invoke(
                    ClientDetailsIntent.GetMonthWorkouts(
                        it
                    )
                )
            },
            workoutSchedule = clientDetails.workoutSchedule,
            selectedDate = selectedDate
        )

        1 -> ProgressSection()  // Implement this based on your progress data
        2 -> GoalsSection()     // Implement this based on your goals data
    }
}