package az.kodcraft.meshq.navigation

import androidx.annotation.DrawableRes
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import az.kodcraft.client.navigation.ClientRouteConstants
import az.kodcraft.dashboard.navigation.DashboardRouteConstants
import az.kodcraft.workout.navigation.WorkoutRouteConstants

object NavGraphConstants {
    const val ROOT_GRAPH = "root_graph"
}

interface TopLevelDestination {
     val route: String
     val selectedIconId: Int
     val unselectedIconId: Int
     companion object {

         fun getStartDestinationForRole(isTrainer: Boolean): TopLevelDestination {
             return if (isTrainer) {
                 TopLevelDestinationTrainer.TRAINER_DASHBOARD_SCREEN
             } else {
                 TopLevelDestinationTrainee.DASHBOARD_SCREEN
             }
         }
     }
}

enum class TopLevelDestinationTrainee(
    override val route: String,
    @DrawableRes override val selectedIconId: Int,
    @DrawableRes override val unselectedIconId: Int
): TopLevelDestination {
    EXPLORE_TRAINERS_SCREEN(
        route = "EXPLORE_TRAINERS_SCREEN",
        unselectedIconId = az.kodcraft.core.R.drawable.ic_search,
        selectedIconId = az.kodcraft.core.R.drawable.ic_search,
    ),
    DASHBOARD_SCREEN(
        route = "DASHBOARD_SCREEN",
        unselectedIconId = az.kodcraft.core.R.drawable.ic_dashboard,
        selectedIconId = az.kodcraft.core.R.drawable.ic_dashboard,
    ),
    MY_PROGRESS_SCREEN(
        route = "MY_PROGRESS_SCREEN",
        unselectedIconId = az.kodcraft.core.R.drawable.ic_checked_list,
        selectedIconId = az.kodcraft.core.R.drawable.ic_checked_list,
    ),
}

enum class TopLevelDestinationTrainer(
    override val route: String,
    @DrawableRes override val selectedIconId: Int,
    @DrawableRes override val unselectedIconId: Int,
    val title:String
): TopLevelDestination {
    TRAINER_DASHBOARD_SCREEN(
        route = DashboardRouteConstants.TRAINER_DASHBOARD_SCREEN,
        unselectedIconId = az.kodcraft.core.R.drawable.ic_dashboard,
        selectedIconId = az.kodcraft.core.R.drawable.ic_dashboard,
        title = "Dashboard"
    ),
    CLIENT_LIST_SCREEN(
        route = ClientRouteConstants.CLIENT_LIST_SCREEN,
        unselectedIconId = az.kodcraft.core.R.drawable.ic_client_list,
        selectedIconId = az.kodcraft.core.R.drawable.ic_client_list,
        title = "Clients"
    ),
    WORKOUTS_LIBRARY_SCREEN(
        route = WorkoutRouteConstants.WORKOUTS_LIBRARY_SCREEN,
        unselectedIconId = az.kodcraft.core.R.drawable.ic_library,
        selectedIconId = az.kodcraft.core.R.drawable.ic_library,
        title = "Workouts"
    ),
    EXERCISE_LIBRARY_SCREEN(
        route = "EXERCISE_LIBRARY_SCREEN",
        unselectedIconId = az.kodcraft.core.R.drawable.ic_dumbbell,
        selectedIconId = az.kodcraft.core.R.drawable.ic_dumbbell,
        title = "Exercises"
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

fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.route, true) ?: false
    } ?: false



