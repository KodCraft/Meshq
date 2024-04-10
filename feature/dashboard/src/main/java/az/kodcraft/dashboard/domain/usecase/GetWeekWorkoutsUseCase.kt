package az.kodcraft.dashboard.domain.usecase

import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.dashboard.domain.model.DashboardWeekWorkoutDm
import az.kodcraft.dashboard.domain.repository.DashboardRepository
import com.solid.copilot.network.base.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetWeekWorkoutsUseCase @Inject constructor(private val repository: DashboardRepository) :
    BaseUseCase<LocalDate, Response<List<DashboardWeekWorkoutDm>>> {
    override suspend fun execute(param:LocalDate): Flow<Response<List<DashboardWeekWorkoutDm>>> =
        repository.fetchWeekWorkouts(param)
}

