package az.kodcraft.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import az.kodcraft.auth.presentation.login.LoginRoute

fun NavGraphBuilder.authGraph(
    navigateToDashboard: () -> Unit, navigateBack: () -> Unit,
    onLoginClicked: (phoneNumber: String) -> Unit,
) {
    composable(route = AuthRouteConstants.LOGIN_SCREEN) { backStackEntry ->
        LoginRoute(
            navigateToDashboard = navigateToDashboard,
            userId = "id",
            navigateBack = navigateBack,
            onLoginClicked = onLoginClicked
        )
    }
}