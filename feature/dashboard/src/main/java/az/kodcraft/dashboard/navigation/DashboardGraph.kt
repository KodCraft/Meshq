package az.kodcraft.dashboard.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import az.kodcraft.dashboard.presentation.DashboardRoute

fun NavGraphBuilder.dashboardGraph(
    navigateToWorkoutDetails:(id: String)-> Unit
) {
    composable(route = DashboardRouteConstants.DASHBOARD_SCREEN) {
        DashboardRoute(navigateToWorkoutDetails = navigateToWorkoutDetails)
    }
}