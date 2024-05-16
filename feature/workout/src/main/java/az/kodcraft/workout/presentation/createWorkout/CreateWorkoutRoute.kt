package az.kodcraft.workout.presentation.createWorkout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.kodcraft.core.R
import az.kodcraft.core.presentation.bases.BasePreviewContainer
import az.kodcraft.core.presentation.composable.appBar.TopAppBar
import az.kodcraft.core.presentation.composable.button.ButtonPrimaryLight
import az.kodcraft.core.presentation.composable.dropdown.DropdownItem
import az.kodcraft.core.presentation.composable.textField.DropdownTextField
import az.kodcraft.core.presentation.theme.PrimaryBlue
import az.kodcraft.core.presentation.theme.mediumTitle
import az.kodcraft.core.utils.noRippleClickable
import az.kodcraft.workout.domain.model.CreateWorkoutDm
import az.kodcraft.workout.presentation.createWorkout.composable.AddExercise
import az.kodcraft.workout.presentation.createWorkout.composable.ExerciseSetsCard
import az.kodcraft.workout.presentation.createWorkout.contract.CreateWorkoutIntent
import az.kodcraft.workout.presentation.createWorkout.contract.CreateWorkoutUiState

@Composable
fun CreateWorkoutRoute(
    viewModel: CreateWorkoutViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    CreateWorkoutScreen(
        uiState = uiState,
        onSaveWorkoutClicked = { },
        onSearchValueChanged = { viewModel.acceptIntent(CreateWorkoutIntent.ChangeSearchValue(it)) },
        navigateBack = navigateBack,
        onExerciseSelected = {
            viewModel.acceptIntent(
                CreateWorkoutIntent.NewExerciseSelected(
                    id = it.id,
                    name = it.name
                )
            )
        },
        onExerciseUnSelected = { viewModel.acceptIntent(CreateWorkoutIntent.UnselectExercise) },
        onSaveExerciseSets = { viewModel.acceptIntent(CreateWorkoutIntent.SaveExerciseSets(it)) }
    )
}


@Composable
fun CreateWorkoutScreen(
    uiState: CreateWorkoutUiState,
    onSaveWorkoutClicked: () -> Unit = {},
    onSaveExerciseSets: (List<CreateWorkoutDm.Exercise.Set>) -> Unit = {},
    onExerciseSelected: (DropdownItem) -> Unit = {},
    onExerciseUnSelected: () -> Unit = {},
    onSearchValueChanged: (String) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
    Column(
        Modifier
            .fillMaxSize()
    ) {
        TopAppBar(
            content = {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_close), contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.noRippleClickable { navigateBack() }//TODO: show warning popup before exit
                )
            }
        )
        Column(
            Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight(0.7f)
                    .fillMaxWidth()
            ) {
                items(uiState.workout.exercises) {
                    Column(Modifier.padding(22.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                it.name,
                                style = MaterialTheme.typography.mediumTitle,

                                )
                            Icon(
                                painter = painterResource(id = R.drawable.ic_remove_circle),
                                contentDescription = "",
                                tint = Color.Red.copy(0.7f),
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(18.dp))
                        ExerciseSetsCard(it.sets)
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            // Filtered list based on the text
            DropdownTextField(
                modifier = Modifier
                    .padding(horizontal = 60.dp),
                value = uiState.searchValue,
                onValueChange = onSearchValueChanged,
                list = uiState.exercises.map { DropdownItem(it.name, it.id) },
                placeholder = "Exercise",
                isLoading = uiState.isLoading,
                onItemSelected = { onExerciseSelected(it) }
            )

            Spacer(modifier = Modifier.height(50.dp))
            if (uiState.workout.exercises.any()) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)) {
                    ButtonPrimaryLight(
                        text = "Save Workout",
                        modifier = Modifier.align(Alignment.Center).width(300.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }


    AnimatedVisibility(
        visible = uiState.selectedExercise != CreateWorkoutDm.Exercise.EMPTY,
        enter = fadeIn() + slideInHorizontally(initialOffsetX = { it }),
        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { it })
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(PrimaryBlue), verticalArrangement = Arrangement.Center
        ) {
            AddExercise(
                modifier = Modifier.padding(horizontal = 28.dp),
                onDismiss = { onExerciseUnSelected() },
                exercise = uiState.workout.exercises.firstOrNull { it.exerciseRefId == uiState.selectedExercise.id }
                    ?: CreateWorkoutDm.Exercise.EMPTY.copy(
                        exerciseRefId = uiState.selectedExercise.id,
                        name = uiState.selectedExercise.name
                    ),
                onSaveExerciseSets = onSaveExerciseSets
            )
        }
    }

}


@Preview
@Composable
fun CreateWorkoutPreview() = BasePreviewContainer {
    CreateWorkoutScreen(
        uiState = CreateWorkoutUiState(workout = CreateWorkoutDm.MOCK)
    )
}

