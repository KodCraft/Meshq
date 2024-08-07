package az.kodcraft.notification.presentation.contract

sealed class NotificationIntent {
    data object GetNotifications : NotificationIntent()
    data class AcceptSubscription(val notificationId:String) : NotificationIntent()
    data class DeleteNotification(val notificationId:String) : NotificationIntent()
}