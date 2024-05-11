package az.kodcraft.workout.domain.repository

import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.workout.domain.model.WorkoutDm
import kotlinx.coroutines.flow.Flow

interface AssignedWorkoutRepository {
    suspend fun getWorkout(workoutId: String): Flow<Response<WorkoutDm?>>
    suspend fun saveFinishedWorkout(workout: WorkoutDm): Flow<Response<Boolean>>
}