package az.kodcraft.client.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import az.kodcraft.client.presentation.clientDetails.ClientDetailsRoute
import az.kodcraft.client.presentation.clientList.ClientListRoute

fun NavGraphBuilder.clientGraph(
    navigateBack: () -> Unit,
    onMenuClick: () -> Unit,
    navigateToClient: (String) -> Unit,
) {
    composable(route = ClientRouteConstants.CLIENT_LIST_SCREEN) {
        ClientListRoute(
            navigateBack = navigateBack,
            navigateToClient = navigateToClient,
            onMenuClick = onMenuClick
        )

    }
    composable(route = ClientRouteConstants.CLIENT_DETAILS_SCREEN) { backStackEntry ->
        backStackEntry.arguments?.getString("userId")?.let { id ->
            ClientDetailsRoute(
                userId = id,
                navigateBack = navigateBack
            )
        }
    }
}