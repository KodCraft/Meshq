package az.kodcraft.dashboard.navigation

import androidx.navigation.NavController
import az.kodcraft.core.navigation.TopLevelDestinationTrainee
import az.kodcraft.core.navigation.TopLevelDestinationTrainer
import az.kodcraft.core.navigation.topLevelNavOptions


fun NavController.navigateToDashboard() {
    navigate(route = TopLevelDestinationTrainee.DASHBOARD.route, topLevelNavOptions)
}
fun NavController.navigateToFinishedWorkouts() {
    navigate(route = TopLevelDestinationTrainee.FINISHED_WORKOUTS.route, topLevelNavOptions)
}
fun NavController.navigateToExerciseLibrary() {
    navigate(route = TopLevelDestinationTrainee.EXERCISE_LIBRARY.route, topLevelNavOptions)
}


fun NavController.navigateToTrainerDashboard() {
    navigate(route = TopLevelDestinationTrainer.TRAINER_DASHBOARD.route, topLevelNavOptions)
}
fun NavController.navigateToWorkoutsLibrary() {
    navigate(route = TopLevelDestinationTrainer.WORKOUTS_LIBRARY.route, topLevelNavOptions)
}
fun NavController.navigateToClientsList() {
    navigate(route = TopLevelDestinationTrainer.CLIENTS_LIST.route, topLevelNavOptions)
}
fun NavController.navigateToTrainerExerciseLibrary() {
    navigate(route = TopLevelDestinationTrainer.EXERCISE_LIBRARY.route, topLevelNavOptions)
}
