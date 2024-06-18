package az.kodcraft.trainer.presentation.trainerList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import az.kodcraft.core.domain.bases.model.doOnFailure
import az.kodcraft.core.domain.bases.model.doOnLoading
import az.kodcraft.core.domain.bases.model.doOnSuccess
import az.kodcraft.core.presentation.bases.BaseViewModel
import az.kodcraft.trainer.domain.usecase.GetTrainersListUseCase
import az.kodcraft.trainer.presentation.trainerList.contract.TrainerListEvent
import az.kodcraft.trainer.presentation.trainerList.contract.TrainerListIntent
import az.kodcraft.trainer.presentation.trainerList.contract.TrainerListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainerListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    initialState: TrainerListUiState,
    private val getTrainersListUseCase: GetTrainersListUseCase
) : BaseViewModel<TrainerListUiState, TrainerListUiState.PartialState, TrainerListEvent, TrainerListIntent>(
    savedStateHandle,
    initialState
) {
    init {
        viewModelScope.launch {
            acceptIntent(TrainerListIntent.GetTrainersList(""))
        }
    }

    override fun mapIntents(intent: TrainerListIntent): Flow<TrainerListUiState.PartialState> =
        when (intent) {
            is TrainerListIntent.SearchTextChange -> flow {
                emit(TrainerListUiState.PartialState.SearchText(intent.value))
                emitAll(fetchTrainersList(intent.value))
            }

            is TrainerListIntent.GetTrainersList -> fetchTrainersList()
            is TrainerListIntent.NavigateToUserProfile -> flow {
                publishEvent(
                    TrainerListEvent.NavigateToUserProfile(
                        intent.id
                    )
                )
            }
        }

    private fun fetchTrainersList(searchText: String = ""): Flow<TrainerListUiState.PartialState> =
        flow {
            getTrainersListUseCase.execute(searchText).doOnSuccess {
                emit(TrainerListUiState.PartialState.TrainersList(it))
            }.doOnLoading {
                emit(TrainerListUiState.PartialState.Loading)
            }.doOnFailure { emit(TrainerListUiState.PartialState.Error(it)) }.collect()
        }

    override fun reduceUiState(
        previousState: TrainerListUiState,
        partialState: TrainerListUiState.PartialState
    ) = when (partialState) {
        TrainerListUiState.PartialState.Loading -> previousState.copy(
            isLoading = true,
            isError = false,
            errorMessage = ""
        )

        is TrainerListUiState.PartialState.SearchText -> previousState.copy(
            isLoading = false,
            errorMessage = "",
            isError = false,
            searchValue = partialState.value
        )

        is TrainerListUiState.PartialState.TrainersList -> previousState.copy(
            isLoading = false,
            isError = false,
            errorMessage = "",
            trainerList = partialState.value
        )

        is TrainerListUiState.PartialState.Error -> previousState.copy(
            isLoading = false,
            isError = true,
            errorMessage = partialState.message
        )
    }
}