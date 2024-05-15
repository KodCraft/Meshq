package az.kodcraft.workout.presentation.createWorkout.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import az.kodcraft.core.presentation.theme.bodySmallLight
import az.kodcraft.workout.domain.model.CreateWorkoutDm


@Composable
fun ExerciseSetsCard(
    sets: List<CreateWorkoutDm.Exercise.Set>
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color.LightGray.copy(0.1f))
            .padding(vertical = 16.dp)
    ) {
        ExerciseSetsHeader()
        Spacer(modifier = Modifier.height(18.dp))
        sets.forEach { set ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = set.type,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmallLight,
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .weight(1.5f)
                )

                Text(
                    text = set.reps,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmallLight,
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .weight(1f)
                )


                Row(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .weight(1.5f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = set.weight,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmallLight,
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .weight(1f)
                    )

                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = set.unit,
                        style = MaterialTheme.typography.bodySmallLight,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Row(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .weight(1.5f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = set.restSeconds,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmallLight,
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .weight(1f)
                    )

                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "sec",
                        style = MaterialTheme.typography.bodySmallLight,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}
