package az.kodcraft.dashboard.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class TrainerDashboardDayDm(
    val date: LocalDate,
    val sessions: List<DayTraineesDm> = emptyList()
) : Parcelable {

    @Parcelize
    data class DayTraineesDm(
        val id: String,
        val workoutTitle: String,
        val traineeName: String,
    ) : Parcelable
}
