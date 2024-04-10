package az.kodcraft.workout.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import az.kodcraft.workout.presentation.workoutDetails.WorkoutDetailsRoute
import az.kodcraft.workout.presentation.workoutProgress.WorkoutProgressRoute

fun NavGraphBuilder.workoutGraph(
    navigateToWorkoutProgress: (String) -> Unit,
    navigateBack: () -> Unit,
) {
    composable(route = WorkoutRouteConstants.WORKOUT_DETAILS_SCREEN) { backStackEntry ->
        backStackEntry.arguments?.getString("workoutId")?.let { id ->
            WorkoutDetailsRoute(
                navigateToWorkoutProgress = navigateToWorkoutProgress,
                workoutId = id,
                navigateBack = navigateBack
            )
        }
    }
    composable(route = WorkoutRouteConstants.WORKOUT_PROGRESS_SCREEN) { backStackEntry ->
        backStackEntry.arguments?.getString("workoutId")?.let{ id ->
            WorkoutProgressRoute()
        }

    }
}