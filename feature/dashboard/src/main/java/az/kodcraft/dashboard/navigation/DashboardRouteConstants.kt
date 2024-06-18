package az.kodcraft.dashboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions


object DashboardRouteConstants{
    const val DASHBOARD = "DASHBOARD"
    const val TRAINER_DASHBOARD = "TRAINER_DASHBOARD"
}

fun NavController.navigateToDashboard(navOptions:NavOptions? = null) {
    navigate(route = DashboardRouteConstants.DASHBOARD, navOptions = navOptions)
}

fun NavController.navigateToTrainerDashboard(navOptions:NavOptions? = null) {
    navigate(route = DashboardRouteConstants.TRAINER_DASHBOARD, navOptions = navOptions)
}
