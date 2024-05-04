package az.kodcraft.workout.presentation.workoutProgress

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import az.kodcraft.core.presentation.bases.BasePreviewContainer
import az.kodcraft.core.presentation.composable.appBar.TopAppBar
import az.kodcraft.core.presentation.composable.button.ButtonPrimary
import az.kodcraft.core.presentation.theme.AccentBlue
import az.kodcraft.core.presentation.theme.PrimaryTurq
import az.kodcraft.core.presentation.theme.body
import az.kodcraft.core.presentation.theme.largeTitle
import az.kodcraft.core.utils.collectWithLifecycle
import az.kodcraft.core.utils.noRippleClickable
import az.kodcraft.workout.R
import az.kodcraft.workout.domain.model.WorkoutDm
import az.kodcraft.workout.presentation.workoutProgress.composable.CompleteButton
import az.kodcraft.workout.presentation.workoutProgress.composable.ExerciseDropDownCard
import az.kodcraft.workout.presentation.workoutProgress.composable.ExercisePreviewCard
import az.kodcraft.workout.presentation.workoutProgress.composable.StartOverButton
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
    if (uiState.isLoading) Box(Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            color = PrimaryTurq, modifier = Modifier.align(Alignment.Center)
        )
    }
    else WorkoutProgressScreen(navigateBack, uiState, onIntent = viewModel::acceptIntent)
}

@Composable
fun WorkoutProgressScreen(
    navigateBack: () -> Unit,
    uiState: WorkoutProgressUiState,
    onIntent: (WorkoutProgressIntent) -> Unit = {}
) {
    var showFinishWarningPopup by remember { mutableStateOf(false) }
    var showStartOverWarningPopup by remember { mutableStateOf(false) }

    if (showFinishWarningPopup) {
        Dialog(onDismissRequest = { showFinishWarningPopup = false }) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(AccentBlue)
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(6.dp),
                    text = "Are you sure ou want to finish and exit the workout? The entered data will be saved",
                    style = MaterialTheme.typography.body
                )
                Spacer(Modifier.height(16.dp))
                ButtonPrimary(
                    text = "Finish",
                    modifier = Modifier
                        .fillMaxWidth()
                        .noRippleClickable { onIntent.invoke(WorkoutProgressIntent.SaveFinishWorkout) })
            }
        }
    }

    if (showStartOverWarningPopup) {
        Dialog(onDismissRequest = { showStartOverWarningPopup = false }) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(AccentBlue)
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(6.dp),
                    text = "Are you sure you want to start the workout over ? The workout progress will be reset",
                    style = MaterialTheme.typography.body
                )
                Spacer(Modifier.height(16.dp))
                ButtonPrimary(
                    text = "Reset",
                    color = Color.Red.copy(0.7f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .noRippleClickable {
                            onIntent.invoke(WorkoutProgressIntent.ResetTheWorkout)
                            showStartOverWarningPopup = false
                        })
            }
        }
    }
    Column(
        Modifier.fillMaxSize()
    ) {
        TopAppBar(showBackIcon = true,
            iconsColor = MaterialTheme.colorScheme.onBackground,
            onBackClick = navigateBack,
            content = {
                Spacer(modifier = Modifier.weight(1f))
                if (uiState.workout.isFinished) StartOverButton(
                    onClick = { showStartOverWarningPopup = true },
                    buttonColor = Color.Red.copy(0.6f)
                )
                Spacer(modifier = Modifier.width(6.dp))
                CompleteButton(
                    onClick = { showFinishWarningPopup = true },
                    buttonColor = PrimaryTurq.copy(0.6f),
                    isFinished = uiState.workout.isFinished
                )
            })

        Column(
            Modifier.fillMaxSize()
        ) {

            Text(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp),
                text = uiState.workout.title,
                style = MaterialTheme.typography.largeTitle
            )
            Spacer(modifier = Modifier.height(24.dp))
            WorkoutProgressContent(workout = uiState.workout,
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
                onWorkoutFinish = { onIntent.invoke(WorkoutProgressIntent.SaveWorkoutProgress) },
                onToggleExerciseSetStatus = { eid, sid ->
                    onIntent.invoke(
                        WorkoutProgressIntent.ChangeExerciseSetStatus(
                            eid, sid
                        )
                    )
                },
                onWeightValueChange = { value, sid ->
                    onIntent.invoke(
                        WorkoutProgressIntent.ChangeWeightValueForSet(
                            value, sid
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
    onWeightValueChange: (value: String, setId: String) -> Unit,
) {
    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 24.dp, bottom = 80.dp)
        ) {
            items(workout.exercises) {
                if (it.isInPreviewMode) {
                    ExercisePreviewCard(
                        isEditable = workout.isFinished.not(),
                        exercise = it,
                        isCompleteClick = { exerciseCompleteClick(it.id) },
                        onClick = { exerciseClick(it.id) },
                        onToggleExerciseSetStatus = onToggleExerciseSetStatus,
                        onWeightValueChange = onWeightValueChange,
                    )
                } else {
                    ExerciseDropDownCard(exercise = it,
                        isCompleteClick = { if(workout.isFinished.not()) exerciseCompleteClick(it.id) },
                        onClick = { exerciseClick(it.id) })
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

        }
        if (!workout.isFinished) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, end = 32.dp, bottom = 24.dp)
                    .align(Alignment.BottomCenter)
            ) {
                ButtonPrimary(
                    text = stringResource(R.string.workout_progress_screen_btn_finish),
                    modifier = Modifier
                        .noRippleClickable { onWorkoutFinish() }
                        .fillMaxWidth()
                )
            }

        }
    }

}


@Preview()
@Composable
fun PreviewExercisePreviewCard() = BasePreviewContainer {
    WorkoutProgressScreen(
        navigateBack = {},
        uiState = WorkoutProgressUiState(workout = WorkoutDm.MOCK.copy(isFinished = true))
    )
}





