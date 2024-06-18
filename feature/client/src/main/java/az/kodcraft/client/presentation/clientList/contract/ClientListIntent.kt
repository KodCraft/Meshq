package az.kodcraft.client.presentation.clientList.contract

sealed class ClientListIntent {
    data class SearchTextChange(val value: String) : ClientListIntent()
    data class GetClientsList(val value: String) : ClientListIntent()
}
