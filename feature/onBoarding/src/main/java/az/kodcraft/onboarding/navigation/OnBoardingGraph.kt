package az.kodcraft.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import az.kodcraft.onboarding.presentation.onboarding.OnBoardingRoute
import az.kodcraft.onboarding.presentation.splash.SplashRoute

fun NavGraphBuilder.onBoardingGraph(
    navController: NavController,
//    changeTopAndBottomState: (showTopBar: Boolean, showBottomNavigation: Boolean) -> Unit,
//    navigateToLogin: () -> Unit,
//    navigateToHome: () -> Unit,
) {


    composable(route = OnBoardingRouteConstants.SPLASH_SCREEN) {
        SplashRoute(
//            onNavigateTo = navController::navigate,
//            onNavigateUp = navController::navigateUp,
//            onNavigateToOnBoarding = { navController.navigateToOnBoarding() },
//            onNavigateToHome = navigateToHome
        )
    }

    composable(route = OnBoardingRouteConstants.ONBOARDING_SCREEN) {
        OnBoardingRoute(
//            onNavigateTo = navController::navigate,
//            onNavigateUp = navController::navigateUp,
//            onNavigateToLogin = navigateToLogin,
//            onNavigateToHome = navigateToHome
        )
    }


}