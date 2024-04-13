package az.kodcraft.auth.presentation.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.kodcraft.auth.presentation.login.contract.LoginUiState
import az.kodcraft.core.presentation.composable.appBar.TopAppBar
import az.kodcraft.core.presentation.theme.primaryTurq

@Composable
fun LoginRoute(
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToDashboard: () -> Unit,
    navigateBack: () -> Unit,
    userId: String
) {
    LaunchedEffect(userId) {
        // viewModel.acceptIntent(WorkoutDetailsIntent.GetWorkoutData(workoutId = workoutId))
    }

    val uiState by viewModel.uiState.collectAsState()
    LoginScreen(
        uiState = uiState,
        onLoginClicked = { Log.d("salam", "LoginRoute: ") },
        navigateBack = navigateBack
    )
}

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onLoginClicked: () -> Unit,
    navigateBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryTurq)
    ) {
        TopAppBar(
            showBackIcon = true,
            iconsColor = MaterialTheme.colorScheme.background,
            onBackClick = navigateBack,
            content = {
                Spacer(modifier = Modifier.weight(1f))
                //CompleteButton(onClick = {}, buttonColor = PrimaryBlue.copy(0.6f))
                Spacer(modifier = Modifier.width(12.dp))
              //  WorkoutDate(uiState.workout.date)
            }
        )
    }
}