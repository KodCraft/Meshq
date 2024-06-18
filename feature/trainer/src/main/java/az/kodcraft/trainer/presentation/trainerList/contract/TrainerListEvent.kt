package az.kodcraft.trainer.presentation.trainerList.contract

sealed class TrainerListEvent {
    data object NavigateToDashboard: TrainerListEvent()
    data class NavigateToUserProfile(val id:String): TrainerListEvent()
}