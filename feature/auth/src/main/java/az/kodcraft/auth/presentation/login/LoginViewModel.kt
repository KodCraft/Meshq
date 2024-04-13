package az.kodcraft.auth.presentation.login

import androidx.lifecycle.SavedStateHandle
import az.kodcraft.auth.presentation.login.contract.LoginEvent
import az.kodcraft.auth.presentation.login.contract.LoginIntent
import az.kodcraft.auth.presentation.login.contract.LoginUiState
import az.kodcraft.core.presentation.bases.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    initialState: LoginUiState,
) : BaseViewModel<LoginUiState, LoginUiState.PartialState, LoginEvent, LoginIntent>(
    savedStateHandle, initialState
) {
    override fun mapIntents(intent: LoginIntent): Flow<LoginUiState.PartialState> {
        TODO("Not yet implemented")
    }

    override fun reduceUiState(
        previousState: LoginUiState, partialState: LoginUiState.PartialState
    ): LoginUiState {
        TODO("Not yet implemented")
    }

}