package az.kodcraft.notification.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.kodcraft.core.R
import az.kodcraft.core.presentation.bases.BasePreviewContainer
import az.kodcraft.core.presentation.composable.appBar.TopAppBar
import az.kodcraft.core.presentation.composable.button.ButtonPrimaryLightSmall
import az.kodcraft.core.presentation.theme.PrimaryBlue
import az.kodcraft.core.presentation.theme.PrimaryTurq
import az.kodcraft.core.presentation.theme.bodyLight
import az.kodcraft.core.presentation.theme.largeHeadLine
import az.kodcraft.notification.domain.model.NotificationListItemDm
import az.kodcraft.notification.presentation.composable.SwipeableNotificationItem
import az.kodcraft.notification.presentation.contract.NotificationIntent
import az.kodcraft.notification.presentation.contract.NotificationUiState

@Composable
fun NotificationRoute(
    viewModel: NotificationViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage.isNotBlank())
            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
    }
//                viewModel.event.collectWithLifecycle {
//                    when (it) {
//                        NotificationEvent.NavigateToDashboard -> navigateBack()
//                    }
//                }
    NotificationScreen(
        uiState = uiState,
        navigateBack = navigateBack,
        onIntent = viewModel::acceptIntent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    uiState: NotificationUiState,
    navigateBack: () -> Unit = {},
    onIntent: (NotificationIntent) -> Unit = {}
) {
    //SearchBar

    //Content
    Column(
        Modifier
            .fillMaxSize()
    ) {
        TopAppBar(
            showBackIcon = true,
            onBackClick = navigateBack,
            content = {
                SearchAndFilter()
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (uiState.isLoading)
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(color = PrimaryTurq)
                }
            else {
                if (uiState.notificationList.isEmpty())
                    Text(
                        text = "You have no notifications",
                        style = MaterialTheme.typography.bodyLight,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    items(uiState.notificationList) { notification ->
                        SwipeableNotificationItem(content = {
                            if (notification.isSubscriptionRequest)
                                SubscriptionRequestNotificationItem(
                                    notification = notification,
                                    isLoading = uiState.isAcceptSubscriptionLoading,
                                    onAcceptClick = {
                                        onIntent(
                                            NotificationIntent.AcceptSubscription(notification.id)
                                        )
                                    })
                            else
                                NotificationItem(notification)

                            HorizontalDivider(Modifier.fillMaxWidth(), thickness = 0.5.dp)
                            Spacer(modifier = Modifier.height(16.dp))
                        }, onSwipeDelete = {
                            onIntent(
                                NotificationIntent.DeleteNotification(notification.id)
                            )
                        },
                            enableDismissFromStartToEnd = false
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SubscriptionRequestNotificationItem(
    notification: NotificationListItemDm,
    isLoading: Boolean,
    onAcceptClick: () -> Unit = {}
) {
    if (isLoading)
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp),
            color = Color.Black
        )
    else
        Box(modifier = Modifier.height(0.5.dp))
    Row(
        modifier = Modifier
            .background(PrimaryBlue)
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = notification.body,
            style = MaterialTheme.typography.bodyLight,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        ButtonPrimaryLightSmall(text = "Accept", modifier = Modifier.padding(start = 16.dp)) {
            onAcceptClick()
        }
        Spacer(modifier = Modifier.width(6.dp))
    }
}

@Composable
fun NotificationItem(notification: NotificationListItemDm) {
    Row(
        modifier = Modifier
            .background(PrimaryBlue)
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = notification.body,
            style = MaterialTheme.typography.bodyLight,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
    }
}

@Composable
fun SearchAndFilter() {
    Column(Modifier.padding(start = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Notification list",
                style = MaterialTheme.typography.largeHeadLine,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = com.google.android.material.R.drawable.abc_ic_search_api_material),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(painter = painterResource(id = R.drawable.ic_filter), contentDescription = "")
        }
    }
}


@Preview
@Composable
fun NotificationListPreview() = BasePreviewContainer {
    NotificationScreen(
        uiState = NotificationUiState().copy(
            notificationList = listOf(
                NotificationListItemDm.MOCK,
                NotificationListItemDm.MOCK.copy(isSubscriptionRequest = false),
                NotificationListItemDm.MOCK,
            )
        )
    )
}