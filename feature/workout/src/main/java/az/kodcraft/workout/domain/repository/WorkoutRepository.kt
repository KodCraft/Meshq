package az.kodcraft.workout.domain.repository

import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.workout.domain.model.CreateWorkoutDm
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    suspend fun saveWorkout(workout: CreateWorkoutDm): Flow<Response<Boolean>>
    suspend fun getWorkout(id: String): Flow<Response<CreateWorkoutDm>>
}