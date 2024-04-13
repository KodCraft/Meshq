package az.kodcraft.workout.data.repository

import az.kodcraft.core.data.ResponseHandler.safeFirestoreCall
import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.workout.data.service.WorkoutService
import az.kodcraft.workout.domain.model.WorkoutDm
import az.kodcraft.workout.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow

class WorkoutRepositoryImpl(private val service: WorkoutService) : WorkoutRepository {
    override suspend fun getWorkout(
        workoutId:String
    ): Flow<Response<WorkoutDm?>> {
        return safeFirestoreCall(
            mapToDomain = { it?.toDm() },
            firestoreCall = {
                service.getWorkout(workoutId)
            }
        )
    }

    override suspend fun saveFinishedWorkout(workout: WorkoutDm): Flow<Response<Boolean>> {
        return safeFirestoreCall(
            mapToDomain = { it },
            firestoreCall = {
                service.saveFinishedWorkout(workout)
            }
        )
    }
}