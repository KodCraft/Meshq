package az.kodcraft.dashboard.presentation.di

import az.kodcraft.dashboard.presentation.traineeDashboard.contract.DashboardUiState
import az.kodcraft.dashboard.presentation.trainerDashboard.contract.TrainerDashboardUiState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object DashboardPresentationModule {
    @Provides
    fun provideOnBoardingUiState(): DashboardUiState =
        DashboardUiState()

    @Provides
    fun provideTrainerDashboardUiState(): TrainerDashboardUiState =
        TrainerDashboardUiState()


}