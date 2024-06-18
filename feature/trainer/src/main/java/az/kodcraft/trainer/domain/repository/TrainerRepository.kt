package az.kodcraft.trainer.domain.repository

import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.trainer.domain.model.TrainerDm
import az.kodcraft.trainer.domain.model.TrainerListItemDm
import kotlinx.coroutines.flow.Flow

interface TrainerRepository {
    suspend fun getTrainers(searchText: String): Flow<Response<List<TrainerListItemDm>>>
    suspend fun getTrainer(id: String): Flow<Response<TrainerDm>>
    suspend fun sendSubscriptionRequest(trainerId: String, traineeId:String): Flow<Response<Boolean>>
    suspend fun unsendSubscriptionRequest(trainerId: String, traineeId:String): Flow<Response<Boolean>>
}