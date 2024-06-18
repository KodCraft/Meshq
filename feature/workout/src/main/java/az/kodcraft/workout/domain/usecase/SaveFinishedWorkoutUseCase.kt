package az.kodcraft.workout.domain.usecase

import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.workout.domain.model.AssignedWorkoutDm
import az.kodcraft.workout.domain.repository.AssignedWorkoutRepository
import com.solid.copilot.network.base.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveFinishedWorkoutUseCase @Inject constructor(private val repository: AssignedWorkoutRepository) :
    BaseUseCase<AssignedWorkoutDm, Response<Boolean>> {
    override suspend fun execute(param:AssignedWorkoutDm): Flow<Response<Boolean>> =
        repository.saveFinishedWorkout(param)
}

