package az.kodcraft.dashboard.presentation.trainerDashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.kodcraft.core.domain.UserManager
import az.kodcraft.core.presentation.bases.BasePreviewContainer
import az.kodcraft.core.presentation.composable.appBar.TopAppBar
import az.kodcraft.core.presentation.theme.PrimaryLight
import az.kodcraft.core.presentation.theme.body
import az.kodcraft.core.utils.noRippleClickable
import az.kodcraft.dashboard.presentation.traineeDashboard.contract.DashboardIntent
import az.kodcraft.dashboard.presentation.traineeDashboard.contract.DashboardUiState


@Composable
fun TrainerDashboardRoute(
    viewModel: TrainerDashboardViewModel = hiltViewModel(),
    onNavigateToCreateWorkout: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    switchMode: () -> Unit = {},
    onMenuClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    TrainerDashboardScreen(
        uiState,
        onIntent = { viewModel.acceptIntent(it) },
        onNavigateToCreateWorkout = onNavigateToCreateWorkout,
        onNavigateToNotifications = onNavigateToNotifications,
        switchMode = switchMode,
        onMenuClick = onMenuClick
    )
}

@Composable
fun TrainerDashboardScreen(
    uiState: DashboardUiState,
    onIntent: (DashboardIntent) -> Unit = {},
    onNavigateToCreateWorkout: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    switchMode: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(showMenuIcon = true, onMenuClick = onMenuClick) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                UserManager.getUserFullName(),
                style = MaterialTheme.typography.body,
                modifier = Modifier.padding(end = 12.dp)
            )
            NotificationIcon(onNavigateToNotifications)
            Icon(
                painter = painterResource(id = az.kodcraft.core.R.drawable.ic_profile),
                tint = Color.White,
                contentDescription = "go to profile",
                modifier = Modifier.noRippleClickable { switchMode() }
            )
        }
        Box(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("TRAINER DASHBOARD UNDER CONSTRUCTION ... ", textAlign = TextAlign.Center)
                }
            }

            FloatingActionButton(
                onClick = onNavigateToCreateWorkout,
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = PrimaryLight
            ) {
                Icon(
                    painter = painterResource(id = az.kodcraft.core.R.drawable.ic_add_data),
                    contentDescription = "",
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun NotificationIcon(onClick: () -> Unit) {

    Icon(
        painter = painterResource(id = az.kodcraft.core.R.drawable.ic_notification_bell),
        tint = Color.White,
        contentDescription = "go to notifications",
        modifier = Modifier
            .noRippleClickable { onClick() }
            .padding(end = 6.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewTrainerDashboard() = BasePreviewContainer {
    TrainerDashboardScreen(
        DashboardUiState()
    )
}




