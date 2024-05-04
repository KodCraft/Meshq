package az.kodcraft.core.navigation

import androidx.annotation.DrawableRes
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.navOptions

object NavGraphConstants {
    const val ROOT_GRAPH = "root_graph"
}

enum class TopLevelDestination(
    val route: String,
    @DrawableRes val selectedIconId: Int,
    @DrawableRes val unselectedIcon: Int,
) {
    FINISHED_WORKOUTS(
        route = "FINISHED_WORKOUTS",
        unselectedIcon = az.kodcraft.core.R.drawable.ic_checked_list,
        selectedIconId = az.kodcraft.core.R.drawable.ic_checked_list,
    ),
    DASHBOARD(
        route = "DASHBOARD",
        unselectedIcon = az.kodcraft.core.R.drawable.ic_dashboard,
        selectedIconId = az.kodcraft.core.R.drawable.ic_dashboard,
    ),
    EXERCISE_LIBRARY(
        route = "EXERCISE_LIBRARY",
        unselectedIcon = az.kodcraft.core.R.drawable.ic_dumbbell,
        selectedIconId = az.kodcraft.core.R.drawable.ic_dumbbell,
    ),
}
val NavController.topLevelNavOptions: NavOptions
    get() = navOptions {
        popUpTo(graph.findStartDestination().id) {
                saveState = true
            }
        launchSingleTop = true
        restoreState = true
    }

