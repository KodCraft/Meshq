package az.kodcraft.workout.presentation.workoutProgress

import androidx.lifecycle.SavedStateHandle
import az.kodcraft.core.domain.bases.model.doOnFailure
import az.kodcraft.core.domain.bases.model.doOnLoading
import az.kodcraft.core.domain.bases.model.doOnNetworkError
import az.kodcraft.core.domain.bases.model.doOnSuccess
import az.kodcraft.core.presentation.bases.BaseViewModel
import az.kodcraft.workout.domain.model.WorkoutDm
import az.kodcraft.workout.domain.usecase.GetWorkoutUseCase
import az.kodcraft.workout.presentation.workoutProgress.contract.WorkoutProgressEvent
import az.kodcraft.workout.presentation.workoutProgress.contract.WorkoutProgressIntent
import az.kodcraft.workout.presentation.workoutProgress.contract.WorkoutProgressUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class WorkoutProgressViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    initialState: WorkoutProgressUiState,
    private val getWorkoutUseCase: GetWorkoutUseCase
) : BaseViewModel<WorkoutProgressUiState, WorkoutProgressUiState.PartialState, WorkoutProgressEvent, WorkoutProgressIntent>(
    savedStateHandle,
    initialState
) {

    override fun mapIntents(intent: WorkoutProgressIntent): Flow<WorkoutProgressUiState.PartialState> =
        when (intent) {
            is WorkoutProgressIntent.GetWorkoutData -> fetchWorkoutData(intent.workoutId)
            is WorkoutProgressIntent.ChangeExerciseStatus -> flowOf(
                WorkoutProgressUiState.PartialState.ExerciseStatus(
                    intent.exerciseId
                )
            )

            WorkoutProgressIntent.CompleteWorkout -> flowOf(
                WorkoutProgressUiState.PartialState.CompleteWorkout
            )

            WorkoutProgressIntent.FinishWorkout -> emptyFlow() //TODO(Implement remote request for saving finished workout)
        }


    override fun reduceUiState(
        previousState: WorkoutProgressUiState,
        partialState: WorkoutProgressUiState.PartialState

    ): WorkoutProgressUiState = when (partialState) {
        WorkoutProgressUiState.PartialState.Loading -> previousState.copy(
            isLoading = true, isError = false
        )

        is WorkoutProgressUiState.PartialState.WorkoutData -> previousState.copy(
            isLoading = false,
            isError = false,
            workout = partialState.data.copy(exercises = partialState.data.exercises.mapIndexed { index, exercise ->
                if (index == 0) exercise.copy(isCurrent = true) else exercise
            })
        )

        is WorkoutProgressUiState.PartialState.ExerciseStatus -> previousState.copy(
            isLoading = false, isError = false,
            workout = previousState.workout.copy(
                exercises = updateExerciseStatus(
                    previousState.workout.exercises,
                    partialState.exerciseId
                )
            )
        )

        WorkoutProgressUiState.PartialState.CompleteWorkout -> previousState.copy(
            isLoading = false, isError = false,
            workout = previousState.workout.copy(
                exercises = previousState.workout.exercises.map { exercise ->
                    exercise.copy(
                        isCurrent = false,
                        sets = exercise.sets.map { set -> set.copy(isComplete = true) })
                }
            )
        )
    }

    private fun updateExerciseStatus(
        exercises: List<WorkoutDm.Exercise>,
        exerciseId: String
    ): List<WorkoutDm.Exercise> {
        // Toggle completion status of the specified exercise and determine new current
        val updatedExercises = exercises.map { exercise ->
            if (exercise.id == exerciseId) {
                val newSets = exercise.sets.map { it.copy(isComplete = !it.isComplete) }
                exercise.copy(sets = newSets)
            } else {
                exercise
            }
        }
        val newCurrentIndex = updatedExercises.indexOfFirst { !it.isComplete() }

        // Update isCurrent status based on new current index
        return updatedExercises.mapIndexed { index, exercise ->
            if (index == newCurrentIndex) {
                exercise.copy(isCurrent = true)
            } else {
                exercise.copy(isCurrent = false)
            }
        }
    }


    private fun fetchWorkoutData(workoutId: String)
            : Flow<WorkoutProgressUiState.PartialState> =
        flow {
            getWorkoutUseCase.execute(
                workoutId
            ).doOnSuccess { data ->
                if (data != null)
                    emit(
                        WorkoutProgressUiState.PartialState.WorkoutData(data)
                    )
            }.doOnFailure {
                // emit(WorkoutProgressUiState.PartialState.Error(it.message.orEmpty()))
            }.doOnLoading {
                emit(WorkoutProgressUiState.PartialState.Loading)
            }.doOnNetworkError {
                //  emit(WorkoutProgressUiState.PartialState.NetworkError)
            }.collect()
        }


}