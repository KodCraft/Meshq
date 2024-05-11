package az.kodcraft.workout.presentation.createWorkout.contract

import android.os.Parcelable
import az.kodcraft.workout.domain.model.CreateWorkoutDm
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateWorkoutUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val workout: CreateWorkoutDm = CreateWorkoutDm.EMPTY
):Parcelable {
    sealed class PartialState {
        data object Loading : PartialState()
    }
}
