package az.kodcraft.client.presentation.clientList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import az.kodcraft.client.domain.usecase.GetClientsListUseCase
import az.kodcraft.client.presentation.clientList.contract.ClientListEvent
import az.kodcraft.client.presentation.clientList.contract.ClientListIntent
import az.kodcraft.client.presentation.clientList.contract.ClientListUiState
import az.kodcraft.core.domain.bases.model.doOnLoading
import az.kodcraft.core.domain.bases.model.doOnSuccess
import az.kodcraft.core.presentation.bases.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    initialState: ClientListUiState,
    private val getClientsListUseCase: GetClientsListUseCase
) : BaseViewModel<ClientListUiState, ClientListUiState.PartialState, ClientListEvent, ClientListIntent>(
    savedStateHandle,
    initialState
) {
    init {
        viewModelScope.launch {
            acceptIntent(ClientListIntent.GetClientsList(""))
        }
    }

    override fun mapIntents(intent: ClientListIntent): Flow<ClientListUiState.PartialState> =
        when (intent) {
            is ClientListIntent.SearchTextChange -> flow {
                ClientListUiState.PartialState.SearchText(intent.value)
            }

            is ClientListIntent.GetClientsList -> fetchClientsList()
        }

    private fun fetchClientsList(): Flow<ClientListUiState.PartialState> = flow {
        getClientsListUseCase.execute("").doOnSuccess {
            emit(ClientListUiState.PartialState.ClientsList(it))
        }.doOnLoading {
            emit(ClientListUiState.PartialState.Loading)
        }.collect()
    }

    override fun reduceUiState(
        previousState: ClientListUiState,
        partialState: ClientListUiState.PartialState
    ) = when (partialState) {
        ClientListUiState.PartialState.Loading -> previousState.copy(
            isLoading = true, isError = false
        )

        is ClientListUiState.PartialState.SearchText -> previousState.copy(
            isLoading = false, isError = false,
            searchValue = partialState.value
        )

        is ClientListUiState.PartialState.ClientsList -> previousState.copy(
            isLoading = false,
            isError = false,
            clientList = partialState.value
        )

    }
}