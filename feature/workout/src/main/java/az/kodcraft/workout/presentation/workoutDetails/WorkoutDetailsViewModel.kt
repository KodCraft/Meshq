package az.kodcraft.workout.presentation.workoutDetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import az.kodcraft.core.domain.bases.model.doOnFailure
import az.kodcraft.core.domain.bases.model.doOnLoading
import az.kodcraft.core.domain.bases.model.doOnNetworkError
import az.kodcraft.core.domain.bases.model.doOnSuccess
import az.kodcraft.core.presentation.bases.BaseViewModel
import az.kodcraft.workout.domain.usecase.GetWorkoutUseCase
import az.kodcraft.workout.presentation.workoutDetails.contract.WorkoutDetailsEvent
import az.kodcraft.workout.presentation.workoutDetails.contract.WorkoutDetailsIntent
import az.kodcraft.workout.presentation.workoutDetails.contract.WorkoutDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class WorkoutDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    initialState: WorkoutDetailsUiState,
    private val getWorkoutUseCase: GetWorkoutUseCase
) : BaseViewModel<WorkoutDetailsUiState, WorkoutDetailsUiState.PartialState, WorkoutDetailsEvent, WorkoutDetailsIntent>(
    savedStateHandle,
    initialState
) {

    override fun mapIntents(intent: WorkoutDetailsIntent): Flow<WorkoutDetailsUiState.PartialState> =
        when (intent) {

            is WorkoutDetailsIntent.GetWorkoutData -> fetchWorkoutData(intent.workoutId)

        }


    override fun reduceUiState(
        previousState: WorkoutDetailsUiState,
        partialState: WorkoutDetailsUiState.PartialState

    )
            : WorkoutDetailsUiState = when (partialState) {
        WorkoutDetailsUiState.PartialState.Loading -> previousState.copy(
            isLoading = true, isError = false
        )

        is WorkoutDetailsUiState.PartialState.WorkoutData -> previousState.copy(
            isLoading = false, isError = false, workout = partialState.data
        )
    }


    private fun fetchWorkoutData(workoutId: String)
            : Flow<WorkoutDetailsUiState.PartialState> =
        flow {
            getWorkoutUseCase.execute(
                workoutId
            ).doOnSuccess { data ->
                Log.d("FIRESTORE_CALL", "VIEWMODEL - EMIT SUCCESS - DATA ID ${data?.id}")
                if (data != null)
                    emit(
                        WorkoutDetailsUiState.PartialState.WorkoutData(
                            data
                        )
                    )
            }.doOnFailure {
                Log.d("FIRESTORE_CALL", "VIEWMODEL - EMIT FAILURE: $it")
                // emit(WorkoutDetailsUiState.PartialState.Error(it.message.orEmpty()))
            }.doOnLoading {
                emit(WorkoutDetailsUiState.PartialState.Loading)
            }.doOnNetworkError {
                //  emit(WorkoutDetailsUiState.PartialState.NetworkError)
            }.collect()
        }


}