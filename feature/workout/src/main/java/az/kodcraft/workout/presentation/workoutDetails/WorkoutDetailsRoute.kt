package az.kodcraft.workout.presentation.workoutDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.kodcraft.core.presentation.composable.appBar.TopAppBar
import az.kodcraft.core.presentation.composable.button.ButtonSecondary
import az.kodcraft.core.presentation.theme.body
import az.kodcraft.core.presentation.theme.largeTitle
import az.kodcraft.core.presentation.theme.primaryTurq
import az.kodcraft.workout.R
import az.kodcraft.workout.domain.model.WorkoutDm
import az.kodcraft.workout.presentation.workoutDetails.composable.CardTabs
import az.kodcraft.workout.presentation.workoutDetails.composable.WorkoutTab
import az.kodcraft.workout.presentation.workoutDetails.contract.WorkoutDetailsUiState

@Composable
fun WorkoutDetailsRoute(
    viewModel: WorkoutDetailsViewModel = hiltViewModel(),
    navigateToWorkoutProgress: (id: String) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    WorkoutDetailsScreen(
        uiState = uiState,
        onStartWorkoutClicked = { navigateToWorkoutProgress(uiState.workout.id) })
}


@Composable
fun WorkoutDetailsScreen(uiState: WorkoutDetailsUiState, onStartWorkoutClicked: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryTurq)
    ) {
        TopAppBar(showBackIcon = true, iconsColor = MaterialTheme.colorScheme.background)
        Column(
            Modifier
                .fillMaxSize()
        ) {

            Text(
                modifier = Modifier.padding(24.dp),
                text = uiState.workout.title,
                style = MaterialTheme.typography.largeTitle
            )
            Spacer(modifier = Modifier.height(24.dp))
            WorkoutContentCard(
                modifier = Modifier.weight(1f),
                workout = uiState.workout,
                onStartWorkoutClicked = onStartWorkoutClicked
            )
        }

    }
}

@Composable
fun WorkoutContentCard(modifier: Modifier, workout: WorkoutDm, onStartWorkoutClicked: () -> Unit) {
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
            text = stringResource(R.string.workout_details_screen_start_workout_button),
            modifier = Modifier
                .clickable { onStartWorkoutClicked() }
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(45.dp)
        )
    }
}

@Composable
fun CardContent(selectedTab: WorkoutTab, workout: WorkoutDm) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(28.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (selectedTab == WorkoutTab.Program) {
            Text(
                text = workout.content,
                style = MaterialTheme.typography.body.copy(Color.White)
            )
        }
    }
}

