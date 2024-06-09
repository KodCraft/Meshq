package az.kodcraft.workout.domain.usecase

import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.workout.domain.model.AssignedWorkoutDm
import az.kodcraft.workout.domain.repository.AssignedWorkoutRepository
import com.solid.copilot.network.base.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWorkoutUseCase @Inject constructor(private val repository: AssignedWorkoutRepository) :
    BaseUseCase<String, Response<AssignedWorkoutDm?>> {
    override suspend fun execute(param:String): Flow<Response<AssignedWorkoutDm?>> =
        repository.getWorkout(param)
}

