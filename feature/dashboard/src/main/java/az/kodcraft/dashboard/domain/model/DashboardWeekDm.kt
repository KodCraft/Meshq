package az.kodcraft.dashboard.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class DashboardWeekDm(
    val weekDays: List<DayOfWeekDm>,
) : Parcelable{
    companion object{
        val EMPTY = DashboardWeekDm(emptyList())
    }
}