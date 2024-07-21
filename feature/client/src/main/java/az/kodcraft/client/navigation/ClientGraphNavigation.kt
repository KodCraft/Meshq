package az.kodcraft.client.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions

object ClientRouteConstants{
    const val CLIENT_DETAILS_SCREEN = "CLIENT_DETAILS_SCREEN/{userId}"
    const val CLIENT_LIST_SCREEN = "CLIENT_LIST_SCREEN"
}

fun NavController.navigateToClientDetails(userId:String){
    navigate(ClientRouteConstants.CLIENT_DETAILS_SCREEN.replace("{userId}", userId))
}
fun NavController.navigateToClientList(navOptions:NavOptions? = null){
    navigate(ClientRouteConstants.CLIENT_LIST_SCREEN, navOptions = navOptions)
}