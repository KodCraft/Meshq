package az.kodcraft.notification.presentation.contract

import android.os.Parcelable
import az.kodcraft.notification.domain.model.NotificationListItemDm
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationUiState(
    val notificationList: List<NotificationListItemDm> = emptyList(),
    val isLoading: Boolean = true,
    val isAcceptSubscriptionLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
) : Parcelable {
    sealed class PartialState {
        data class Error(val message: String) : PartialState()
        data object Loading : PartialState()
        data object AcceptSubscriptionLoading : PartialState()
        data class AcceptSubscription(val notificationId:String) : PartialState()
        data class Notifications(val value: List<NotificationListItemDm>) : PartialState()
    }
}