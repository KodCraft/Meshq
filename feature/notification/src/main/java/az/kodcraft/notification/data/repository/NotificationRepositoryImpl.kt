package az.kodcraft.notification.data.repository

import az.kodcraft.core.data.ResponseHandler
import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.notification.data.service.NotificationService
import az.kodcraft.notification.domain.model.NotificationListItemDm
import az.kodcraft.notification.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow

class NotificationRepositoryImpl (private val service:NotificationService): NotificationRepository {
    override suspend fun getNotifications(): Flow<Response<List<NotificationListItemDm>>> {
        return ResponseHandler.safeFirestoreCall(
            mapToDomain = { it.map { trainer -> trainer.toDm() } },
            firestoreCall = {
                service.getNotifications()
            }
        )
    }

    override suspend fun acceptSubscriptionRequest(notificationId: String, subRequestId:String): Flow<Response<Boolean>> {
        return ResponseHandler.safeFirestoreCall(
            mapToDomain = { true },
            firestoreCall = {
                service.acceptSubscriptionRequest(notificationId = notificationId, subRequestId = subRequestId)
            }
        )
    }
    override suspend fun deleteNotification(notificationId: String): Flow<Response<Boolean>> {
        return ResponseHandler.safeFirestoreCall(
            mapToDomain = { true },
            firestoreCall = {
                service.deleteNotification(notificationId = notificationId)
            }
        )
    }
}