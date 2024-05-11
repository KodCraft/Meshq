package az.kodcraft.workout.presentation.createWorkout

import androidx.lifecycle.SavedStateHandle
import az.kodcraft.core.presentation.bases.BaseViewModel
import az.kodcraft.workout.presentation.createWorkout.contract.CreateWorkoutEvent
import az.kodcraft.workout.presentation.createWorkout.contract.CreateWorkoutIntent
import az.kodcraft.workout.presentation.createWorkout.contract.CreateWorkoutUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CreateWorkoutViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    initialState: CreateWorkoutUiState,
//    private val saveWorkoutUseCase: SaveWorkoutUseCase
//    private val getExerciseUseCase: GetExerciseUseCase
//    private val getExercisesUseCase: GetExercisesUseCase
) : BaseViewModel<CreateWorkoutUiState, CreateWorkoutUiState.PartialState, CreateWorkoutEvent, CreateWorkoutIntent>(
    savedStateHandle,
    initialState
) {

    override fun mapIntents(intent: CreateWorkoutIntent): Flow<CreateWorkoutUiState.PartialState> =
        when (intent) {
            CreateWorkoutIntent.SaveWorkout -> TODO()
        }


    override fun reduceUiState(
        previousState: CreateWorkoutUiState,
        partialState: CreateWorkoutUiState.PartialState
    ): CreateWorkoutUiState = when (partialState) {
        CreateWorkoutUiState.PartialState.Loading -> previousState.copy(
            isLoading = true, isError = false
        )

    }
}
