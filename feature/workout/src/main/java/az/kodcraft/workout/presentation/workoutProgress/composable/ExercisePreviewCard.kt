package az.kodcraft.workout.presentation.workoutProgress.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import az.kodcraft.core.R
import az.kodcraft.core.presentation.bases.BasePreviewContainer
import az.kodcraft.core.presentation.composable.textField.TextFieldSingleLineBox
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
    isEditable: Boolean,
    onToggleExerciseSetStatus: (exerciseId: String, setId: String) -> Unit,
    onWeightValueChange: (value: String, setId: String) -> Unit,
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
            ExerciseSetsCard(
                exercise, modifier = Modifier.padding(8.dp),
                onToggleExerciseSetStatus = { onToggleExerciseSetStatus(exercise.id, it) },
                onWeightValueChange = onWeightValueChange,
                isEditable = isEditable
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
            painter = painterResource(id = if (isExerciseComplete) R.drawable.ic_done else R.drawable.ic_check),
            contentDescription = "exercise complete indicator",
            tint = if (isExerciseComplete) PrimaryTurq else Color.LightGray.copy(0.3f),
            modifier = Modifier.noRippleClickable { if (isEditable) isCompleteClick() }
        )
    }
}

@Composable
fun ExerciseSetsCard(
    exercise: WorkoutDm.Exercise,
    modifier: Modifier,
    isEditable: Boolean = false,
    onToggleExerciseSetStatus: (String) -> Unit = {},
    onWeightValueChange: (value: String, setId: String) -> Unit = { _, _ -> }
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
                maxLines = 1,
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .weight(1.5f),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
            )

            Text(
                text = "reps",
                maxLines = 1,
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .weight(1f),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
            )

            Text(
                text = "weight",
                maxLines = 1,
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .weight(1.5f),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
            )

            Text(
                text = "rest",
                maxLines = 1,
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .weight(1f),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
            )

            Text(
                text = "",
                maxLines = 1,
                modifier = Modifier
                    .width(24.dp),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        exercise.sets.forEach {
            Row(
                modifier = Modifier
                    .noRippleClickable { if (isEditable) onToggleExerciseSetStatus(it.id) }
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = it.type,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmallLight,
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .weight(1.5f)
                )


                Text(
                    text = it.reps,
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
                    TextFieldSingleLineBox(
                        isEditable = isEditable,
                        value = it.weight,
                        onValueChange = { v -> onWeightValueChange(v, it.id) },
                        textStyle = MaterialTheme.typography.bodySmallLight,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = it.unit,
                        style = MaterialTheme.typography.bodySmallLight,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    text = "${it.restSeconds} sec",
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmallLight,
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .weight(1f)
                )

                Box(modifier = Modifier.width(24.dp)) {
                    if (it.isComplete) Icon(
                        painterResource(id = R.drawable.ic_check_circle),
                        tint = PrimaryTurq.copy(0.7f),
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.Center),
                        contentDescription = "set complete"
                    )
                }
            }

        }

    }
}


@Preview()
@Composable
fun PreviewExercisePreviewCard() = BasePreviewContainer {
    ExercisePreviewCard(
        exercise = WorkoutDm.Exercise.MOCK,
        isCompleteClick = { },
        onClick = { },
        isEditable = true,
        onToggleExerciseSetStatus = { _, _ -> },
        onWeightValueChange = { _, _ -> })
}