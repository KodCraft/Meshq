package az.kodcraft.notification.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class NotificationListItemDm(
    val id: String,
    val subscriptionId: String,
    val isSubscriptionRequest: Boolean,
    val body: String,
    val date: LocalDateTime
) : Parcelable {
    companion object {
        val EMPTY = NotificationListItemDm(
            id = "", subscriptionId = "", false, "", LocalDateTime.now()
        )
        val MOCK = NotificationListItemDm(
            id = "9703",
            subscriptionId = "",
            isSubscriptionRequest = true,
            body = "New subscription request from the.lion",
            date = LocalDateTime.now()
        )
    }
}
