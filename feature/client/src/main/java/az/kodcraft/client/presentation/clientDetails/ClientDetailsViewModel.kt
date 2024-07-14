package az.kodcraft.client.presentation.clientDetails

import androidx.lifecycle.SavedStateHandle
import az.kodcraft.client.domain.model.ClientDetailsRequestDto
import az.kodcraft.client.domain.usecase.GetClientDetailsUseCase
import az.kodcraft.client.domain.usecase.GetClientMonthWorkoutsUseCase
import az.kodcraft.client.presentation.clientDetails.contract.ClientDetailsEvent
import az.kodcraft.client.presentation.clientDetails.contract.ClientDetailsIntent
import az.kodcraft.client.presentation.clientDetails.contract.ClientDetailsUiState
import az.kodcraft.core.domain.bases.model.doOnLoading
import az.kodcraft.core.domain.bases.model.doOnSuccess
import az.kodcraft.core.presentation.bases.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class ClientDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    initialState: ClientDetailsUiState,
    private val getClientsDetailsUseCase: GetClientDetailsUseCase,
    private val getClientMonthWorkoutsUseCase: GetClientMonthWorkoutsUseCase,
) : BaseViewModel<ClientDetailsUiState, ClientDetailsUiState.PartialState, ClientDetailsEvent, ClientDetailsIntent>(
    savedStateHandle,
    initialState
) {
    override fun mapIntents(intent: ClientDetailsIntent): Flow<ClientDetailsUiState.PartialState> =
        when (intent) {
            is ClientDetailsIntent.GetClientDetails -> fetchClientsDetails(intent.id)
            is ClientDetailsIntent.GetMonthWorkouts -> fetchClientsWorkouts(intent.yearMonth)
            is ClientDetailsIntent.SelectDate -> flowOf(
                ClientDetailsUiState.PartialState.SelectDate(
                    intent.value
                )
            )
        }

    private fun fetchClientsDetails(
        id: String
    ): Flow<ClientDetailsUiState.PartialState> = flow {
        getClientsDetailsUseCase.execute(id)
            .doOnSuccess {
                emit(ClientDetailsUiState.PartialState.ClientsDetails(it))
            }.doOnLoading {
                emit(ClientDetailsUiState.PartialState.Loading)
            }.collect()
    }

    private fun fetchClientsWorkouts(yearMonth: YearMonth = YearMonth.now()): Flow<ClientDetailsUiState.PartialState> =
        flow {
            getClientMonthWorkoutsUseCase.execute(
                ClientDetailsRequestDto(
                    id = uiState.value.clientDetails.id,
                    yearMonth = yearMonth
                )
            ).doOnSuccess {
                emit(ClientDetailsUiState.PartialState.ClientWorkouts(it))
            }.doOnLoading {
                emit(ClientDetailsUiState.PartialState.Loading)
            }.collect()
        }

    override fun reduceUiState(
        previousState: ClientDetailsUiState,
        partialState: ClientDetailsUiState.PartialState
    ) = when (partialState) {
        ClientDetailsUiState.PartialState.Loading -> previousState.copy(
            isLoading = true, isError = false
        )

        is ClientDetailsUiState.PartialState.ClientsDetails -> previousState.copy(
            isLoading = false,
            isError = false,
            clientDetails = partialState.value
        )

        is ClientDetailsUiState.PartialState.ClientWorkouts -> previousState.copy(
            isLoading = false,
            isError = false,
            clientDetails = previousState.clientDetails.copy(workoutSchedule = partialState.data)
        )

        is ClientDetailsUiState.PartialState.SelectDate -> previousState.copy(selectedDay = partialState.value)
    }
}