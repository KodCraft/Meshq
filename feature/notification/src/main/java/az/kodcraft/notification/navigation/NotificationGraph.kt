package az.kodcraft.notification.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import az.kodcraft.notification.presentation.NotificationRoute

fun NavGraphBuilder.notificationGraph(
    navigateBack: () -> Unit
) {
    composable(route = NotificationRouteConstants.NOTIFICATIONS_SCREEN) {
        NotificationRoute(
            navigateBack = navigateBack
        )

    }
}