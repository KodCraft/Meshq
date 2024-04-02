package az.kodcraft.meshq.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import az.kodcraft.dashboard.navigation.dashboardGraph
import az.kodcraft.onboarding.navigation.OnBoardingRouteConstants
import az.kodcraft.onboarding.navigation.onBoardingGraph
import az.kodcraft.workout.navigation.WorkoutRouteConstants
import az.kodcraft.workout.navigation.workoutGraph

@Composable
fun MeshqNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
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
            navController = navController,
//            changeTopAndBottomState = { _, _ -> },
//            navigateToLogin = navController::navigateToLogin,
//            navigateToHome = navController::navigateToHome
        )

        dashboardGraph(
//            changeTopAndBottomState = { _, _ -> },
            navigateToWorkoutDetails = {navController.navigate(WorkoutRouteConstants.WORKOUT_DETAILS_SCREEN)},
//            navigateToProjects = navController::navigateToProjects,
//            navigateToProjectDetails = navController::navigateToProjectDetails,
//            navigateToNotification = navController::navigateToNotification,
//            onNavigateToCampaignDetails = navController::navigateToCampaignDetails,
//            onNavigateToSearch = navController::navigateToSearch,
//            onNavigateToLogin = navController::navigateToLogin
        )

        workoutGraph(navigateToWorkoutProgress= {navController.navigate(WorkoutRouteConstants.WORKOUT_PROGRESS_SCREEN)})
    }
}