package az.kodcraft.client.presentation.clientList.contract

import android.os.Parcelable
import az.kodcraft.client.domain.model.ClientListItemDm
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClientListUiState(
    val clientList: List<ClientListItemDm> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val searchValue: String = "",
) : Parcelable {
    sealed class PartialState {
        data object Loading : PartialState()
        data class SearchText(val value: String) : PartialState()
        data class ClientsList(val value: List<ClientListItemDm>) : PartialState()
    }
}
