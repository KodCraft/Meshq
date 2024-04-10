package az.kodcraft.workout.navigation

import androidx.navigation.NavController

object WorkoutRouteConstants {
    const val WORKOUT_DETAILS_SCREEN = "WORKOUT_DETAILS_SCREEN/{workoutId}"
    const val WORKOUT_PROGRESS_SCREEN = "WORKOUT_PROGRESS_SCREEN/{workoutId}"
}

fun NavController.navigateToWorkoutDetails(workoutId:String){
    navigate(WorkoutRouteConstants.WORKOUT_DETAILS_SCREEN.replace("{workoutId}", workoutId))
}