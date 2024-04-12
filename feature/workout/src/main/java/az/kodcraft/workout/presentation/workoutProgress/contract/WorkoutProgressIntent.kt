package az.kodcraft.workout.presentation.workoutProgress.contract


sealed class WorkoutProgressIntent {
    data object FinishWorkout : WorkoutProgressIntent()
    data object CompleteWorkout : WorkoutProgressIntent()
    data class GetWorkoutData(val workoutId: String) : WorkoutProgressIntent()
    data class ChangeExerciseStatus(val exerciseId: String) : WorkoutProgressIntent()
}
