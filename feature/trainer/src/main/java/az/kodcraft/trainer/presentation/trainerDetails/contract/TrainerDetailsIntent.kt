package az.kodcraft.trainer.presentation.trainerDetails.contract

sealed class TrainerDetailsIntent {
    data object SendSubscriptionRequest : TrainerDetailsIntent()
    data object UnSendSubscriptionRequest : TrainerDetailsIntent()
    data class GetTrainerDetails(val id: String) : TrainerDetailsIntent()
}
