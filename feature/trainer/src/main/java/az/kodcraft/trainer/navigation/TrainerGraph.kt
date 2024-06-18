package az.kodcraft.trainer.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import az.kodcraft.trainer.presentation.trainerDetails.TrainerDetailsRoute
import az.kodcraft.trainer.presentation.trainerList.TrainerListRoute

fun NavGraphBuilder.trainerGraph(
    navigateBack: () -> Unit,
    navigateToUserProfile: (id:String) -> Unit,

) {
    composable(route = TrainerRouteConstants.EXPLORE_TRAINERS) {
        TrainerListRoute(
            navigateBack = navigateBack,
            navigateToUserProfile = navigateToUserProfile
        )

    }
    composable(route = TrainerRouteConstants.TRAINER_DETAILS_SCREEN) { backStackEntry ->
        backStackEntry.arguments?.getString("userId")?.let { id ->
            TrainerDetailsRoute(
                userId = id,
                navigateBack = navigateBack
            )
        }
    }
}