package az.kodcraft.auth.navigation

import androidx.navigation.NavController
import az.kodcraft.auth.navigation.AuthRouteConstants.LOGIN_SCREEN

object AuthRouteConstants {
    const val LOGIN_SCREEN = "login_screen"
}
fun NavController.navigateToLoginScreen(){
    popBackStack(route = LOGIN_SCREEN, inclusive = false)
}
