package az.kodcraft.auth.presentation.login

import android.app.Activity
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import az.kodcraft.auth.presentation.login.contract.LoginEvent
import az.kodcraft.auth.presentation.login.contract.LoginIntent
import az.kodcraft.auth.presentation.login.contract.LoginUiState
import az.kodcraft.core.presentation.bases.BaseViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
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

            is LoginIntent.OnPhoneNumberChanged -> {
                flowOf(LoginUiState.PartialState.PhoneNumber(intent.phoneNumber))
            }

            is LoginIntent.CountryCode -> {
                flowOf(LoginUiState.PartialState.CountryCode(intent.countryCode))
            }

            is LoginIntent.Login -> {
                emptyFlow()
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


            is LoginUiState.PartialState.PhoneNumber -> previousState.copy(
                phoneNumber = partialState.phoneNumber
            )

            is LoginUiState.PartialState.CountryCode -> previousState.copy(
                countryCode = partialState.countryCode
            )
        }
    }

    private fun checkPhoneNumber(options: PhoneAuthOptions) {
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}