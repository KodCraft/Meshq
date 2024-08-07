package az.kodcraft.workout.presentation.workoutDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.kodcraft.core.presentation.bases.BasePreviewContainer
import az.kodcraft.core.presentation.composable.appBar.TopAppBar
import az.kodcraft.core.presentation.composable.button.ButtonSecondary
import az.kodcraft.core.presentation.theme.PrimaryBlue
import az.kodcraft.core.presentation.theme.PrimaryTurq
import az.kodcraft.core.presentation.theme.body
import az.kodcraft.core.presentation.theme.bodySmallLight
import az.kodcraft.core.presentation.theme.mediumTitle
import az.kodcraft.core.presentation.theme.primaryTurq
import az.kodcraft.core.utils.formatDateToStringDayAndMonth
import az.kodcraft.core.utils.noRippleClickable
import az.kodcraft.workout.R
import az.kodcraft.workout.domain.model.AssignedWorkoutDm
import az.kodcraft.workout.presentation.workoutDetails.composable.CardTabs
import az.kodcraft.workout.presentation.workoutDetails.composable.WorkoutTab
import az.kodcraft.workout.presentation.workoutDetails.contract.WorkoutDetailsIntent
import az.kodcraft.workout.presentation.workoutDetails.contract.WorkoutDetailsUiState
import az.kodcraft.workout.presentation.workoutProgress.composable.CompleteButton

@Composable
fun WorkoutDetailsRoute(
    viewModel: WorkoutDetailsViewModel = hiltViewModel(),
    navigateToWorkoutProgress: (id: String) -> Unit,
    navigateBack: () -> Unit,
    workoutId: String
) {
    LaunchedEffect(workoutId) {
        viewModel.acceptIntent(WorkoutDetailsIntent.GetWorkoutData(workoutId = workoutId))
    }

    val uiState by viewModel.uiState.collectAsState()
    if (uiState.isLoading)
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                color = PrimaryTurq,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    else
        WorkoutDetailsScreen(
            uiState = uiState,
            onStartWorkoutClicked = { navigateToWorkoutProgress(uiState.workout.id) },
            navigateBack = navigateBack
        )
}


@Composable
fun WorkoutDetailsScreen(
    uiState: WorkoutDetailsUiState,
    onStartWorkoutClicked: () -> Unit,
    navigateBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryTurq)
    ) {
        TopAppBar(
            showBackIcon = true,
            iconsColor = MaterialTheme.colorScheme.background,
            onBackClick = navigateBack,
            content = {
                Spacer(modifier = Modifier.weight(1f))
//                if (uiState.workout.isFinished) StartOverButton(
//                    onClick = {},
//                    buttonColor = Color.Red.copy(0.6f)
//                )
//                Spacer(modifier = Modifier.width(6.dp))
                CompleteButton(
                    onClick = {},
                    buttonColor = PrimaryBlue.copy(0.6f),
                    isFinished = uiState.workout.isFinished
                )
               // if (uiState.workout.isFinished.not()) {
                    Spacer(modifier = Modifier.width(12.dp))
                    WorkoutDate(uiState.workout.date.formatDateToStringDayAndMonth())
              //  }
            }
        )
        Column(
            Modifier
                .fillMaxSize()
        ) {

            Text(
                modifier = Modifier.padding(24.dp),
                text = uiState.workout.title,
                style = MaterialTheme.typography.mediumTitle.copy(color = PrimaryBlue)
            )
            WorkoutContentCard(
                modifier = Modifier.weight(1f),
                workout = uiState.workout,
                onStartWorkoutClicked = onStartWorkoutClicked
            )
        }

    }
}

@Composable
fun WorkoutContentCard(modifier: Modifier, workout: AssignedWorkoutDm, onStartWorkoutClicked: () -> Unit) {
    var selectedTab by remember { mutableStateOf(WorkoutTab.Program) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(MaterialTheme.colorScheme.background)
    ) {
        CardTabs(selectedTab, onTabCLicked = { selectedTab = it })
        CardContent(selectedTab, workout)
        Spacer(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
        ButtonSecondary(
            text = if (workout.isFinished) stringResource(R.string.workout_details_screen_btn_view_workout) else stringResource(
                R.string.workout_details_screen_btn_start_workout
            ),
            modifier = Modifier
                .noRippleClickable { onStartWorkoutClicked() }
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(45.dp)
        )
    }
}

@Composable
fun CardContent(selectedTab: WorkoutTab, workout: AssignedWorkoutDm) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(28.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (selectedTab == WorkoutTab.Program) {
            workout.exercises.forEach {
                Text(
                    text = it.toString(),
                    style = MaterialTheme.typography.bodySmallLight
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        } else {
            Text(
                text = workout.notes,
                style = MaterialTheme.typography.bodySmallLight
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun WorkoutDate(date: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = az.kodcraft.core.R.drawable.ic_calendar),
            contentDescription = "calendar",
            tint = Color.Black,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = date, style = MaterialTheme.typography.body.copy(color = Color.Black))
    }
}


@Preview
@Composable
fun WorkoutDetailsPreview() = BasePreviewContainer {
    WorkoutDetailsScreen(
        uiState = WorkoutDetailsUiState(workout = AssignedWorkoutDm.MOCK.copy(isFinished = true)),
        onStartWorkoutClicked = { /*TODO*/ }) {

    }
}
