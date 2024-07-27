package az.kodcraft.dashboard.data.dto.trainer

import com.google.firebase.Timestamp

data class TrainerDashboardWorkoutDto(
    var id: String = "",
    val date: Timestamp = Timestamp.now(),
    val title: String = "",
    val traineeId: String = ""
)