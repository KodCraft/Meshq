package az.kodcraft.dashboard.data.dto

import az.kodcraft.core.utils.formatDateToStringDayAndMonth
import az.kodcraft.dashboard.domain.model.DashboardWeekWorkoutDm
import com.google.firebase.Timestamp
import java.time.LocalDate
import java.time.ZoneId

data class DashboardWeekWorkoutDto(
    var id: String = "",
    val title: String= "",
    val date: Timestamp= Timestamp.now(),
    val notes: String= "",
    val labels: List<String> = emptyList()
) {
    
    fun toDm(selectedDate:LocalDate) = DashboardWeekWorkoutDm(
        id = id,
        date = date.toDate().formatDateToStringDayAndMonth(),
        content = notes,
        title = title,
        labels = labels,
        isSelected = date.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() == selectedDate
    )
}