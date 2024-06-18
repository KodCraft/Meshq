package az.kodcraft.dashboard.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import az.kodcraft.core.navigation.TopLevelDestinationTrainee
import az.kodcraft.core.navigation.TopLevelDestinationTrainer
import az.kodcraft.dashboard.presentation.traineeDashboard.DashboardRoute
import az.kodcraft.dashboard.presentation.trainerDashboard.TrainerDashboardRoute

fun NavGraphBuilder.dashboardGraph(
    padding: PaddingValues,
    navigateToWorkoutDetails: (id: String) -> Unit,
    navigateToCreateWorkout: () -> Unit,
    switchMode: () -> Unit,
    onMenuClick: () -> Unit
) {

    //Trainee module
    composable(route = TopLevelDestinationTrainee.DASHBOARD.route) {
        DashboardRoute(
            padding = padding,
            navigateToWorkoutDetails = navigateToWorkoutDetails,
            switchMode = switchMode,
            onMenuClick = onMenuClick
        )
    }
    composable(route = TopLevelDestinationTrainee.FINISHED_WORKOUTS.route) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { Text("FINISHED WORKOUTS LIST UNDER CONSTRUCTION ... ", textAlign = TextAlign.Center) }
    }
    composable(route = TopLevelDestinationTrainee.EXERCISE_LIBRARY.route) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { Text("EXERCISE LIBRARY UNDER CONSTRUCTION ... ", textAlign = TextAlign.Center) }
    }

    //Trainer module
    composable(route = TopLevelDestinationTrainer.TRAINER_DASHBOARD.route) {
        TrainerDashboardRoute(
            onNavigateToCreateWorkout = navigateToCreateWorkout,
            switchMode = switchMode,
            onMenuClick = onMenuClick
        )
    }

    composable(route = TopLevelDestinationTrainer.WORKOUTS_LIBRARY.route) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { Text("WORKOUT LIBRARY UNDER CONSTRUCTION ... ", textAlign = TextAlign.Center) }
    }
    composable(route = TopLevelDestinationTrainer.EXERCISE_LIBRARY.route) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { Text("TRAINER EXERCISE LIBRARY UNDER CONSTRUCTION ... ", textAlign = TextAlign.Center) }
    }

    composable(route = TopLevelDestinationTrainer.CLIENTS_LIST.route) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { Text("CLIENTS LIST UNDER CONSTRUCTION ... ", textAlign = TextAlign.Center) }
    }
}