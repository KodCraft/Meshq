package az.kodcraft.dashboard.presentation.contract

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import az.kodcraft.dashboard.domain.model.DashboardWeekDm
import az.kodcraft.dashboard.domain.model.DashboardWeekWorkoutDm
import az.kodcraft.dashboard.presentation.StartIndex
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale

@Immutable
@Parcelize
data class DashboardUiState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val startIndex: Int = StartIndex,
    val selectedDay: LocalDate = LocalDate.now(),
    val weekData: DashboardWeekDm = DashboardWeekDm.EMPTY,
    val weekWorkouts: List<DashboardWeekWorkoutDm> = emptyList()
) : Parcelable {
    fun selectedWeek(index:Int) = selectedDay.get(
        WeekFields.of(
            Locale.getDefault()
        ).weekOfWeekBasedYear()
    )+ (index -  startIndex)

    sealed class PartialState {
        data object Loading : PartialState()
        data object Init : PartialState()
        data class WeekData(val data: DashboardWeekDm) : PartialState()
        data class SelectedDay(val date: LocalDate, val index: Int) : PartialState()
        data class WeekWorkouts(val data: List<DashboardWeekWorkoutDm>) : PartialState()
    }
}