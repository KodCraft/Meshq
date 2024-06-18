package az.kodcraft.workout.presentation.createWorkout.contract

import android.os.Parcelable
import az.kodcraft.workout.domain.model.CreateWorkoutDm
import az.kodcraft.workout.domain.model.ExerciseDm
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateWorkoutUiState(
    val workout:CreateWorkoutDm = CreateWorkoutDm.EMPTY,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val searchValue: String = "",
    val exercises: List<ExerciseDm> = emptyList(),
    val selectedExercise: CreateWorkoutDm.Exercise = CreateWorkoutDm.Exercise.EMPTY
) : Parcelable {
    sealed class PartialState {
        data object Loading : PartialState()
        data class Exercises(val data: List<ExerciseDm>) : PartialState()
        data class WorkoutExercise(val sets: List<CreateWorkoutDm.Exercise.Set>) : PartialState()
        data class SearchText(val value: String) : PartialState()
        data class RemoveWorkoutExercise(val id: String) : PartialState()
        data class SelectedExercise(val exercise: CreateWorkoutDm.Exercise) : PartialState()
    }
}
