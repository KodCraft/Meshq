package az.kodcraft.workout.presentation.di

import az.kodcraft.workout.presentation.workoutDetails.contract.WorkoutDetailsUiState
import az.kodcraft.workout.presentation.workoutProgress.contract.WorkoutProgressUiState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object WorkoutPresentationModule {
    @Provides
    fun provideWorkoutDetailsUiState(): WorkoutDetailsUiState =
        WorkoutDetailsUiState()
    @Provides
    fun provideWorkoutProgressUiState(): WorkoutProgressUiState =
        WorkoutProgressUiState()


}