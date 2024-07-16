package az.kodcraft.client.presentation.clientDetails.contract

import android.os.Parcelable
import az.kodcraft.client.domain.model.ClientDm
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.YearMonth

@Parcelize
data class ClientDetailsUiState(
    val clientDetails: ClientDm = ClientDm.EMPTY,
    val isLoading: Boolean = true,
    val isScheduleLoading: Boolean = true,
    val isError: Boolean = false,
    val selectedDay: LocalDate = LocalDate.now(),
) : Parcelable {
    sealed class PartialState {
        data object Loading : PartialState()
        data object ScheduleLoading : PartialState()
        data class ClientsDetails(val value: ClientDm) : PartialState()
        data class ClientWorkouts(
            val data: List<ClientDm.WorkoutSessionDm>,
            val yearMonth: YearMonth
        ) : PartialState()
        data class SelectDate(val value:LocalDate) : PartialState()
    }
}
