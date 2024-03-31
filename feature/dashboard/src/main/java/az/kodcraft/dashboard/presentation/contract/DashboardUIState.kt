package az.kodcraft.dashboard.presentation.contract

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import az.kodcraft.dashboard.domain.model.DashboardWeekDm
import az.kodcraft.dashboard.domain.model.DashboardWeekWorkoutDm
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Immutable
@Parcelize
data class DashboardUiState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val selectedDay: LocalDate = LocalDate.now(),
    val weekData: DashboardWeekDm = DashboardWeekDm.EMPTY,
    val weekWorkouts: List<DashboardWeekWorkoutDm> = emptyList()
) : Parcelable {
    sealed class PartialState {
        object Loading : PartialState()
        object Init : PartialState()
        data class WeekData(val data: DashboardWeekDm) : PartialState()
        data class SelectedDay(val date: LocalDate) : PartialState()
        data class WeekWorkouts(val data: List<DashboardWeekWorkoutDm>) : PartialState()
    }
}