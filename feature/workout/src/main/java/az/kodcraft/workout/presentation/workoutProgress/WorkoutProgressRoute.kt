package az.kodcraft.workout.presentation.workoutProgress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.kodcraft.core.R
import az.kodcraft.core.presentation.composable.appBar.TopAppBar
import az.kodcraft.core.presentation.composable.button.ButtonSecondary
import az.kodcraft.core.presentation.theme.PrimaryTurq
import az.kodcraft.core.presentation.theme.body
import az.kodcraft.core.presentation.theme.largeTitle
import az.kodcraft.core.utils.noRippleClickable
import az.kodcraft.workout.domain.model.WorkoutDm
import az.kodcraft.workout.presentation.workoutDetails.CompleteButton
import az.kodcraft.workout.presentation.workoutProgress.contract.WorkoutProgressIntent
import az.kodcraft.workout.presentation.workoutProgress.contract.WorkoutProgressUiState

@Composable
fun WorkoutProgressRoute(
    viewModel: WorkoutProgressViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    workoutId: String
) {
    LaunchedEffect(workoutId) {
        viewModel.acceptIntent(WorkoutProgressIntent.GetWorkoutData(workoutId = workoutId))
    }

    val uiState by viewModel.uiState.collectAsState()
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
                    PrimaryTurq.copy(0.6f)
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
                onWorkoutFinish = { onIntent.invoke(WorkoutProgressIntent.FinishWorkout) })

            Spacer(Modifier.height(50.dp))
        }
    }

}

@Composable
fun WorkoutProgressContent(
    workout: WorkoutDm,
    exerciseCompleteClick: (String) -> Unit,
    onWorkoutFinish: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 24.dp)
    ) {
        items(workout.exercises) {
            ExerciseDropDownCard(exercise = it, isCompleteClick = { exerciseCompleteClick(it.id) })
            Spacer(modifier = Modifier.height(24.dp))
        }
        if (workout.isComplete()) {
            item {

                Spacer(Modifier.height(16.dp))
                ButtonSecondary(text = stringResource(az.kodcraft.workout.R.string.workout_progress_screen_btn_finish),
                    modifier = Modifier
                        .noRippleClickable { onWorkoutFinish() }
                        .fillMaxWidth()
                        .padding(16.dp))
            }
        }
    }
}

@Composable
fun ExerciseDropDownCard(exercise: WorkoutDm.Exercise, isCompleteClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val isExerciseComplete = exercise.isComplete()
        val isExerciseCurrent = exercise.isCurrent
        Row(
            modifier = Modifier
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
                style = MaterialTheme.typography.body
            )
            Icon(
                painter = painterResource(id = com.google.android.material.R.drawable.mtrl_ic_arrow_drop_down),
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


