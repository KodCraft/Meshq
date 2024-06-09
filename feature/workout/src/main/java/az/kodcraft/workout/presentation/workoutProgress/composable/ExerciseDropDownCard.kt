package az.kodcraft.workout.presentation.workoutProgress.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
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
import az.kodcraft.core.presentation.theme.PrimaryTurq
import az.kodcraft.core.presentation.theme.body
import az.kodcraft.core.utils.noRippleClickable
import az.kodcraft.workout.domain.model.AssignedWorkoutDm


@Composable
fun ExerciseDropDownCard(
    exercise: AssignedWorkoutDm.Exercise,
    isCompleteClick: () -> Unit,
    onClick: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val isExerciseComplete = exercise.isComplete()
        val isExerciseCurrent = exercise.isCurrent
        Row(
            modifier = Modifier
                .noRippleClickable { onClick() }
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (isExerciseCurrent) PrimaryTurq.copy(0.3f) else Color.LightGray.copy(
                        0.1f
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = exercise.name,
                modifier = Modifier.padding(16.dp),
                style = if (isExerciseCurrent) MaterialTheme.typography.body.copy(fontWeight = FontWeight.Bold)
                else MaterialTheme.typography.body
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_dropdown),
                contentDescription = "dropdown btn",
                modifier = Modifier.padding(6.dp),
                tint = Color.Black
            )
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
