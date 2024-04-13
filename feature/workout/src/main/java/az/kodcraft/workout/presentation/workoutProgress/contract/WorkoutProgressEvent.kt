package az.kodcraft.workout.presentation.workoutProgress.contract

sealed class WorkoutProgressEvent {
    data object NavigateHome: WorkoutProgressEvent()
}