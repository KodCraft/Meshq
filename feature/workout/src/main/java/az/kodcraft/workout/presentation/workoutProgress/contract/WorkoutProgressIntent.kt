package az.kodcraft.workout.presentation.workoutProgress.contract


sealed class WorkoutProgressIntent {
    data object SaveWorkoutProgress : WorkoutProgressIntent()
    data object ResetTheWorkout : WorkoutProgressIntent()
    data object SaveFinishWorkout : WorkoutProgressIntent()
    data class GetWorkoutData(val workoutId: String) : WorkoutProgressIntent()
    data class ChangeExerciseStatus(val exerciseId: String) : WorkoutProgressIntent()
    data class ChangeExerciseSetStatus(val exerciseId: String, val setId:String) : WorkoutProgressIntent()
    data class ChangeWeightValueForSet(val value: String, val setId:String) : WorkoutProgressIntent()
    data class ToggleExercisePreview(val exerciseId: String) : WorkoutProgressIntent()
}
