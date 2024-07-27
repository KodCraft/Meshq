package az.kodcraft.dashboard.domain.usecase.trainer

import androidx.paging.PagingData
import az.kodcraft.dashboard.domain.model.TrainerDashboardDayDm
import az.kodcraft.dashboard.domain.repository.TrainerDashboardRepository
import com.solid.copilot.network.base.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetPaginatedWorkoutsUseCase @Inject constructor(private val repository: TrainerDashboardRepository) :
    BaseUseCase<LocalDate, PagingData<TrainerDashboardDayDm>> {
    override suspend fun execute(param:LocalDate): Flow<PagingData<TrainerDashboardDayDm>> =
        repository.getWorkouts(param)
}

