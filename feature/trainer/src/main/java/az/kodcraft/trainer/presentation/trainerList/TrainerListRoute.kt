package az.kodcraft.trainer.presentation.trainerList

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import az.kodcraft.core.R
import az.kodcraft.core.presentation.bases.BasePreviewContainer
import az.kodcraft.core.presentation.theme.AccentBlue
import az.kodcraft.core.presentation.theme.body
import az.kodcraft.core.presentation.theme.footNoteLight
import az.kodcraft.core.utils.collectWithLifecycle
import az.kodcraft.core.utils.noRippleClickable
import az.kodcraft.trainer.domain.model.TrainerListItemDm
import az.kodcraft.trainer.presentation.trainerDetails.ShimmerEffect
import az.kodcraft.trainer.presentation.trainerList.contract.TrainerListEvent
import az.kodcraft.trainer.presentation.trainerList.contract.TrainerListIntent
import az.kodcraft.trainer.presentation.trainerList.contract.TrainerListUiState
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

@Composable
fun TrainerListRoute(
    viewModel: TrainerListViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToUserProfile: (id: String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage.isNotBlank())
            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
    }
    viewModel.event.collectWithLifecycle {
        when (it) {
            TrainerListEvent.NavigateToDashboard -> navigateBack()
            is TrainerListEvent.NavigateToUserProfile -> navigateToUserProfile(it.id)
        }
    }
    TrainerListScreen(
        uiState = uiState,
        onIntent = viewModel::acceptIntent,
    )
}


@Composable
fun TrainerListScreen(
    uiState: TrainerListUiState,
    onIntent: (TrainerListIntent) -> Unit = {}
) {
    Column(
        Modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        SearchExplore(
            uiState.searchValue,
            onValueChange = { onIntent.invoke(TrainerListIntent.SearchTextChange(it)) },
            modifier = Modifier.padding(horizontal = 32.dp)
        )
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            items(uiState.trainerList) { trainer ->
                Row(
                    modifier = Modifier
                        .noRippleClickable {
                            onIntent.invoke(
                                TrainerListIntent.NavigateToUserProfile(
                                    trainer.id
                                )
                            )
                        }
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    trainer.imageUrl.ifEmpty { null }?.let { imageUrl ->
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imageUrl)
                                .build(),
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth,
                            loading = {
                                ShimmerEffect()
                            },
                            error = {
                                Image(
                                    painter = painterResource(id = az.kodcraft.trainer.R.drawable.profile_image_placeholder),
                                    contentDescription = "Profile Image",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                )
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                    } ?: Image(
                        painter = painterResource(id = az.kodcraft.trainer.R.drawable.profile_image_placeholder),
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Column(
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = trainer.username,
                            style = MaterialTheme.typography.body,
                        )
                        Text(
                            text = trainer.description,
                            style = MaterialTheme.typography.footNoteLight
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchExplore(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    BasicTextField(
        modifier = modifier,
        value = value,
        textStyle = MaterialTheme.typography.body.copy(
            color = Color.White
        ),
        cursorBrush = SolidColor(Color.White),
        onValueChange = onValueChange,
        maxLines = 1,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .height(35.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(AccentBlue),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "",
                    tint = Color.White.copy(0.5f),
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(20.dp)
                )
                Box(Modifier.weight(1f)) {
                    if (value.isBlank()) {
                        Text(
                            text = "Search",
                            style = MaterialTheme.typography.body.copy(
                                color = Color.White.copy(0.5f),
                                fontSize = 18.sp
                            )
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}

@Preview
@Composable
fun TrainerListPreview() = BasePreviewContainer {
    TrainerListScreen(
        uiState = TrainerListUiState().copy(
            trainerList = listOf(
                TrainerListItemDm.MOCK1,
                TrainerListItemDm.MOCK2,
                TrainerListItemDm.MOCK3,
                TrainerListItemDm.MOCK4,
            )
        )
    )
}