package az.kodcraft.workout.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions

object WorkoutRouteConstants {
    const val WORKOUT_DETAILS_SCREEN = "WORKOUT_DETAILS_SCREEN/{workoutId}"
    const val WORKOUT_PROGRESS_SCREEN = "WORKOUT_PROGRESS_SCREEN/{workoutId}"
    const val CREATE_WORKOUT_SCREEN = "CREATE_WORKOUT_SCREEN"
    const val FINISHED_WORKOUTS_SCREEN = "FINISHED_WORKOUTS_SCREEN"
    const val WORKOUTS_LIBRARY_SCREEN = "WORKOUTS_LIBRARY_SCREEN"
}

fun NavController.navigateToWorkoutDetails(workoutId:String){
    navigate(WorkoutRouteConstants.WORKOUT_DETAILS_SCREEN.replace("{workoutId}", workoutId))
}
fun NavController.navigateToWorkoutProgress(workoutId:String){
    navigate(WorkoutRouteConstants.WORKOUT_PROGRESS_SCREEN.replace("{workoutId}", workoutId))
}
fun NavController.navigateToCreateWorkout(){
    navigate(WorkoutRouteConstants.CREATE_WORKOUT_SCREEN)
}

fun NavController.navigateToWorkoutsLibrary(navOptions: NavOptions? = null){
    navigate(WorkoutRouteConstants.WORKOUTS_LIBRARY_SCREEN, navOptions = navOptions)
}