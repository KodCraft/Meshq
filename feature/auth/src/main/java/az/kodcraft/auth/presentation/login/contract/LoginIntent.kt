package az.kodcraft.auth.presentation.login.contract

import java.time.LocalDate

sealed class LoginIntent {
    data object Init : LoginIntent()
    data object GetWeekData : LoginIntent()
    data class GetWeekWorkouts(val date:LocalDate) : LoginIntent()
    data class SetDay(val date:LocalDate, val index : Int): LoginIntent()
}
