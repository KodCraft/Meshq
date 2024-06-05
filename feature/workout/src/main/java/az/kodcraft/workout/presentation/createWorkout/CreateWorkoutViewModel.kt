package az.kodcraft.workout.presentation.createWorkout

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import az.kodcraft.core.domain.bases.model.doOnFailure
import az.kodcraft.core.domain.bases.model.doOnLoading
import az.kodcraft.core.domain.bases.model.doOnNetworkError
import az.kodcraft.core.domain.bases.model.doOnSuccess
import az.kodcraft.core.presentation.bases.BaseViewModel
import az.kodcraft.workout.domain.model.CreateWorkoutDm
import az.kodcraft.workout.domain.usecase.GetExercisesUseCase
import az.kodcraft.workout.presentation.createWorkout.contract.CreateWorkoutEvent
import az.kodcraft.workout.presentation.createWorkout.contract.CreateWorkoutIntent
import az.kodcraft.workout.presentation.createWorkout.contract.CreateWorkoutUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class CreateWorkoutViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    initialState: CreateWorkoutUiState,
//    private val saveWorkoutUseCase: SaveWorkoutUseCase
    private val getExerciseUseCase: GetExercisesUseCase
//    private val getExerciseUseCase: GetExerciseUseCase
) : BaseViewModel<CreateWorkoutUiState, CreateWorkoutUiState.PartialState, CreateWorkoutEvent, CreateWorkoutIntent>(
    savedStateHandle,
    initialState
) {

    override fun mapIntents(intent: CreateWorkoutIntent): Flow<CreateWorkoutUiState.PartialState> =
        when (intent) {
            CreateWorkoutIntent.SaveWorkout -> TODO()
            CreateWorkoutIntent.GetExercises -> fetchExercises()
            is CreateWorkoutIntent.ChangeSearchValue -> fetchExercises(intent.value)
            is CreateWorkoutIntent.NewExerciseSelected -> flowOf(
                CreateWorkoutUiState.PartialState.SelectedExercise(
                     CreateWorkoutDm.Exercise.EMPTY.copy(id = intent.id, name =intent.name ))
            )
            CreateWorkoutIntent.UnselectExercise -> flowOf(
                CreateWorkoutUiState.PartialState.SelectedExercise(
                    CreateWorkoutDm.Exercise.EMPTY)
            )
            is CreateWorkoutIntent.SaveExerciseSets -> flowOf(
                CreateWorkoutUiState.PartialState.WorkoutExercise(
                    intent.sets
                )
            )

            is CreateWorkoutIntent.RemoveExercise ->  flowOf(
                CreateWorkoutUiState.PartialState.RemoveWorkoutExercise(
                    intent.id
                )
            )
        }

    private fun fetchExercises(searchValue: String = ""): Flow<CreateWorkoutUiState.PartialState> =
        flow {
            emit(
                CreateWorkoutUiState.PartialState.SearchText(
                    searchValue
                )
            )
            getExerciseUseCase.execute(searchValue)
                .doOnLoading {
                    Log.d("firestore_data", "loading")
                    emit(CreateWorkoutUiState.PartialState.Loading)
                }
                .doOnSuccess {
                    Log.d("firestore_data success", it.joinToString { it.name })
                    emit(CreateWorkoutUiState.PartialState.Exercises(it))
                }
                .doOnFailure {
                    Log.e("firestore_data error", it)
                }
                .doOnNetworkError {
                    Log.e("firestore_data network error", "")
                }
                .collect()
        }


    override fun reduceUiState(
        previousState: CreateWorkoutUiState,
        partialState: CreateWorkoutUiState.PartialState
    ): CreateWorkoutUiState = when (partialState) {

        CreateWorkoutUiState.PartialState.Loading -> previousState.copy(
            isLoading = true, isError = false
        )

        is CreateWorkoutUiState.PartialState.Exercises -> previousState.copy(
            isLoading = false, isError = false,
            exercises = partialState.data
        )

        is CreateWorkoutUiState.PartialState.SearchText -> previousState.copy(
            isLoading = false, isError = false,
            searchValue = partialState.value
        )

        is CreateWorkoutUiState.PartialState.SelectedExercise -> previousState.copy(
            isLoading = false, isError = false,
            selectedExercise = partialState.exercise,
            searchValue = ""
        )

        is CreateWorkoutUiState.PartialState.WorkoutExercise -> previousState.copy(
            isLoading = false, isError = false,
            workout = upsertWorkoutExercise(previousState.workout, partialState.sets),
            selectedExercise = CreateWorkoutDm.Exercise.EMPTY,
            searchValue = ""
        )

        is CreateWorkoutUiState.PartialState.RemoveWorkoutExercise -> previousState.copy(
            isLoading = false, isError = false,
            workout = previousState.workout.copy(exercises =  previousState.workout.exercises.filterNot { it.id == partialState.id })
        )
    }


    private fun upsertWorkoutExercise(
        previousWorkoutState: CreateWorkoutDm,
        sets: List<CreateWorkoutDm.Exercise.Set>
    ): CreateWorkoutDm =
        if (previousWorkoutState.exercises.any() { it.id == uiState.value.selectedExercise.id }) {
            previousWorkoutState.copy(exercises = previousWorkoutState.exercises.map {
                if (it.id == uiState.value.selectedExercise.id) it.copy(sets = sets) else it
            })
        } else {
            previousWorkoutState.copy(
                exercises = previousWorkoutState.exercises + CreateWorkoutDm.Exercise(
                    id = Random.nextLong().toString(),
                    sets = sets,
                    name = uiState.value.selectedExercise.name,
                    exerciseRefId = uiState.value.selectedExercise.id
                )
            )
        }

}
