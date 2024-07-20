package az.kodcraft.client.presentation.clientDetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import az.kodcraft.client.domain.model.AssignWorkoutReqDm
import az.kodcraft.client.domain.model.ClientDetailsRequestDto
import az.kodcraft.client.domain.usecase.AssignWorkoutUseCase
import az.kodcraft.client.domain.usecase.GetClientDetailsUseCase
import az.kodcraft.client.domain.usecase.GetClientMonthWorkoutsUseCase
import az.kodcraft.client.domain.usecase.GetWorkoutsListUseCase
import az.kodcraft.client.presentation.clientDetails.contract.ClientDetailsEvent
import az.kodcraft.client.presentation.clientDetails.contract.ClientDetailsIntent
import az.kodcraft.client.presentation.clientDetails.contract.ClientDetailsUiState
import az.kodcraft.core.domain.bases.model.doOnFailure
import az.kodcraft.core.domain.bases.model.doOnLoading
import az.kodcraft.core.domain.bases.model.doOnSuccess
import az.kodcraft.core.presentation.bases.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class ClientDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    initialState: ClientDetailsUiState,
    private val getClientsDetailsUseCase: GetClientDetailsUseCase,
    private val getClientMonthWorkoutsUseCase: GetClientMonthWorkoutsUseCase,
    private val getWorkoutsListUseCase: GetWorkoutsListUseCase,
    private val assignWorkoutUseCase: AssignWorkoutUseCase,
) : BaseViewModel<ClientDetailsUiState, ClientDetailsUiState.PartialState, ClientDetailsEvent, ClientDetailsIntent>(
    savedStateHandle,
    initialState
) {
    init {
        viewModelScope.launch {
            uiState.collect {
                if (it.applyFilter) {
                    acceptIntent(ClientDetailsIntent.GetWorkouts)
                }
            }
        }
    }

    override fun mapIntents(intent: ClientDetailsIntent): Flow<ClientDetailsUiState.PartialState> =
        when (intent) {
            is ClientDetailsIntent.GetClientDetails -> fetchClientsDetails(intent.id)
            is ClientDetailsIntent.GetMonthWorkouts -> fetchClientsWorkouts(intent.yearMonth)
            is ClientDetailsIntent.SelectDate -> flowOf(
                ClientDetailsUiState.PartialState.SelectDate(
                    intent.value
                )
            )

            is ClientDetailsIntent.FilterWorkoutsSearchText -> flow {
                emit(ClientDetailsUiState.PartialState.WorkoutsSearchText(intent.value))
            }

            is ClientDetailsIntent.FilterWorkoutsTags -> flow {
                emit(ClientDetailsUiState.PartialState.WorkoutsTag(intent.tag))
                emitAll(fetchWorkouts())
            }

            is ClientDetailsIntent.ResetFilter -> flow {
                emit(ClientDetailsUiState.PartialState.ResetFilter)
                emitAll(fetchWorkouts())
            }

            ClientDetailsIntent.GetWorkouts -> fetchWorkouts()
            is ClientDetailsIntent.SelectWorkoutToAssign -> flow {
                emit(ClientDetailsUiState.PartialState.WorkoutToAssign(intent.workout))
            }

            is ClientDetailsIntent.SetDateForWorkoutToAssign -> flow {
                emit(ClientDetailsUiState.PartialState.DateForWorkoutToAssign(intent.date))
            }
            ClientDetailsIntent.HideSheet -> flow {
                emit(ClientDetailsUiState.PartialState.HideSheet)
            }

            ClientDetailsIntent.AssignWorkout -> assignWorkout()
        }


    override fun reduceUiState(
        previousState: ClientDetailsUiState,
        partialState: ClientDetailsUiState.PartialState
    ) = when (partialState) {
        ClientDetailsUiState.PartialState.Loading -> previousState.copy(
            isLoading = true, isError = false
        )

        ClientDetailsUiState.PartialState.ScheduleLoading -> previousState.copy(
            isScheduleLoading = true, isError = false
        )

        ClientDetailsUiState.PartialState.WorkoutsLoading -> previousState.copy(
            areWorkoutsLoading = true, isError = false,
            applyFilter = false
        )

        is ClientDetailsUiState.PartialState.ClientsDetails -> previousState.copy(
            isLoading = false,
            isScheduleLoading = false,
            isError = false,
            clientDetails = partialState.value
        )

        is ClientDetailsUiState.PartialState.ClientWorkouts -> previousState.copy(
            isScheduleLoading = false,
            isError = false,
            clientDetails = previousState.clientDetails.copy(workoutSchedule = partialState.data),
            selectedDay = previousState.selectedDay.withYear(partialState.yearMonth.year)
                .withMonth(partialState.yearMonth.monthValue)
        )

        is ClientDetailsUiState.PartialState.SelectDate -> previousState.copy(selectedDay = partialState.value)
        is ClientDetailsUiState.PartialState.Workouts -> previousState.copy(
            filteredWorkouts = partialState.data,
            areWorkoutsLoading = false,
            applyFilter = false
        )

        is ClientDetailsUiState.PartialState.WorkoutsFilter -> previousState.copy(
            workoutsFilter = partialState.value,
            applyFilter = true
        )

        is ClientDetailsUiState.PartialState.WorkoutToAssign -> previousState.copy(
            workoutToAssign = previousState.workoutToAssign.copy(workout = partialState.value),
        )

        is ClientDetailsUiState.PartialState.DateForWorkoutToAssign -> previousState.copy(
            workoutToAssign = previousState.workoutToAssign.copy(date = partialState.value),
            showSheet = true
        )
        is ClientDetailsUiState.PartialState.HideSheet -> previousState.copy(
            showSheet = false
        )

        is ClientDetailsUiState.PartialState.WorkoutsSearchText -> {
            previousState.copy(
                workoutsFilter = previousState.workoutsFilter.copy(search = partialState.value),
                applyFilter = true
            )
        }

        is ClientDetailsUiState.PartialState.WorkoutsTag -> previousState.copy(
            workoutsFilter = previousState.workoutsFilter.copy(
                tags = previousState.workoutsFilter.tags.map {
                    if (it.id == partialState.tag.id) it.copy(
                        isSelected = !it.isSelected
                    ) else it
                }
            ),
            applyFilter = true
        )

        ClientDetailsUiState.PartialState.ResetFilter -> previousState.copy(
            workoutsFilter = previousState.workoutsFilter.copy(
                search = "",
                tags = previousState.workoutsFilter.tags.map { it.copy(isSelected = false) }),
            applyFilter = true
        )

        ClientDetailsUiState.PartialState.WorkoutAssigned -> previousState.copy(workoutToAssign = AssignWorkoutReqDm.EMPTY, showSheet = false)
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
                emit(ClientDetailsUiState.PartialState.ClientWorkouts(it, yearMonth))
            }.doOnLoading {
                emit(ClientDetailsUiState.PartialState.ScheduleLoading)
            }.doOnFailure {
                Log.e("CLIENT_WORKOUTS",it)
            }.collect()
        }

    private fun fetchWorkouts(): Flow<ClientDetailsUiState.PartialState> =
        flow {
            getWorkoutsListUseCase.execute(uiState.value.workoutsFilter)
                .doOnSuccess {
                    emit(ClientDetailsUiState.PartialState.Workouts(it))
                }.doOnLoading {
                    emit(ClientDetailsUiState.PartialState.WorkoutsLoading)
                }.collect()
        }

    private fun assignWorkout(): Flow<ClientDetailsUiState.PartialState> =
        flow {
            assignWorkoutUseCase.execute(
                uiState.value.workoutToAssign.copy(traineeId = uiState.value.clientDetails.id)
            ).doOnSuccess {
                acceptIntent(ClientDetailsIntent.GetMonthWorkouts(YearMonth.of( uiState.value.selectedDay.year, uiState.value.selectedDay.monthValue)))
                emit(ClientDetailsUiState.PartialState.WorkoutAssigned)
            }.doOnLoading {
                // emit(ClientDetailsUiState.PartialState.WorkoutsLoading)
            }.collect()
        }

}