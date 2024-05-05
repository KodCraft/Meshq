package az.kodcraft.dashboard.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import az.kodcraft.core.navigation.TopLevelDestination
import az.kodcraft.dashboard.presentation.DashboardRoute

fun NavGraphBuilder.dashboardGraph(
    padding:PaddingValues,
    navigateToWorkoutDetails: (id: String) -> Unit
) {
    composable(route = TopLevelDestination.DASHBOARD.route) {
        DashboardRoute(padding = padding, navigateToWorkoutDetails = navigateToWorkoutDetails)
    }
    composable(route = TopLevelDestination.FINISHED_WORKOUTS.route) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { Text("UNDER CONSTRUCTION ... ") }
    }
    composable(route = TopLevelDestination.EXERCISE_LIBRARY.route) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { Text("UNDER CONSTRUCTION ... ") }
    }
}