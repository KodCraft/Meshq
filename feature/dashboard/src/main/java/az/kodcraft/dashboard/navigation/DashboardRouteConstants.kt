package az.kodcraft.dashboard.navigation

import androidx.navigation.NavController
import az.kodcraft.dashboard.navigation.DashboardRouteConstants.DASHBOARD_SCREEN

object DashboardRouteConstants {
    const val DASHBOARD_SCREEN = "dashboard_screen"
}

fun NavController.navigateToDashboard() {
    popBackStack(route = DASHBOARD_SCREEN, inclusive = false)
}