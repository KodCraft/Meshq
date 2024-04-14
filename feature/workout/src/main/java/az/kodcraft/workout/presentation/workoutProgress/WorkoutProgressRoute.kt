package az.kodcraft.workout.presentation.workoutProgress

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.kodcraft.core.presentation.composable.appBar.TopAppBar
import az.kodcraft.core.presentation.composable.button.ButtonPrimary
import az.kodcraft.core.presentation.theme.PrimaryTurq
import az.kodcraft.core.presentation.theme.largeTitle
import az.kodcraft.core.utils.collectWithLifecycle
import az.kodcraft.core.utils.noRippleClickable
import az.kodcraft.workout.R
import az.kodcraft.workout.domain.model.WorkoutDm
import az.kodcraft.workout.presentation.workoutDetails.CompleteButton
import az.kodcraft.workout.presentation.workoutProgress.composable.ExerciseDropDownCard
import az.kodcraft.workout.presentation.workoutProgress.composable.ExercisePreviewCard
import az.kodcraft.workout.presentation.workoutProgress.contract.WorkoutProgressEvent
import az.kodcraft.workout.presentation.workoutProgress.contract.WorkoutProgressIntent
import az.kodcraft.workout.presentation.workoutProgress.contract.WorkoutProgressUiState

@Composable
fun WorkoutProgressRoute(
    viewModel: WorkoutProgressViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateHome: () -> Unit,
    workoutId: String
) {
    LaunchedEffect(workoutId) {
        viewModel.acceptIntent(WorkoutProgressIntent.GetWorkoutData(workoutId = workoutId))
    }

    viewModel.event.collectWithLifecycle {
        when (it) {
            WorkoutProgressEvent.NavigateHome -> navigateHome()
        }
    }

    val uiState by viewModel.uiState.collectAsState()
    if (uiState.isLoading)
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(color = PrimaryTurq, modifier = Modifier.align(Alignment.Center))
        }
    else
    WorkoutProgressScreen(navigateBack, uiState, onIntent = viewModel::acceptIntent)
}

@Composable
fun WorkoutProgressScreen(
    navigateBack: () -> Unit,
    uiState: WorkoutProgressUiState,
    onIntent: (WorkoutProgressIntent) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
    ) {
        TopAppBar(showBackIcon = true,
            iconsColor = MaterialTheme.colorScheme.onBackground,
            onBackClick = navigateBack,
            content = {
                Spacer(modifier = Modifier.weight(1f))
                CompleteButton(
                    onClick = { onIntent.invoke(WorkoutProgressIntent.CompleteWorkout) },
                    buttonColor = PrimaryTurq.copy(0.6f),
                    isFinished = uiState.workout.isFinished
                )
                Spacer(modifier = Modifier.width(12.dp))
            })

        Column(
            Modifier
                .fillMaxSize()
        ) {

            Text(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp),
                text = uiState.workout.title,
                style = MaterialTheme.typography.largeTitle
            )
            Spacer(modifier = Modifier.height(24.dp))
            WorkoutProgressContent(
                workout = uiState.workout,
                exerciseCompleteClick = {
                    onIntent.invoke(
                        WorkoutProgressIntent.ChangeExerciseStatus(it)
                    )
                },
                exerciseClick = {
                    onIntent.invoke(
                        WorkoutProgressIntent.ToggleExercisePreview(it)
                    )
                },
                onWorkoutFinish = { onIntent.invoke(WorkoutProgressIntent.FinishWorkout) },
                onToggleExerciseSetStatus = { eid, sid ->
                    onIntent.invoke(
                        WorkoutProgressIntent.ChangeExerciseSetStatus(
                            eid,
                            sid
                        )
                    )
                })

            Spacer(Modifier.height(50.dp))
        }
    }

}

@Composable
fun WorkoutProgressContent(
    workout: WorkoutDm,
    exerciseCompleteClick: (String) -> Unit,
    exerciseClick: (String) -> Unit,
    onWorkoutFinish: () -> Unit,
    onToggleExerciseSetStatus: (exerciseId: String, setId: String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 24.dp)
    ) {
        items(workout.exercises) {
            if (it.isInPreviewMode) {
                ExercisePreviewCard(
                    exercise = it,
                    isCompleteClick = { exerciseCompleteClick(it.id) },
                    onClick = { exerciseClick(it.id) },
                    onToggleExerciseSetStatus = onToggleExerciseSetStatus
                )
            } else {
                ExerciseDropDownCard(exercise = it,
                    isCompleteClick = { exerciseCompleteClick(it.id) },
                    onClick = { exerciseClick(it.id) })
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
        if (workout.isComplete()) {
            item {
                Spacer(Modifier.height(32.dp))
                ButtonPrimary(text = stringResource(R.string.workout_progress_screen_btn_finish),
                    modifier = Modifier
                        .noRippleClickable { onWorkoutFinish() }
                        .fillMaxWidth())
            }
        }
    }
}





