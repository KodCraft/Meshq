package az.kodcraft.workout.domain.repository

import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.workout.domain.model.AssignedWorkoutDm
import kotlinx.coroutines.flow.Flow

interface AssignedWorkoutRepository {
    suspend fun getWorkout(workoutId: String): Flow<Response<AssignedWorkoutDm?>>
    suspend fun saveFinishedWorkout(workout: AssignedWorkoutDm): Flow<Response<Boolean>>
}