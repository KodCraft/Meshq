package az.kodcraft.auth.presentation.login.di

import az.kodcraft.auth.presentation.login.contract.LoginUiState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object LoginPresentationModule {
    @Provides
    fun provideLoginUiState(): LoginUiState = LoginUiState()

}