package az.kodcraft.client.domain.usecase

import az.kodcraft.client.domain.model.WorkoutListItemDm
import az.kodcraft.client.domain.model.WorkoutsFilterReqDm
import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.client.domain.repository.WorkoutRepository
import com.solid.copilot.network.base.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWorkoutsListUseCase @Inject constructor(private val repository: WorkoutRepository) :
    BaseUseCase<WorkoutsFilterReqDm, Response<List<WorkoutListItemDm>>> {
    override suspend fun execute(param:WorkoutsFilterReqDm): Flow<Response<List<WorkoutListItemDm>>> =
        repository.getWorkouts(param)
}