package az.kodcraft.client.presentation.di

import az.kodcraft.client.presentation.clientDetails.contract.ClientDetailsUiState
import az.kodcraft.client.presentation.clientList.contract.ClientListUiState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ClientPresentationModule {
    @Provides
    fun provideClientListUiState(): ClientListUiState =
        ClientListUiState()

    @Provides
    fun provideClientDetailsUiState(): ClientDetailsUiState =
        ClientDetailsUiState()
}