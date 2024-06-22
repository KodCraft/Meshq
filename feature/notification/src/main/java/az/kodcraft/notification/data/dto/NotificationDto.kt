package az.kodcraft.notification.data.dto

import az.kodcraft.notification.domain.model.NotificationListItemDm
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class NotificationDto(
    val id: String = "",
    val message: String = "",
    @PropertyName("subscription_request_id")
    @field:JvmField
    val subRequestId: String = "",
    @PropertyName("timestamp")
    val date: Timestamp = Timestamp.now(),
    @field:JvmField
    val isRead: Boolean = false
) {
    private val isSubscriptionRequest: Boolean
        get() = subRequestId.isNotEmpty()

    fun toDm() = NotificationListItemDm(
        id = id,
        subscriptionId = subRequestId,
        isSubscriptionRequest = isSubscriptionRequest,
        body = message,
        date = date.toDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
    )
}
