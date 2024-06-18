package az.kodcraft.workout.data.repository

import az.kodcraft.core.data.ResponseHandler.safeFirestoreCall
import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.workout.data.dto.WorkoutDto.Companion.toDto
import az.kodcraft.workout.data.service.WorkoutService
import az.kodcraft.workout.domain.model.CreateWorkoutDm
import az.kodcraft.workout.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow

class WorkoutRepositoryImpl(private val service: WorkoutService) : WorkoutRepository {


    override suspend fun saveWorkout(workout: CreateWorkoutDm): Flow<Response<Boolean>> {
        return safeFirestoreCall(
            mapToDomain = { it },
            firestoreCall = {
                service.saveWorkout(workout.toDto())
            }
        )
    }

    override suspend fun getWorkout(id: String): Flow<Response<CreateWorkoutDm>> {
        return safeFirestoreCall(
            mapToDomain = { it?.toDm()?: CreateWorkoutDm.EMPTY },
            firestoreCall = {
                service.getWorkout(id)
            }
        )
    }
}