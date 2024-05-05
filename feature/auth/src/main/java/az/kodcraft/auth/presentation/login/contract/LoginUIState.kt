package az.kodcraft.auth.presentation.login.contract

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.util.Locale.IsoCountryCode

@Immutable
@Parcelize
data class LoginUiState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val phoneNumber: String = "",
    val countryCode: String = "",
    val deviceCountryCode: String = "",
) : Parcelable {

    sealed class PartialState {
        data object Loading : PartialState()
        data object Init : PartialState()
        data class PhoneNumber(val phoneNumber: String) : PartialState()
        data class CountryCode(val countryCode: String) : PartialState()
    }
}