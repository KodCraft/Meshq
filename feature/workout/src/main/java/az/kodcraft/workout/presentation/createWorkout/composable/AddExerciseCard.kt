package az.kodcraft.workout.presentation.createWorkout.composable

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import az.kodcraft.core.presentation.theme.PrimaryTurq
import az.kodcraft.core.presentation.theme.bodySmallLight
import az.kodcraft.core.presentation.theme.largeHeadLine
import az.kodcraft.core.utils.noRippleClickable
import az.kodcraft.workout.domain.model.CreateWorkoutDm
import kotlin.random.Random
import kotlin.random.nextULong

@Composable
fun AddExercise(
    modifier: Modifier = Modifier,
    exercise: CreateWorkoutDm.Exercise,
    onSaveExerciseSets: (List<CreateWorkoutDm.Exercise.Set>) -> Unit = {},
    onDismiss: () -> Unit = {}
) {

    var setsInEdit by remember {
        mutableStateOf(exercise.sets)
    }
    Column(modifier = modifier) {
        Row(Modifier.padding(6.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = az.kodcraft.core.R.drawable.ic_back),
                contentDescription = "",
                modifier = Modifier.noRippleClickable { onDismiss() }
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.largeHeadLine.copy(Color.White)
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_check_circle),
                tint = PrimaryTurq,
                contentDescription = "",
                modifier = Modifier
                    .padding(8.dp)
                    .noRippleClickable { onSaveExerciseSets(setsInEdit) }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        AddExerciseSetsCard(
            setsInEdit,
            updateSets = { setsInEdit = it }
        )
    }
}

@Composable
fun AddExerciseSetsCard(
    setsInEdit: List<CreateWorkoutDm.Exercise.Set>,
    updateSets: (List<CreateWorkoutDm.Exercise.Set>) -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color.LightGray.copy(0.1f))
            .padding(vertical = 16.dp)
    ) {

        val isEditable = true
        var lastSet by remember {
            mutableStateOf(CreateWorkoutDm.Exercise.Set.EMPTY)
        }
        ExerciseSetsHeader()
        Spacer(modifier = Modifier.height(18.dp))
        setsInEdit.forEach { set ->
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

                TextFieldSingleLineBox(
                    isEditable = isEditable,
                    value = set.reps,
                    onValueChange = { v ->
                        updateSets(
                            setsInEdit.map { if (it.id == set.id) it.copy(reps = v) else it })
                    },
                    textStyle = MaterialTheme.typography.bodySmallLight,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 6.dp)
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
                        value = set.weight,
                        onValueChange = { v ->
                            updateSets(
                                setsInEdit.map { if (it.id == set.id) it.copy(weight = v) else it })
                        },
                        textStyle = MaterialTheme.typography.bodySmallLight,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 6.dp)
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
                    TextFieldSingleLineBox(
                        isEditable = isEditable,
                        value = set.restSeconds,
                        onValueChange = { v ->
                            updateSets(
                                setsInEdit.map { if (it.id == set.id) it.copy(restSeconds = v) else it })
                        },
                        textStyle = MaterialTheme.typography.bodySmallLight,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 6.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "sec",
                        style = MaterialTheme.typography.bodySmallLight,
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Box(modifier = Modifier.noRippleClickable {
                    updateSets(setsInEdit.filterNot { it.id == set.id })
                }.width(24.dp)) {
                    Icon(
                        painterResource(id = R.drawable.ic_remove_circle),
                        tint = Color.Red,
                        modifier = Modifier.padding(4.dp)
                            .size(16.dp)
                            .align(Alignment.Center),
                        contentDescription = "set complete"
                    )

                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = lastSet.type,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmallLight,
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .weight(1.5f)
            )

            TextFieldSingleLineBox(
                isEditable = isEditable,
                value = lastSet.reps,
                onValueChange = { v -> lastSet = lastSet.copy(reps = v) },
                textStyle = MaterialTheme.typography.bodySmallLight,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 6.dp)
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
                    value = lastSet.weight,
                    onValueChange = { v -> lastSet = lastSet.copy(weight = v) },
                    textStyle = MaterialTheme.typography.bodySmallLight,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 6.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = lastSet.unit,
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
                TextFieldSingleLineBox(
                    isEditable = isEditable,
                    value = lastSet.restSeconds.toString(),
                    onValueChange = { v -> lastSet = lastSet.copy(restSeconds = v) },
                    textStyle = MaterialTheme.typography.bodySmallLight,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 6.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "sec",
                    style = MaterialTheme.typography.bodySmallLight,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Box(modifier = Modifier.noRippleClickable {
                val newId = Random.nextULong().toString()
                updateSets(setsInEdit + lastSet.copy(id = newId))
                lastSet = CreateWorkoutDm.Exercise.Set.EMPTY
            }.width(24.dp)) {
                Icon(
                    painterResource(id = R.drawable.ic_add_circle),
                    tint = PrimaryTurq.copy(0.7f),
                    modifier = Modifier
                        .padding(4.dp)
                        .size(16.dp)
                        .align(Alignment.Center),
                    contentDescription = "set complete"
                )

            }
        }
    }
}

@Composable
fun ExerciseSetsHeader() {
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
            style = MaterialTheme.typography.bodySmallLight.copy(fontWeight = FontWeight.Medium)
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
}

@Composable
@Preview
fun PreviewAddExercise() = BasePreviewContainer {
    AddExercise(exercise = CreateWorkoutDm.Exercise.MOCK)
}