package az.kodcraft.client.domain.repository

import az.kodcraft.client.domain.model.AssignWorkoutReqDm
import az.kodcraft.client.domain.model.WorkoutListItemDm
import az.kodcraft.client.domain.model.WorkoutsFilterReqDm
import az.kodcraft.core.domain.bases.model.Response
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    suspend fun getWorkouts(req: WorkoutsFilterReqDm): Flow<Response<List<WorkoutListItemDm>>>
    suspend fun assignWorkout(req: AssignWorkoutReqDm): Flow<Response<Boolean>>
}