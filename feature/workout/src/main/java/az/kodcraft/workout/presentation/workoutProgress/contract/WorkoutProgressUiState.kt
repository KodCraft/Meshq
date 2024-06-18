package az.kodcraft.workout.presentation.workoutProgress.contract

import android.os.Parcelable
import az.kodcraft.workout.domain.model.AssignedWorkoutDm
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkoutProgressUiState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val workout: AssignedWorkoutDm = AssignedWorkoutDm.EMPTY
) : Parcelable {
    sealed class PartialState {
        data object Loading : PartialState()
        data object RestartWorkout : PartialState()
        data object WorkoutSaved : PartialState()
        data object WorkoutFinished : PartialState()
        data class ToggleExercisePreview(val exerciseId: String) : PartialState()
        data class ExerciseSetStatus(val exerciseId: String, val setId:String) : PartialState()
        data class ExerciseSetWeight(val value: String, val setId:String) : PartialState()
        data class WorkoutData(val data: AssignedWorkoutDm) : PartialState()
        data class ExerciseStatus(val exerciseId: String) : PartialState()
    }
}
