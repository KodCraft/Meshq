package az.kodcraft.notification.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import az.kodcraft.notification.presentation.contract.NotificationUiState

@Module
@InstallIn(ViewModelComponent::class)
object NotificationPresentationModule {
    @Provides
    fun provideNotificationUiState(): NotificationUiState =
        NotificationUiState()
}