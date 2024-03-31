package az.kodcraft.dashboard.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate


@Parcelize
data class DayOfWeekDm(
    val day: LocalDate,
    val workoutId: String?
) : Parcelable
