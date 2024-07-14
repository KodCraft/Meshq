package az.kodcraft.core.presentation.composable.calendar.contract

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import az.kodcraft.core.presentation.composable.calendar.CalendarDataSource
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.time.LocalDate
import java.time.YearMonth


@Parcelize
data class CalendarUiState(
    val yearMonth: YearMonth = YearMonth.now(),
    val dates: List<Date> = CalendarDataSource.getDates(YearMonth.now())
) : Parcelable {
    sealed class PartialState {
        data object NextMonth : PartialState()
        data object PreviousMonth : PartialState()
    }

    @Parcelize
    data class Date(
        val dayOfMonth: Int,
        val localDate: LocalDate,
        val badges: @RawValue List<Color> = emptyList(),
        val isSelected: Boolean
    ) : Parcelable {
        companion object {
            val Empty = Date(dayOfMonth = -1, localDate = LocalDate.now(), isSelected = false)
        }
    }
}