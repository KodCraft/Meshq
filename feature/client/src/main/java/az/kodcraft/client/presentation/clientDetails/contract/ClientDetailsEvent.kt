package az.kodcraft.client.presentation.clientDetails.contract

sealed class ClientDetailsEvent {
    data object NavigateToDashboard: ClientDetailsEvent()
}