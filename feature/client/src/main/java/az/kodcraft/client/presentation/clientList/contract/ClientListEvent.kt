package az.kodcraft.client.presentation.clientList.contract

sealed class ClientListEvent {
    data object NavigateToDashboard: ClientListEvent()
}