package az.kodcraft.client.presentation.clientDetails.contract

import java.time.LocalDate
import java.time.YearMonth

sealed class ClientDetailsIntent {
    data class GetClientDetails(val id: String) : ClientDetailsIntent()
    data class GetMonthWorkouts(val yearMonth: YearMonth) : ClientDetailsIntent()
    data class SelectDate(val value: LocalDate) : ClientDetailsIntent()
}
