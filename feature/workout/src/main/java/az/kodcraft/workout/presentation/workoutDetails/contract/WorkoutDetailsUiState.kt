package az.kodcraft.workout.presentation.workoutDetails.contract

import android.os.Parcelable
import az.kodcraft.workout.domain.model.WorkoutDm
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkoutDetailsUiState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val workout: WorkoutDm = WorkoutDm.MOCK
):Parcelable {
    sealed class PartialState {
        data object Loading : PartialState()
    }
}
