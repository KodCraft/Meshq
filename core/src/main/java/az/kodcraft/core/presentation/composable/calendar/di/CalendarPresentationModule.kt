package az.kodcraft.core.presentation.composable.calendar.di

import az.kodcraft.core.presentation.composable.calendar.contract.CalendarUiState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object CalendarPresentationModule {
    @Provides
    fun provideTrainerDetailsUiState(): CalendarUiState =
        CalendarUiState()

}