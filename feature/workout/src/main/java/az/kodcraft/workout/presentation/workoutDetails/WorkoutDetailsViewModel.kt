package az.kodcraft.workout.presentation.workoutDetails

import androidx.lifecycle.SavedStateHandle
import az.kodcraft.core.presentation.bases.BaseViewModel
import az.kodcraft.workout.presentation.workoutDetails.contract.WorkoutDetailsEvent
import az.kodcraft.workout.presentation.workoutDetails.contract.WorkoutDetailsIntent
import az.kodcraft.workout.presentation.workoutDetails.contract.WorkoutDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

@HiltViewModel
class WorkoutDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    initialState: WorkoutDetailsUiState,
//    private val getWeekDataUseCase: GetWeekDataUseCase,
//    private val getWeekWorkoutsUseCase: GetWeekWorkoutsUseCase
) : BaseViewModel<WorkoutDetailsUiState, WorkoutDetailsUiState.PartialState, WorkoutDetailsEvent, WorkoutDetailsIntent>(
    savedStateHandle,
    initialState
) {
    init {
      //  acceptIntent(WorkoutDetailsIntent.Init)
    }

    override fun mapIntents(intent: WorkoutDetailsIntent): Flow<WorkoutDetailsUiState.PartialState> {
        return emptyFlow()
    }
//        when (intent) {
//            WorkoutDetailsIntent.Init -> flow {
//                emit(
//                    WorkoutDetailsUiState.PartialState.Init
//                )
//                //acceptIntent(WorkoutDetailsIntent.GetWeekData)
//                acceptIntent(WorkoutDetailsIntent.GetWeekWorkouts(uiState.value.selectedDay))
//            }
//is WorkoutDetailsIntent.GetWeekWorkouts -> fetchWeekWorkouts(intent.date)
//
//        }

    override fun reduceUiState(
        previousState: WorkoutDetailsUiState,
        partialState: WorkoutDetailsUiState.PartialState
    ): WorkoutDetailsUiState = when (partialState) {
        WorkoutDetailsUiState.PartialState.Loading -> previousState.copy(
            isLoading = true, isError = false
        )


    }


//    private fun fetchWeekData(): Flow<WorkoutDetailsUiState.PartialState> = flow {
//        getWeekDataUseCase.execute(
//            uiState.value.selectedDay.get(
//                WeekFields.of(
//                    Locale.getDefault()
//                ).weekOfWeekBasedYear()
//            ),
//        ).doOnSuccess { weekData ->
//            emit(
//                WorkoutDetailsUiState.PartialState.WeekData(
//                    weekData
//                )
//            )
//        }.doOnFailure {
//            // emit(WorkoutDetailsUiState.PartialState.Error(it.message.orEmpty()))
//        }.doOnLoading {
//            emit(WorkoutDetailsUiState.PartialState.Loading)
//        }.doOnNetworkError {
//            //  emit(WorkoutDetailsUiState.PartialState.NetworkError)
//        }.collect()
//    }


}