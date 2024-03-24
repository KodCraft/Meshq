package az.kodcraft.dashboard.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.Text


@Composable
fun DashboardRoute(
    viewModel: DashboardViewModel = hiltViewModel()
){

    DashboardScreen()
}

@Composable
fun DashboardScreen() {
    Column{
        Text(text = "hello from dashboard")
    }
}
