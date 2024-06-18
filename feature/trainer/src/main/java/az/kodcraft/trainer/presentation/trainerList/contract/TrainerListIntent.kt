package az.kodcraft.trainer.presentation.trainerList.contract

sealed class TrainerListIntent {
    data class SearchTextChange(val value: String) : TrainerListIntent()
    data class GetTrainersList(val value: String) : TrainerListIntent()
    data class NavigateToUserProfile(val id: String) : TrainerListIntent()
}
