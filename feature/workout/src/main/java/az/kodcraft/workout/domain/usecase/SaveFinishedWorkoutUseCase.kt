package az.kodcraft.workout.domain.usecase

import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.workout.domain.model.WorkoutDm
import az.kodcraft.workout.domain.repository.WorkoutRepository
import com.solid.copilot.network.base.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveFinishedWorkoutUseCase @Inject constructor(private val repository: WorkoutRepository) :
    BaseUseCase<WorkoutDm, Response<Boolean>> {
    override suspend fun execute(param:WorkoutDm): Flow<Response<Boolean>> =
        repository.saveFinishedWorkout(param)
}

