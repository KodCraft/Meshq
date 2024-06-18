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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import az.kodcraft.core.R
import az.kodcraft.core.presentation.bases.BasePreviewContainer
import az.kodcraft.core.presentation.composable.textField.RangeSelectionTextField
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

    var lastSet by remember {
        mutableStateOf(CreateWorkoutDm.Exercise.Set.EMPTY)
    }
    var lastSetApplied by remember { mutableStateOf(false) }

    LaunchedEffect(lastSet) {
        lastSetApplied = lastSet.reps != "0" && lastSet.reps.isNotBlank()
    }
    Column(modifier = modifier) {
        Row(Modifier.padding(6.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
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
                    .noRippleClickable { onSaveExerciseSets(if (lastSetApplied) setsInEdit + lastSet else setsInEdit) }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        AddExerciseSetsCard(
            setsInEdit,
            lastSet = lastSet,
            lastSetApplied = lastSetApplied,
            updateLastSet = { lastSet = it },
            updateSets = { setsInEdit = it }
        )
    }
}

@Composable
fun AddExerciseSetsCard(
    setsInEdit: List<CreateWorkoutDm.Exercise.Set>,
    updateSets: (List<CreateWorkoutDm.Exercise.Set>) -> Unit,
    lastSet: CreateWorkoutDm.Exercise.Set,
    lastSetApplied: Boolean,
    updateLastSet: (CreateWorkoutDm.Exercise.Set) -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color.LightGray.copy(0.1f))
            .padding(vertical = 16.dp)
    ) {

        val isEditable = true
        ExerciseSetsHeader()
        Spacer(modifier = Modifier.height(18.dp))
        setsInEdit.forEach { set ->
            ExerciseSet(
                isLastSet = false,
                isEditable = isEditable,
                set = set,
                onUpdateSetType = { v ->
                    updateSets(
                        setsInEdit.map { if (it.id == set.id) it.copy(type = v) else it })
                },
                onUpdateReps = { v ->
                    updateSets(setsInEdit.map { if (it.id == set.id) it.copy(reps = v) else it })
                },
                onUpdateWeight = { v ->
                    updateSets(setsInEdit.map { if (it.id == set.id) it.copy(weight = v) else it })
                },
                onUpdateRestSeconds = { v ->
                    updateSets(setsInEdit.map { if (it.id == set.id) it.copy(restSeconds = v) else it })
                },
                onIconClicked = { setsInEdit.filterNot { it.id == set.id } })
        }
        ExerciseSet(
            isEditable = true,
            set = lastSet,
            onUpdateSetType = { v ->
                updateLastSet(lastSet.copy(type = v))
            },
            onUpdateReps = { v -> updateLastSet(lastSet.copy(reps = v)) },
            onUpdateWeight = { v -> updateLastSet(lastSet.copy(weight = v)) },
            onUpdateRestSeconds = { v -> updateLastSet(lastSet.copy(restSeconds = v)) },
            onIconClicked = {
                val newId = Random
                    .nextULong()
                    .toString()
                updateSets(setsInEdit + lastSet.copy(id = newId))
            },
            isLastSet = true,
            lastSetApplied = lastSetApplied
        )
    }
}

@Composable
fun ExerciseSet(
    isEditable: Boolean,
    set: CreateWorkoutDm.Exercise.Set,
    onUpdateSetType: (String) -> Unit,
    onUpdateReps: (String) -> Unit,
    onUpdateWeight: (String) -> Unit,
    onUpdateRestSeconds: (String) -> Unit,
    onIconClicked: () -> Unit,
    isLastSet: Boolean,
    lastSetApplied: Boolean = true
) {
    var isRepsInFocus by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SetTypeDropdown(
            selectedType = set.type, onTypeSelected = { v ->
                onUpdateSetType(v)
            }, modifier = Modifier
                .weight(1.2f),
            color = if (isLastSet && !lastSetApplied) Color.White.copy(0.5f) else Color.White
        )

        Box(
            Modifier
                .weight(1.2f)
        ) {
            TextFieldSingleLineBox(
                isEditable = false,
                value = set.reps,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number ),
                textStyle = MaterialTheme.typography.bodySmallLight.copy(
                    color = if (isLastSet && !lastSetApplied) Color.White.copy(0.5f) else Color.White
                ),
                modifier = Modifier.noRippleClickable { isRepsInFocus = true }
                    .padding(horizontal = 8.dp)
            )

            if (isRepsInFocus) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.LightGray)
                        .padding(horizontal =  2.dp, vertical = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    RangeSelectionTextField(
                        set.reps,
                        textStyle = MaterialTheme.typography.bodySmallLight,
                        onRangeChanged = { v -> onUpdateReps(v)},
                        modifier = Modifier,
                        onClearFocus = {isRepsInFocus = false}
                    )
                }
            }
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
                value = set.weight,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number ),
                onValueChange = { v ->
                    val numericValue = v.filter { char -> char.isDigit() }.trimStart { char -> char == '0' }
                    onUpdateWeight(numericValue)
                },
                textStyle = MaterialTheme.typography.bodySmallLight.copy(
                    color = if (isLastSet && !lastSetApplied) Color.White.copy(0.5f) else Color.White
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 6.dp).onFocusChanged { if(it.isFocused) isRepsInFocus = false }
            )

            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = set.unit,
                style = MaterialTheme.typography.bodySmallLight.copy(
                    color = if (isLastSet && !lastSetApplied) Color.White.copy(0.5f) else Color.White
                ),
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
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number ),
                onValueChange = { v ->
                    val numericValue = v.filter { char -> char.isDigit() }.trimStart { char -> char == '0' }
                    onUpdateRestSeconds(numericValue)
                },
                textStyle = MaterialTheme.typography.bodySmallLight.copy(
                    color = if (isLastSet && !lastSetApplied) Color.White.copy(0.5f) else Color.White
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 6.dp).onFocusChanged { if(it.isFocused) isRepsInFocus = false }
            )

            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "sec",
                style = MaterialTheme.typography.bodySmallLight.copy(
                    color = if (isLastSet && !lastSetApplied) Color.White.copy(0.5f) else Color.White
                ),
                modifier = Modifier
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Box(modifier = Modifier
            .noRippleClickable {
                onIconClicked()
            }
            .width(24.dp)) {
            Icon(
                painterResource(id = if (isLastSet.not()) R.drawable.ic_remove_circle else R.drawable.ic_add_circle),
                tint = if (isLastSet.not()) Color.Red else PrimaryTurq,
                modifier = Modifier
                    .padding(4.dp)
                    .size(16.dp)
                    .align(Alignment.Center),
                contentDescription = "set action"
            )

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
                .weight(1.2f),
            style = MaterialTheme.typography.bodySmallLight.copy(fontWeight = FontWeight.Medium)
        )

        Text(
            text = "reps",
            maxLines = 1,
            modifier = Modifier
                .padding(horizontal = 3.dp)
                .weight(1.2f),
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
fun SetTypeDropdown(
    selectedType: String,
    onTypeSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.White
) {
    var expanded by remember { mutableStateOf(false) }
    // Generate month names using LocalDate and the default locale
    val types = listOf("warmup", "working", "failure")

    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = selectedType,
                style = MaterialTheme.typography.bodySmallLight.copy(color = color),
                modifier = Modifier
                    .noRippleClickable { expanded = true }
                    .padding(end = 4.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_dropdown),
                contentDescription = "type dropdown",
                modifier = Modifier.size(16.dp),
                tint = color
            )
        }

        // Dropdown menu that shows when expanded is true
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .height(160.dp)  // Adjust height to fit the UI design
                .padding(top = 4.dp)
        ) {
            types.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type, style = MaterialTheme.typography.bodySmall) },
                    onClick = {
                        onTypeSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
@Preview
fun PreviewAddExercise() = BasePreviewContainer {
    AddExercise(exercise = CreateWorkoutDm.Exercise.MOCK)
}