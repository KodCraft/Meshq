package az.kodcraft.dashboard.navigation

import androidx.navigation.NavController
import az.kodcraft.core.navigation.TopLevelDestination
import az.kodcraft.core.navigation.topLevelNavOptions


fun NavController.navigateToDashboard() {
    navigate(route = TopLevelDestination.DASHBOARD.route, topLevelNavOptions)
}
fun NavController.navigateToFinishedWorkouts() {
    navigate(route = TopLevelDestination.FINISHED_WORKOUTS.route, topLevelNavOptions)
}
fun NavController.navigateToExerciseLibrary() {
    navigate(route = TopLevelDestination.EXERCISE_LIBRARY.route, topLevelNavOptions)
}