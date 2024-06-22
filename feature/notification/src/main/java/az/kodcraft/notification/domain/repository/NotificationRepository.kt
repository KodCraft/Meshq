package az.kodcraft.notification.domain.repository

import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.notification.domain.model.NotificationListItemDm
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun getNotifications(): Flow<Response<List<NotificationListItemDm>>>
    suspend fun acceptSubscriptionRequest(notificationId: String, subRequestId:String): Flow<Response<Boolean>>
    suspend fun deleteNotification(notificationId: String): Flow<Response<Boolean>>
}