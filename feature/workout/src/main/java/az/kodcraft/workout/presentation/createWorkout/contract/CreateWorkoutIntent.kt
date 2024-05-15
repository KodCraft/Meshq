package az.kodcraft.workout.presentation.createWorkout.contract

import az.kodcraft.workout.domain.model.CreateWorkoutDm


sealed class CreateWorkoutIntent {
    data object SaveWorkout : CreateWorkoutIntent()
    data object GetExercises : CreateWorkoutIntent()
    data object UnselectExercise : CreateWorkoutIntent()
    data class ChangeSearchValue(val value:String) : CreateWorkoutIntent()
    data class NewExerciseSelected(val id: String, val name:String) : CreateWorkoutIntent()
    data class SaveExerciseSets(val sets: List<CreateWorkoutDm.Exercise.Set>) : CreateWorkoutIntent()
}
