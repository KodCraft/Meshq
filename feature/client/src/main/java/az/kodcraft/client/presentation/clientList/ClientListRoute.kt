package az.kodcraft.client.presentation.clientList

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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.kodcraft.client.domain.model.ClientListItemDm
import az.kodcraft.client.presentation.clientList.contract.ClientListEvent
import az.kodcraft.client.presentation.clientList.contract.ClientListIntent
import az.kodcraft.client.presentation.clientList.contract.ClientListUiState
import az.kodcraft.core.R
import az.kodcraft.core.presentation.bases.BasePreviewContainer
import az.kodcraft.core.presentation.composable.appBar.TopAppBar
import az.kodcraft.core.presentation.theme.PrimaryTurq
import az.kodcraft.core.presentation.theme.bodyLargeLight
import az.kodcraft.core.presentation.theme.bodyLight
import az.kodcraft.core.presentation.theme.largeHeadLine
import az.kodcraft.core.utils.collectWithLifecycle

@Composable
fun ClientListRoute(
    viewModel: ClientListViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    viewModel.event.collectWithLifecycle {
        when (it) {
            ClientListEvent.NavigateToDashboard -> navigateBack()
        }
    }
    ClientListScreen(
        uiState = uiState,
        navigateBack = navigateBack,
        onIntent = viewModel::acceptIntent,
    )
}


@Composable
fun ClientListScreen(
    uiState: ClientListUiState,
    navigateBack: () -> Unit = {},
    onIntent: (ClientListIntent) -> Unit = {}
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
                Box(contentAlignment = Alignment.Center,modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(color = PrimaryTurq)
                }
            else {
                if (uiState.clientList.isEmpty())
                    Text(
                        text = "You have no clients",
                        style = MaterialTheme.typography.bodyLight,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    items(uiState.clientList) { client ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_profile),
                                contentDescription = "",
                                tint = Color.White
                            )
                            Text(
                                text = client.name,
                                style = bodyLargeLight,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                        HorizontalDivider(Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SearchAndFilter() {
    Column(Modifier.padding(start = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Client list",
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
fun ClientListPreview() = BasePreviewContainer {
    ClientListScreen(
        uiState = ClientListUiState().copy(
            clientList = listOf(
                ClientListItemDm.MOCK,
                ClientListItemDm.MOCK.copy(name = "Mahammad"),
                ClientListItemDm.MOCK.copy(name = "Ilhama"),
            )
        )
    )
}