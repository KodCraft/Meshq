package az.kodcraft.workout.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import az.kodcraft.workout.presentation.workoutDetails.WorkoutDetailsRoute
import az.kodcraft.workout.presentation.workoutProgress.WorkoutProgressRoute

fun NavGraphBuilder.workoutGraph(
    navigateToWorkoutProgress: (String) -> Unit
) {
    composable(route = WorkoutRouteConstants.WORKOUT_DETAILS_SCREEN) {
        WorkoutDetailsRoute(navigateToWorkoutProgress = navigateToWorkoutProgress)
    }
    composable(route = WorkoutRouteConstants.WORKOUT_PROGRESS_SCREEN) {
        WorkoutProgressRoute()
    }
}