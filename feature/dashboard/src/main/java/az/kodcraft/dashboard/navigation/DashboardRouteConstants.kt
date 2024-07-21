package az.kodcraft.dashboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions


object DashboardRouteConstants{
    const val DASHBOARD_SCREEN = "DASHBOARD_SCREEN"
    const val TRAINER_DASHBOARD_SCREEN = "TRAINER_DASHBOARD_SCREEN"
}

fun NavController.navigateToDashboard(navOptions:NavOptions? = null) {
    navigate(route = DashboardRouteConstants.DASHBOARD_SCREEN, navOptions = navOptions)
}

fun NavController.navigateToTrainerDashboard(navOptions:NavOptions? = null) {
    navigate(route = DashboardRouteConstants.TRAINER_DASHBOARD_SCREEN, navOptions = navOptions)
}
