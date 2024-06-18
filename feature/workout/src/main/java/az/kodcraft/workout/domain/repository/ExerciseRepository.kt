package az.kodcraft.workout.domain.repository

import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.workout.domain.model.ExerciseDm
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    suspend fun getExercises(searchText: String): Flow<Response<List<ExerciseDm>>>
}