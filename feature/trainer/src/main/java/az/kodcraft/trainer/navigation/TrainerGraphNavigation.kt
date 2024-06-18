package az.kodcraft.trainer.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions

object TrainerRouteConstants{
    const val TRAINER_DETAILS_SCREEN = "TRAINER_DETAILS_SCREEN/{userId}"
    const val EXPLORE_TRAINERS = "EXPLORE_TRAINERS"
}

fun NavController.navigateToTrainerDetails(userId:String){
    navigate(TrainerRouteConstants.TRAINER_DETAILS_SCREEN.replace("{userId}", userId))
}
fun NavController.navigateToTrainersList(navOptions: NavOptions? = null){
    navigate(TrainerRouteConstants.EXPLORE_TRAINERS, navOptions = navOptions)
}