package az.kodcraft.auth.presentation.login.contract

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Immutable
@Parcelize
data class LoginUiState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
) : Parcelable {

    sealed class PartialState {
        data object Loading : PartialState()
        data object Init : PartialState()
         class WeekData() : PartialState()
        data class SelectedDay(val date: LocalDate, val index: Int) : PartialState()
        object LoginUserAccount : PartialState()
    }
}