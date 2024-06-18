package az.kodcraft.trainer.presentation.trainerDetails.contract

sealed class TrainerDetailsEvent {
    data object NavigateToDashboard: TrainerDetailsEvent()
}