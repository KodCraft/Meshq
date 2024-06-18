package az.kodcraft.trainer.presentation.trainerDetails

import androidx.lifecycle.SavedStateHandle
import az.kodcraft.core.domain.bases.model.doOnFailure
import az.kodcraft.core.domain.bases.model.doOnLoading
import az.kodcraft.core.domain.bases.model.doOnSuccess
import az.kodcraft.core.presentation.bases.BaseViewModel
import az.kodcraft.trainer.domain.usecase.GetTrainerDetailsUseCase
import az.kodcraft.trainer.domain.usecase.SendSubscriptionRequestUseCase
import az.kodcraft.trainer.domain.usecase.UnsendSubscriptionRequestUseCase
import az.kodcraft.trainer.presentation.trainerDetails.contract.TrainerDetailsEvent
import az.kodcraft.trainer.presentation.trainerDetails.contract.TrainerDetailsIntent
import az.kodcraft.trainer.presentation.trainerDetails.contract.TrainerDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class TrainerDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    initialState: TrainerDetailsUiState,
    private val getTrainerDetailsUseCase: GetTrainerDetailsUseCase,
    private val sendSubscriptionRequestUseCase: SendSubscriptionRequestUseCase,
    private val unsendSubscriptionRequestUseCase: UnsendSubscriptionRequestUseCase,
) : BaseViewModel<TrainerDetailsUiState, TrainerDetailsUiState.PartialState, TrainerDetailsEvent, TrainerDetailsIntent>(
    savedStateHandle,
    initialState
) {

    override fun mapIntents(intent: TrainerDetailsIntent): Flow<TrainerDetailsUiState.PartialState> =
        when (intent) {

            is TrainerDetailsIntent.GetTrainerDetails -> fetchTrainerDetails(intent.id)
            TrainerDetailsIntent.SendSubscriptionRequest -> sendSubscriptionRequest()

            TrainerDetailsIntent.UnSendSubscriptionRequest -> unSendSubscriptionRequest()

        }

    private fun fetchTrainerDetails(id:String): Flow<TrainerDetailsUiState.PartialState> =
        flow {
            getTrainerDetailsUseCase.execute(id).doOnSuccess {
                emit(TrainerDetailsUiState.PartialState.Trainer(it))
            }.doOnLoading {
                emit(TrainerDetailsUiState.PartialState.Loading)
            }.doOnFailure { emit(TrainerDetailsUiState.PartialState.Error(it)) }.collect()
        }

   private fun sendSubscriptionRequest(): Flow<TrainerDetailsUiState.PartialState> =
        flow { //TODO Send notification to request receiver
            sendSubscriptionRequestUseCase.execute(uiState.value.trainer.id).doOnSuccess {
                emit(TrainerDetailsUiState.PartialState.SendRequestComplete)
            }.doOnLoading {
                emit(TrainerDetailsUiState.PartialState.LoadingSendRequest)
            }.doOnFailure { emit(TrainerDetailsUiState.PartialState.Error(it)) }.collect()
        }
   private fun unSendSubscriptionRequest(): Flow<TrainerDetailsUiState.PartialState> =
        flow { //TODO Send notification to request receiver
            unsendSubscriptionRequestUseCase.execute(uiState.value.trainer.id).doOnSuccess {
                emit(TrainerDetailsUiState.PartialState.UnSendRequestComplete)
            }.doOnLoading {
                emit(TrainerDetailsUiState.PartialState.LoadingSendRequest)
            }.doOnFailure { emit(TrainerDetailsUiState.PartialState.Error(it)) }.collect()
        }

    override fun reduceUiState(
        previousState: TrainerDetailsUiState,
        partialState: TrainerDetailsUiState.PartialState
    ) = when (partialState) {
        TrainerDetailsUiState.PartialState.Loading -> previousState.copy(
            isLoading = true,
            isError = false,
            errorMessage = ""
        )

        is TrainerDetailsUiState.PartialState.SearchText -> previousState.copy(
            isLoading = false,
            errorMessage = "",
            isError = false,
        )

        is TrainerDetailsUiState.PartialState.Trainer -> previousState.copy(
            isLoading = false,
            isError = false,
            errorMessage = "",
            trainer = partialState.value
        )

        is TrainerDetailsUiState.PartialState.Error -> previousState.copy(
            isLoading = false,
            isError = true,
            errorMessage = partialState.message
        )

        TrainerDetailsUiState.PartialState.LoadingSendRequest -> previousState.copy(
            isSendRequestLoading = true,
        )
        TrainerDetailsUiState.PartialState.SendRequestComplete -> previousState.copy(
            isLoading = false,
            isSendRequestLoading = false,
            trainer = previousState.trainer.copy(isRequestSent = true)
        )
        TrainerDetailsUiState.PartialState.UnSendRequestComplete -> previousState.copy(
            isLoading = false,
            isSendRequestLoading = false,
            trainer = previousState.trainer.copy(isRequestSent = false)
        )
    }
}