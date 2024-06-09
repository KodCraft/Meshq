package az.kodcraft.workout.presentation.createWorkout.contract

sealed class CreateWorkoutEvent {
    data object NavigateToDashboard: CreateWorkoutEvent()
}