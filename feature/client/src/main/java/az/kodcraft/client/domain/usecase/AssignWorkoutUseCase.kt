package az.kodcraft.client.domain.usecase

import az.kodcraft.client.domain.model.AssignWorkoutReqDm
import az.kodcraft.client.domain.repository.WorkoutRepository
import az.kodcraft.core.domain.bases.model.Response
import com.solid.copilot.network.base.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AssignWorkoutUseCase @Inject constructor(private val repository: WorkoutRepository) :
    BaseUseCase<AssignWorkoutReqDm, Response<Boolean>> {
    override suspend fun execute(param:AssignWorkoutReqDm): Flow<Response<Boolean>> =
        repository.assignWorkout(param)
}