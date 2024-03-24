package az.kodcraft.dashboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import az.kodcraft.dashboard.presentation.DashboardRoute

fun NavGraphBuilder.dashboardGraph(
    navController: NavController
) {
    composable(route = DashboardRouteConstants.DASHBOARD_SCREEN) {
        DashboardRoute()
    }
}