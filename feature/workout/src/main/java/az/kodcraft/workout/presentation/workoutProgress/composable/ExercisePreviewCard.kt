package az.kodcraft.workout.presentation.workoutProgress.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import az.kodcraft.core.R
import az.kodcraft.core.presentation.theme.PrimaryBlue
import az.kodcraft.core.presentation.theme.PrimaryTurq
import az.kodcraft.core.presentation.theme.body
import az.kodcraft.core.presentation.theme.bodySmallLight
import az.kodcraft.core.utils.noRippleClickable
import az.kodcraft.workout.domain.model.WorkoutDm



@Composable
fun ExercisePreviewCard(
    exercise: WorkoutDm.Exercise,
    isCompleteClick: () -> Unit,
    onClick: () -> Unit,
    onToggleExerciseSetStatus: (exerciseId: String, setId: String) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val isExerciseComplete = exercise.isComplete()
        val isExerciseCurrent = exercise.isCurrent
        Column(
            modifier = Modifier
                .noRippleClickable { onClick() }
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (isExerciseCurrent) PrimaryTurq.copy(0.7f) else Color.LightGray.copy(
                        0.3f
                    )
                )) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = exercise.name,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 16.dp,
                        bottom = 6.dp,
                        end = 6.dp
                    ),
                    style = if (isExerciseCurrent) MaterialTheme.typography.body.copy(fontWeight = FontWeight.Bold)
                    else MaterialTheme.typography.body
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_info),
                    contentDescription = "exercise info btn",
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .size(18.dp),
                    tint = Color.Black
                )
            }
            ExerciseSetsCard(exercise, modifier = Modifier.padding(8.dp),
                onToggleExerciseSetStatus = { onToggleExerciseSetStatus(exercise.id, it) })
        }
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
            painter = painterResource(id = if (isExerciseComplete) R.drawable.ic_done else R.drawable.ic_check),
            contentDescription = "exercise complete indicator",
            tint = if (isExerciseComplete) PrimaryTurq else Color.LightGray.copy(0.3f),
            modifier = Modifier.noRippleClickable { isCompleteClick() }
        )
    }
}

@Composable
fun ExerciseSetsCard(
    exercise: WorkoutDm.Exercise,
    modifier: Modifier,
    onToggleExerciseSetStatus: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(PrimaryBlue.copy(0.6f))
            .padding(vertical = 13.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "set type",
                modifier = Modifier.weight(1.5f),
                style = MaterialTheme.typography.bodySmallLight.copy(fontWeight = FontWeight.Medium)
            )

            Text(
                text = "reps",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodySmallLight.copy(fontWeight = FontWeight.Medium)
            )

            Text(
                text = "rest",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodySmallLight.copy(fontWeight = FontWeight.Medium)
            )

            Text(
                text = "",
                modifier = Modifier.width(24.dp),
                style = MaterialTheme.typography.bodySmallLight.copy(fontWeight = FontWeight.Medium)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        exercise.sets.forEach {
            Row(
                modifier = Modifier
                    .noRippleClickable { onToggleExerciseSetStatus(it.id) }
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = it.type, style = MaterialTheme.typography.bodySmallLight,
                    modifier = Modifier.weight(1.5f)
                )


                Text(
                    text = it.reps, style = MaterialTheme.typography.bodySmallLight,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "${it.restSeconds} sec",
                    style = MaterialTheme.typography.bodySmallLight,
                    modifier = Modifier.weight(1f)
                )

                if (it.isComplete) Icon(
                    painterResource(id = R.drawable.ic_check_circle),
                    tint = PrimaryTurq.copy(0.7f),
                    modifier = Modifier.size(16.dp),
                    contentDescription = "set complete"
                ) else {
                    Spacer(modifier = Modifier.size(16.dp))  // Include spacer to maintain alignment
                }
            }

        }

    }
}