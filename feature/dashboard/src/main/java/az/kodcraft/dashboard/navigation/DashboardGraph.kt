package az.kodcraft.dashboard.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import az.kodcraft.dashboard.navigation.DashboardRouteConstants.DASHBOARD_SCREEN
import az.kodcraft.dashboard.navigation.DashboardRouteConstants.TRAINER_DASHBOARD_SCREEN
import az.kodcraft.dashboard.presentation.traineeDashboard.DashboardRoute
import az.kodcraft.dashboard.presentation.trainerDashboard.TrainerDashboardRoute

fun NavGraphBuilder.dashboardGraph(
    padding: PaddingValues,
    navigateToWorkoutDetails: (id: String) -> Unit,
    navigateToCreateWorkout: () -> Unit,
    navigateToNotifications: () -> Unit,
    switchMode: () -> Unit,
    onMenuClick: () -> Unit
) {

    //Trainee module
    composable(route = DASHBOARD_SCREEN) {
        DashboardRoute(
            padding = padding,
            navigateToWorkoutDetails = navigateToWorkoutDetails,
            switchMode = switchMode
        )
    }

//    composable(route = EXERCISE_LIBRARY.route) {
//        Box(
//            Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) { Text("EXERCISE LIBRARY UNDER CONSTRUCTION ... ", textAlign = TextAlign.Center) }
//    }

    //Trainer module
    composable(route = TRAINER_DASHBOARD_SCREEN) {
        TrainerDashboardRoute(
            onNavigateToCreateWorkout = navigateToCreateWorkout,
            onNavigateToNotifications = navigateToNotifications,
            switchMode = switchMode,
            onMenuClick = onMenuClick
        )
    }


//    composable(route = EXERCISE_LIBRARY.route) {
//        Box(
//            Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) { Text("TRAINER EXERCISE LIBRARY UNDER CONSTRUCTION ... ", textAlign = TextAlign.Center) }
//    }
}