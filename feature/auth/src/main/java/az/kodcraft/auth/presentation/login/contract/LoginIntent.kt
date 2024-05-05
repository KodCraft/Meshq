package az.kodcraft.auth.presentation.login.contract

import java.time.LocalDate
import java.util.Locale.IsoCountryCode

sealed class LoginIntent {
    data object Init : LoginIntent()
    data class OnPhoneNumberChanged(val phoneNumber: String) : LoginIntent()
    data class CountryCode(val countryCode: String) : LoginIntent()
    data class Login(val countryCode: String) : LoginIntent()
    data class SetDay(val date: LocalDate, val index: Int) : LoginIntent()
}
