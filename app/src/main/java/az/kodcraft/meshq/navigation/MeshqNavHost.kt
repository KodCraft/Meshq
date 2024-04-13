package az.kodcraft.meshq.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import az.kodcraft.dashboard.navigation.dashboardGraph
import az.kodcraft.dashboard.navigation.navigateToDashboard
import az.kodcraft.onboarding.navigation.OnBoardingRouteConstants
import az.kodcraft.onboarding.navigation.onBoardingGraph
import az.kodcraft.workout.navigation.navigateToWorkoutDetails
import az.kodcraft.workout.navigation.navigateToWorkoutProgress
import az.kodcraft.workout.navigation.workoutGraph

@Composable
fun MeshqNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    padding: PaddingValues = PaddingValues(0.dp),
    startDestination: String = OnBoardingRouteConstants.SPLASH_SCREEN
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        route = NavGraphConstants.ROOT_GRAPH
    )
    {
        onBoardingGraph(
            navController = navController
        )

        dashboardGraph(
            navigateToWorkoutDetails = navController::navigateToWorkoutDetails
        )

        workoutGraph(
            navigateToWorkoutProgress = navController::navigateToWorkoutProgress,
            navigateBack = { navController.popBackStack() },
            navigateHome = { navController.navigateToDashboard() })
    }
}