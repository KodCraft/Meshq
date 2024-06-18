package az.kodcraft.workout.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import az.kodcraft.workout.presentation.createWorkout.CreateWorkoutRoute
import az.kodcraft.workout.presentation.workoutDetails.WorkoutDetailsRoute
import az.kodcraft.workout.presentation.workoutProgress.WorkoutProgressRoute

fun NavGraphBuilder.workoutGraph(
    navigateToWorkoutProgress: (String) -> Unit,
    navigateBack: () -> Unit,
    navigateHome: () -> Unit,
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
        backStackEntry.arguments?.getString("workoutId")?.let { id ->
            WorkoutProgressRoute(
                workoutId = id,
                navigateBack = navigateBack,
                navigateHome = navigateHome,
            )
        }
    }
    composable(route = WorkoutRouteConstants.CREATE_WORKOUT_SCREEN) {
        CreateWorkoutRoute(
            navigateBack = navigateBack,
        )
    }
    composable(route = WorkoutRouteConstants.FINISHED_WORKOUTS_SCREEN) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { Text("FINISHED WORKOUTS LIST UNDER CONSTRUCTION ... ", textAlign = TextAlign.Center) }
    }

        composable(route = WorkoutRouteConstants.WORKOUTS_LIBRARY_SCREEN) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { Text("WORKOUT LIBRARY UNDER CONSTRUCTION ... ", textAlign = TextAlign.Center) }
    }
}