package az.kodcraft.workout.presentation.workoutDetails.contract


sealed class WorkoutDetailsIntent {
    data class GetWorkoutData(val workoutId:String):WorkoutDetailsIntent()
}
