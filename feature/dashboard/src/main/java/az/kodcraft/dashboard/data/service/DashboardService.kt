package az.kodcraft.dashboard.data.service

import az.kodcraft.core.utils.localDateToTimestamp
import az.kodcraft.dashboard.data.dto.DashboardWeekWorkoutDto
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

class DashboardService(private val workoutRef: CollectionReference) {

    suspend fun fetchWeekWorkouts(
        startDate: String,
        endDate: String
    ): List<DashboardWeekWorkoutDto> {

        val startTimestamp = localDateToTimestamp(LocalDate.parse(startDate))
        val endTimestamp = localDateToTimestamp(
            LocalDate.parse(endDate).plusDays(1)
        )

        val documents = workoutRef
            .whereGreaterThanOrEqualTo("date", startTimestamp)
            .whereLessThanOrEqualTo("date", endTimestamp)
            .get()
            .await()

        return documents.map { document ->
            document.toObject(DashboardWeekWorkoutDto::class.java).apply {
                id = document.id  // Set the document ID
            }
        }
    }
}

