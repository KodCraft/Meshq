package az.kodcraft.auth.presentation.login

import androidx.lifecycle.SavedStateHandle
import az.kodcraft.auth.presentation.login.contract.LoginEvent
import az.kodcraft.auth.presentation.login.contract.LoginIntent
import az.kodcraft.auth.presentation.login.contract.LoginUiState
import az.kodcraft.core.presentation.bases.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    initialState: LoginUiState,
) : BaseViewModel<LoginUiState, LoginUiState.PartialState, LoginEvent, LoginIntent>(
    savedStateHandle, initialState
) {
    override fun mapIntents(intent: LoginIntent): Flow<LoginUiState.PartialState> {
        return when (intent) {
            LoginIntent.Init -> {
                emptyFlow()
            }

            LoginIntent.GetWeekData -> {
                flowOf()
            }

            is LoginIntent.GetWeekWorkouts -> {
                flowOf()
            }

            is LoginIntent.SetDay -> flow {
                emptyFlow<LoginIntent>()
            }
        }
    }

    override fun reduceUiState(
        previousState: LoginUiState, partialState: LoginUiState.PartialState
    ): LoginUiState {
        return when (partialState) {
            LoginUiState.PartialState.Loading -> previousState.copy(
                isLoading = true, isError = false
            )

            LoginUiState.PartialState.Init -> previousState.copy(
                isLoading = false, isError = false,
            )

            is LoginUiState.PartialState.WeekData -> previousState.copy(
                isLoading = false, isError = false
            )


            is LoginUiState.PartialState.SelectedDay -> previousState.copy()

            LoginUiState.PartialState.LoginUserAccount -> previousState.copy()
        }
    }

}