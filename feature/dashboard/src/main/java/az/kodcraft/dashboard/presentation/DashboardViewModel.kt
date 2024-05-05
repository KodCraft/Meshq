package az.kodcraft.dashboard.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import az.kodcraft.core.domain.bases.model.doOnFailure
import az.kodcraft.core.domain.bases.model.doOnLoading
import az.kodcraft.core.domain.bases.model.doOnNetworkError
import az.kodcraft.core.domain.bases.model.doOnSuccess
import az.kodcraft.core.presentation.bases.BaseViewModel
import az.kodcraft.dashboard.domain.usecase.GetWeekDataUseCase
import az.kodcraft.dashboard.domain.usecase.GetWeekWorkoutsUseCase
import az.kodcraft.dashboard.presentation.contract.DashboardEvent
import az.kodcraft.dashboard.presentation.contract.DashboardIntent
import az.kodcraft.dashboard.presentation.contract.DashboardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    initialState: DashboardUiState,
    private val getWeekDataUseCase: GetWeekDataUseCase,
    private val getWeekWorkoutsUseCase: GetWeekWorkoutsUseCase
) : BaseViewModel<DashboardUiState, DashboardUiState.PartialState, DashboardEvent, DashboardIntent>(
    savedStateHandle, initialState
) {
    init {
        acceptIntent(DashboardIntent.Init)
    }

    override fun mapIntents(intent: DashboardIntent): Flow<DashboardUiState.PartialState> =
        when (intent) {
            DashboardIntent.Init -> flow {
                emit(
                    DashboardUiState.PartialState.Init
                )
                //acceptIntent(DashboardIntent.GetWeekData)
                acceptIntent(DashboardIntent.GetWeekWorkouts(uiState.value.selectedDay))
            }

            DashboardIntent.GetWeekData -> fetchWeekData()
            is DashboardIntent.GetWeekWorkouts -> fetchWeekWorkouts(intent.date)
            is DashboardIntent.SetDay -> flow {
                emit(DashboardUiState.PartialState.SelectedDay(intent.date, intent.index))
                acceptIntent(DashboardIntent.GetWeekWorkouts(intent.date))
            }
        }

    override fun reduceUiState(
        previousState: DashboardUiState, partialState: DashboardUiState.PartialState
    ): DashboardUiState = when (partialState) {
        DashboardUiState.PartialState.Loading -> previousState.copy(
            isLoading = true, isError = false
        )

        DashboardUiState.PartialState.Init -> previousState.copy(
            isLoading = false, isError = false, selectedDay = LocalDate.now()
        )

        is DashboardUiState.PartialState.WeekData -> previousState.copy(
            isLoading = false, isError = false, weekData = partialState.data
        )

        is DashboardUiState.PartialState.WeekWorkouts -> previousState.copy(
            isLoading = false, isError = false, weekWorkouts = partialState.data
        )

        is DashboardUiState.PartialState.SelectedDay -> previousState.copy(
            selectedDay = partialState.date, startIndex = partialState.index
        )
    }


    private fun fetchWeekData(): Flow<DashboardUiState.PartialState> = flow {
        getWeekDataUseCase.execute(
            uiState.value.selectedDay.get(
                WeekFields.of(
                    Locale.getDefault()
                ).weekOfWeekBasedYear()
            ),
        ).doOnSuccess { weekData ->
            emit(
                DashboardUiState.PartialState.WeekData(
                    weekData
                )
            )
        }.doOnFailure {
            // emit(DashboardUiState.PartialState.Error(it.message.orEmpty()))
        }.doOnLoading {
            emit(DashboardUiState.PartialState.Loading)
        }.doOnNetworkError {
            //  emit(DashboardUiState.PartialState.NetworkError)
        }.collect()
    }

    private fun fetchWeekWorkouts(date: LocalDate): Flow<DashboardUiState.PartialState> = flow {
        getWeekWorkoutsUseCase.execute(
            date
        ).doOnSuccess { weekData ->
            Log.d("FIRESTORE_CALL", "VIEWMODEL - EMIT SUCCESS")
            emit(
                DashboardUiState.PartialState.WeekWorkouts(
                    weekData
                )
            )
        }.doOnFailure {
            Log.d("FIRESTORE_CALL", "VIEWMODEL - EMIT FAILURE: $it")
            // emit(DashboardUiState.PartialState.Error(it.message.orEmpty()))
        }.doOnLoading {

            Log.d("FIRESTORE_CALL", "VIEWMODEL - EMIT LOADING")
            emit(DashboardUiState.PartialState.Loading)
        }.doOnNetworkError {
            Log.d("FIRESTORE_CALL", "VIEWMODEL - EMIT NETWORK ERROR")
            //  emit(DashboardUiState.PartialState.NetworkError)
        }.collect()
    }
}