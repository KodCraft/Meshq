package az.kodcraft.dashboard.presentation.trainerDashboard

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import az.kodcraft.core.domain.UserManager
import az.kodcraft.core.presentation.bases.BaseViewModel
import az.kodcraft.core.utils.formatDateToWeeklyStringDayAndMonth
import az.kodcraft.core.utils.toLocalDate
import az.kodcraft.dashboard.data.di.ASSIGNED_WORKOUTS_COLLECTION
import az.kodcraft.dashboard.data.di.DATE
import az.kodcraft.dashboard.domain.usecase.trainer.GetPaginatedWorkoutsUseCase
import az.kodcraft.dashboard.presentation.trainerDashboard.contract.TrainerDashboardEvent
import az.kodcraft.dashboard.presentation.trainerDashboard.contract.TrainerDashboardIntent
import az.kodcraft.dashboard.presentation.trainerDashboard.contract.TrainerDashboardUiState
import com.google.firebase.Firebase
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TrainerDashboardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    initialState: TrainerDashboardUiState,
    private val getPaginatedWorkoutsUseCase: GetPaginatedWorkoutsUseCase
) : BaseViewModel<TrainerDashboardUiState, TrainerDashboardUiState.PartialState, TrainerDashboardEvent, TrainerDashboardIntent>(
    savedStateHandle,
    initialState
) {
    init {
        viewModelScope.launch {
            Log.d(
                "PAGING_FETCH",
                Firebase.firestore.collection(ASSIGNED_WORKOUTS_COLLECTION)
                    .where(Filter.equalTo("trainerId", UserManager.getUserId()))
                    .orderBy(DATE, Query.Direction.ASCENDING).get().await().map {
                        it.getTimestamp("date")?.toLocalDate()
                            ?.formatDateToWeeklyStringDayAndMonth()
                    }.joinToString()
            )
        }
        acceptIntent(TrainerDashboardIntent.Init)
    }

    override fun mapIntents(intent: TrainerDashboardIntent): Flow<TrainerDashboardUiState.PartialState> =
        when (intent) {
            TrainerDashboardIntent.Init -> fetchWorkouts()
        }


    private fun fetchWorkouts(): Flow<TrainerDashboardUiState.PartialState> = flow {
        getPaginatedWorkoutsUseCase.execute(LocalDate.now()).cachedIn(viewModelScope)
            .onEach {
                emit(TrainerDashboardUiState.PartialState.Workouts(it))
            }.collect()

    }


    override fun reduceUiState(
        previousState: TrainerDashboardUiState,
        partialState: TrainerDashboardUiState.PartialState
    ): TrainerDashboardUiState = when (partialState) {
        TrainerDashboardUiState.PartialState.Loading -> previousState.copy(
            isLoading = true, isError = false
        )

        TrainerDashboardUiState.PartialState.Init -> previousState.copy(
            isLoading = false, isError = false
        )

        is TrainerDashboardUiState.PartialState.Workouts -> previousState.copy(
            isLoading = false, isError = false,
            workouts = partialState.data
        )
    }

}