package az.kodcraft.workout.presentation.createWorkout.contract


sealed class CreateWorkoutIntent {
    data object SaveWorkout:CreateWorkoutIntent()
}
