package az.kodcraft.workout.presentation.workoutDetails.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import az.kodcraft.core.presentation.theme.headLine
import az.kodcraft.workout.R


@Composable
fun CardTabs(selectedTab: WorkoutTab, onTabCLicked: (WorkoutTab) -> Unit) {
    Spacer(modifier = Modifier.height(30.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        WorkoutTab.entries.forEach { tab ->
            Column(
                modifier = Modifier
                    .clickable { onTabCLicked(tab) }
                    .weight(0.5f)
            ) {
                Text(
                    text = stringResource(id = tab.title),
                    style = if (selectedTab == tab) MaterialTheme.typography.headLine.copy(Color.White) else MaterialTheme.typography.headLine.copy(
                        color = Color.White.copy(0.5f)
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(
                    thickness = 2.dp,
                    color = if (selectedTab == tab) Color.White else Color.White.copy(0.5f)
                )
            }
        }
    }

}

enum class WorkoutTab(val title: Int) {
    Program(R.string.workout_details_screen_workout_tab_program),
    Notes(R.string.workout_details_screen_workout_tab_notes)
}