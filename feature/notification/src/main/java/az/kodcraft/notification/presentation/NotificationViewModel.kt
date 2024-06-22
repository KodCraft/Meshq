package az.kodcraft.notification.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import az.kodcraft.core.domain.bases.model.doOnFailure
import az.kodcraft.core.domain.bases.model.doOnLoading
import az.kodcraft.core.domain.bases.model.doOnSuccess
import az.kodcraft.core.presentation.bases.BaseViewModel
import az.kodcraft.notification.domain.usecase.AcceptSubscriptionRequestUseCase
import az.kodcraft.notification.domain.usecase.GetNotificationsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import az.kodcraft.notification.presentation.contract.NotificationUiState
import az.kodcraft.notification.presentation.contract.NotificationEvent
import az.kodcraft.notification.presentation.contract.NotificationIntent
import kotlinx.coroutines.launch

@HiltViewModel
class NotificationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    initialState: NotificationUiState,
    private val getNotificationsUseCase: GetNotificationsListUseCase,
    private val acceptSubscriptionRequestUseCase: AcceptSubscriptionRequestUseCase
) : BaseViewModel<
        NotificationUiState,
        NotificationUiState.PartialState,
        NotificationEvent,
        NotificationIntent>(
    savedStateHandle,
    initialState
) {
    init {
        viewModelScope.launch {
            acceptIntent(NotificationIntent.GetNotifications)
        }
    }

    override fun mapIntents(intent: NotificationIntent): Flow<NotificationUiState.PartialState> =
        when (intent) {
            is NotificationIntent.GetNotifications -> fetchNotifications()
            is NotificationIntent.AcceptSubscription -> acceptSubscription(intent.notificationId)
        }

    private fun fetchNotifications(): Flow<NotificationUiState.PartialState> =
        flow {
            getNotificationsUseCase.execute().doOnSuccess {
                emit(NotificationUiState.PartialState.Notifications(it))
            }.doOnLoading {
                emit(NotificationUiState.PartialState.Loading)
            }.doOnFailure {
                emit(NotificationUiState.PartialState.Error(it))
            }.collect()
        }

    private fun acceptSubscription(notificationId: String): Flow<NotificationUiState.PartialState> =
        flow {
            acceptSubscriptionRequestUseCase.execute(
                Pair(
                    notificationId,
                    uiState.value.notificationList.first{ it.id == notificationId }.subscriptionId
                )
            ).doOnSuccess {
                emit(NotificationUiState.PartialState.AcceptSubscription(notificationId))
            }.doOnLoading {
                emit(NotificationUiState.PartialState.AcceptSubscriptionLoading)
            }.doOnFailure {
                emit(NotificationUiState.PartialState.Error(it))
            }.collect()
        }

    override fun reduceUiState(
        previousState: NotificationUiState,
        partialState: NotificationUiState.PartialState
    ) = when (partialState) {
        NotificationUiState.PartialState.Loading -> previousState.copy(
            isLoading = true,
            isError = false,
            errorMessage = ""
        )
        NotificationUiState.PartialState.AcceptSubscriptionLoading -> previousState.copy(
            isAcceptSubscriptionLoading = true,
            isError = false,
            errorMessage = ""
        )

        is NotificationUiState.PartialState.AcceptSubscription -> previousState.copy(
            isAcceptSubscriptionLoading = false,
            isError = false,
            notificationList = previousState.notificationList.filterNot { it.id == partialState.notificationId },
            errorMessage = ""
        )

        is NotificationUiState.PartialState.Notifications -> previousState.copy(
            isLoading = false,
            isError = false,
            errorMessage = "",
            notificationList = partialState.value
        )

        is NotificationUiState.PartialState.Error -> previousState.copy(
            isLoading = false,
            isError = true,
            errorMessage = partialState.message
        )
    }
}