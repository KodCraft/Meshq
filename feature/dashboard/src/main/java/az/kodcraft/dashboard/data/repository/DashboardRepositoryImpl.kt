package az.kodcraft.dashboard.data.repository

import az.kodcraft.core.data.ResponseHandler.safeFirestoreCall
import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.core.utils.getStartAndEndDateOfWeek
import az.kodcraft.dashboard.data.service.DashboardService
import az.kodcraft.dashboard.domain.model.DashboardWeekWorkoutDm
import az.kodcraft.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class DashboardRepositoryImpl(private val service: DashboardService) : DashboardRepository {
    override suspend fun fetchWeekWorkouts(
        date: LocalDate
    ): Flow<Response<List<DashboardWeekWorkoutDm>>> {

        val dateRange = getStartAndEndDateOfWeek(date)
        return safeFirestoreCall(
            mapToDomain = { result -> result.map { it.toDm(date) } },
            firestoreCall = {
                service.fetchWeekWorkouts(dateRange.first, dateRange.second)
            }
        )
    }
}