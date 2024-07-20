package az.kodcraft.client.presentation.clientDetails.contract

import az.kodcraft.client.domain.model.WorkoutListItemDm
import az.kodcraft.client.domain.model.WorkoutsFilterReqDm
import java.time.LocalDate
import java.time.YearMonth

sealed class ClientDetailsIntent {
    data class GetClientDetails(val id: String) : ClientDetailsIntent()
    data class GetMonthWorkouts(val yearMonth: YearMonth) : ClientDetailsIntent()
    data class SelectDate(val value: LocalDate) : ClientDetailsIntent()
    data object GetWorkouts : ClientDetailsIntent()
    data object ResetFilter : ClientDetailsIntent()
    data object AssignWorkout : ClientDetailsIntent()
    data object HideSheet : ClientDetailsIntent()
    data class FilterWorkoutsSearchText(val value: String) : ClientDetailsIntent()
    data class FilterWorkoutsTags(val tag: WorkoutsFilterReqDm.WorkoutTagDm) : ClientDetailsIntent()
    data class SelectWorkoutToAssign(val workout:WorkoutListItemDm) : ClientDetailsIntent()
    data class SetDateForWorkoutToAssign(val date:LocalDate) : ClientDetailsIntent()

}
