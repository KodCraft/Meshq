package az.kodcraft.client.presentation.clientDetails.contract

import android.os.Parcelable
import az.kodcraft.client.domain.model.AssignWorkoutReqDm
import az.kodcraft.client.domain.model.ClientDm
import az.kodcraft.client.domain.model.WorkoutListItemDm
import az.kodcraft.client.domain.model.WorkoutsFilterReqDm
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.YearMonth

@Parcelize
data class ClientDetailsUiState(
    val clientDetails: ClientDm = ClientDm.EMPTY,
    val filteredWorkouts: List<WorkoutListItemDm> =  emptyList(),
    val workoutToAssign: AssignWorkoutReqDm = AssignWorkoutReqDm.EMPTY,
    val workoutsFilter:  WorkoutsFilterReqDm = WorkoutsFilterReqDm.MOCK,
    val showSheet: Boolean = false,
    val isLoading: Boolean = true,
    val isScheduleLoading: Boolean = true,
    val isAssignmentLoading: Boolean = false,
    val areWorkoutsLoading: Boolean = true,
    val isError: Boolean = false,
    val selectedDay: LocalDate = LocalDate.now(),
    val applyFilter:Boolean = false
) : Parcelable {
    sealed class PartialState {
        data object Loading : PartialState()
        data object WorkoutAssignLoading : PartialState()
        data object HideSheet : PartialState()
        data object WorkoutAssigned : PartialState()
        data object ResetFilter : PartialState()
        data object ScheduleLoading : PartialState()
        data object WorkoutsLoading : PartialState()
        data class ClientsDetails(val value: ClientDm) : PartialState()
        data class ClientWorkouts(
            val data: List<ClientDm.WorkoutSessionDm>,
            val yearMonth: YearMonth
        ) : PartialState()
        data class WorkoutsTag(val tag:WorkoutsFilterReqDm.WorkoutTagDm) : PartialState()
        data class WorkoutsSearchText(val value:String) : PartialState()
        data class SelectDate(val value:LocalDate) : PartialState()
        data class Workouts(val data: List<WorkoutListItemDm>) : PartialState()
        data class WorkoutsFilter(val value: WorkoutsFilterReqDm) : PartialState()
        data class WorkoutToAssign(val value: WorkoutListItemDm) : PartialState()
        data class DateForWorkoutToAssign(val value: LocalDate) : PartialState()
    }
}
