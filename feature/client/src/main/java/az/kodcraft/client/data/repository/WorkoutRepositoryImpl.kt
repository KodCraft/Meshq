package az.kodcraft.client.data.repository

import az.kodcraft.client.domain.model.WorkoutListItemDm
import az.kodcraft.client.domain.model.WorkoutsFilterReqDm
import az.kodcraft.client.domain.repository.WorkoutRepository
import az.kodcraft.core.data.ResponseHandler
import az.kodcraft.core.domain.UserManager
import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.client.data.service.WorkoutService
import az.kodcraft.client.domain.model.AssignWorkoutReqDm
import az.kodcraft.core.utils.localDateToTimestamp
import kotlinx.coroutines.flow.Flow

class WorkoutRepositoryImpl(private val service: WorkoutService) : WorkoutRepository {
    override suspend fun getWorkouts(req: WorkoutsFilterReqDm): Flow<Response<List<WorkoutListItemDm>>> {
        return ResponseHandler.safeFirestoreCall(
            mapToDomain = { it.map { workout -> workout.toDm() } },
            firestoreCall = {
                service.getWorkouts(
                    UserManager.getUserId(),
                    req.search,
                    req.tags.filter { it.isSelected }.map { it.id })
            }
        )
    }

    override suspend fun assignWorkout(req: AssignWorkoutReqDm): Flow<Response<Boolean>> {
        return ResponseHandler.safeFirestoreCall(
            mapToDomain = { true },
            firestoreCall = {
                service.assignWorkout(
                    UserManager.getUserId(),
                    req.traineeId,
                    req.workout.id,
                    localDateToTimestamp(req.date)
                )
            }
        )
    }
}