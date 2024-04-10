package az.kodcraft.dashboard.domain.repository

import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.dashboard.domain.model.DashboardWeekWorkoutDm
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface DashboardRepository {
    suspend fun fetchWeekWorkouts(date: LocalDate): Flow<Response<List<DashboardWeekWorkoutDm>>>
}