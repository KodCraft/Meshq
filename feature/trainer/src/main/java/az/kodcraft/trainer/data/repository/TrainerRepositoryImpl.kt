package az.kodcraft.trainer.data.repository

import az.kodcraft.core.data.ResponseHandler
import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.trainer.data.service.TrainerService
import az.kodcraft.trainer.domain.model.TrainerDm
import az.kodcraft.trainer.domain.model.TrainerListItemDm
import az.kodcraft.trainer.domain.repository.TrainerRepository
import kotlinx.coroutines.flow.Flow

class TrainerRepositoryImpl (private val service:TrainerService):TrainerRepository {
    override suspend fun getTrainers(searchText: String): Flow<Response<List<TrainerListItemDm>>> {
        return ResponseHandler.safeFirestoreCall(
            mapToDomain = { it.map { trainer -> trainer.toDm() } },
            firestoreCall = {
                service.getTrainers(searchText)
            }
        )
    }
    override suspend fun getTrainer(id: String): Flow<Response<TrainerDm>> {
        return ResponseHandler.safeFirestoreCall(
            mapToDomain = { it?.toDm() ?: TrainerDm.EMPTY },
            firestoreCall = {
                service.getTrainer(id)
            }
        )
    }
    override suspend fun sendSubscriptionRequest(trainerId: String, traineeId:String): Flow<Response<Boolean>> {
        return ResponseHandler.safeFirestoreCall(
            mapToDomain = { true },
            firestoreCall = {
                service.sendSubscriptionRequest(trainerId = trainerId, traineeId = traineeId)
            }
        )
    }
    override suspend fun unsendSubscriptionRequest(trainerId: String, traineeId:String): Flow<Response<Boolean>> {
        return ResponseHandler.safeFirestoreCall(
            mapToDomain = { true },
            firestoreCall = {
                service.unSendSubscriptionRequest(trainerId = trainerId, traineeId = traineeId)
            }
        )
    }
}