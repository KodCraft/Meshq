package az.kodcraft.workout.domain.usecase

import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.workout.domain.model.CreateWorkoutDm
import az.kodcraft.workout.domain.repository.WorkoutRepository
import com.solid.copilot.network.base.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveWorkoutUseCase @Inject constructor(private val repository: WorkoutRepository) :
    BaseUseCase<CreateWorkoutDm, Response<Boolean>> {
    override suspend fun execute(param:CreateWorkoutDm): Flow<Response<Boolean>> =
        repository.saveWorkout(param)
}

