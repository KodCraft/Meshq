package az.kodcraft.notification.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions

object NotificationRouteConstants{
    const val NOTIFICATIONS_SCREEN = "NOTIFICATIONS_SCREEN"
}

fun NavController.navigateToNotificationsList(navOptions: NavOptions? = null){
    navigate(NotificationRouteConstants.NOTIFICATIONS_SCREEN, navOptions = navOptions)
}