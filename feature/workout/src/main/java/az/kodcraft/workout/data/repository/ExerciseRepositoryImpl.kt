package az.kodcraft.workout.data.repository

import az.kodcraft.core.data.ResponseHandler.safeFirestoreCall
import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.workout.data.dto.toDm
import az.kodcraft.workout.data.service.ExerciseService
import az.kodcraft.workout.domain.model.ExerciseDm
import az.kodcraft.workout.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow

class ExerciseRepositoryImpl(private val service: ExerciseService) : ExerciseRepository {
    override suspend fun getExercises(searchText: String): Flow<Response<List<ExerciseDm>>> {
        return safeFirestoreCall(
            mapToDomain = { it.map { exercise -> exercise.toDm() } },
            firestoreCall = {
                service.getExercises(searchText)
            }
        )
    }

}