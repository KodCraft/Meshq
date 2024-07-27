package az.kodcraft.dashboard.data.service

import az.kodcraft.core.domain.UserManager
import az.kodcraft.core.utils.localDateToTimestamp
import az.kodcraft.dashboard.data.dto.trainee.DashboardWeekWorkoutDto
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Filter
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

class DashboardService(
    private val assignedWorkoutRef: CollectionReference,
    private val usersCollection: CollectionReference
) {

    suspend fun fetchWeekWorkouts(
        startDate: String,
        endDate: String
    ): List<DashboardWeekWorkoutDto> {

        val startTimestamp = localDateToTimestamp(LocalDate.parse(startDate))
        val endTimestamp = localDateToTimestamp(
            LocalDate.parse(endDate).plusDays(1)
        )

        val documents = assignedWorkoutRef
            .whereGreaterThanOrEqualTo("date", startTimestamp)
            .whereLessThanOrEqualTo("date", endTimestamp)
            .where(Filter.equalTo("traineeId", UserManager.getUserId()))
            .get()
            .await()

        return documents.map { document ->
            document.toObject(DashboardWeekWorkoutDto::class.java).apply {
                id = document.id  // Set the document ID
            }
        }
    }

}

