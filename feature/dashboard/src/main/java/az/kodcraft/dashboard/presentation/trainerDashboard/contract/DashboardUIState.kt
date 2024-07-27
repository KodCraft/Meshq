package az.kodcraft.dashboard.presentation.trainerDashboard.contract

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import az.kodcraft.dashboard.domain.model.TrainerDashboardDayDm
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Immutable
@Parcelize
data class TrainerDashboardUiState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val workouts: @RawValue PagingData<TrainerDashboardDayDm> = PagingData.empty()
) : Parcelable {
    sealed class PartialState {
        data class Workouts(val data: PagingData<TrainerDashboardDayDm>) : PartialState()

        data object Loading : PartialState()
        data object Init : PartialState()
    }
}