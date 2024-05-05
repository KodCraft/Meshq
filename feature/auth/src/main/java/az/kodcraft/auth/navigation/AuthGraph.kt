package az.kodcraft.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import az.kodcraft.auth.presentation.login.LoginRoute

fun NavGraphBuilder.authGraph(
    navigateToDashboard: () -> Unit, navigateBack: () -> Unit
) {
    composable(route = AuthRouteConstants.LOGIN_SCREEN) { backStackEntry ->
        LoginRoute(
            navigateToDashboard = navigateToDashboard, userId = "id", navigateBack = navigateBack
        )
       /* backStackEntry.arguments?.getString("workoutId")?.let { id ->
            LoginRoute(
                navigateToDashboard = navigateToDashboard, userId = id, navigateBack = navigateBack
            )
        }*/
    }
}
