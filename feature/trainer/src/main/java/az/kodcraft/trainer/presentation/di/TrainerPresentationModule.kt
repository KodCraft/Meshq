package az.kodcraft.trainer.presentation.di

import az.kodcraft.trainer.presentation.trainerDetails.contract.TrainerDetailsUiState
import az.kodcraft.trainer.presentation.trainerList.contract.TrainerListUiState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object TrainerPresentationModule {
    @Provides
    fun provideTrainerDetailsUiState(): TrainerDetailsUiState =
        TrainerDetailsUiState()

    @Provides
    fun provideTrainerListUiState(): TrainerListUiState =
        TrainerListUiState()
}