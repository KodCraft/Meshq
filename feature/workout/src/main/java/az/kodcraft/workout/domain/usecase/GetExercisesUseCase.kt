package az.kodcraft.workout.domain.usecase

import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.workout.domain.model.ExerciseDm
import az.kodcraft.workout.domain.repository.ExerciseRepository
import com.solid.copilot.network.base.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExercisesUseCase @Inject constructor(private val repository: ExerciseRepository) :
    BaseUseCase<String, Response<List<ExerciseDm>>> {
    override suspend fun execute(param:String): Flow<Response<List<ExerciseDm>>> =
        repository.getExercises(param)
}

