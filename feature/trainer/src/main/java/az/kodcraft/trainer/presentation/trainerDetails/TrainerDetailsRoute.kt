package az.kodcraft.trainer.presentation.trainerDetails

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.kodcraft.core.presentation.bases.BasePreviewContainer
import az.kodcraft.core.presentation.composable.button.ButtonPrimaryLightWithLoader
import az.kodcraft.core.presentation.composable.image.ShimmerEffect
import az.kodcraft.core.presentation.theme.body
import az.kodcraft.core.presentation.theme.bodyLargeLight
import az.kodcraft.core.presentation.theme.bodySmallLight
import az.kodcraft.core.presentation.theme.footNoteLight
import az.kodcraft.core.utils.collectWithLifecycle
import az.kodcraft.trainer.domain.model.TrainerDm
import az.kodcraft.trainer.presentation.trainerDetails.contract.TrainerDetailsEvent
import az.kodcraft.trainer.presentation.trainerDetails.contract.TrainerDetailsIntent
import az.kodcraft.trainer.presentation.trainerDetails.contract.TrainerDetailsUiState
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

@Composable
fun TrainerDetailsRoute(
    viewModel: TrainerDetailsViewModel = hiltViewModel(),
    userId: String,
    navigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = userId) {
        viewModel.acceptIntent(TrainerDetailsIntent.GetTrainerDetails(userId))

    }
    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage.isNotBlank())
            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
    }

    viewModel.event.collectWithLifecycle {
        when (it) {
            TrainerDetailsEvent.NavigateToDashboard -> navigateBack()
        }
    }
    TrainerDetailsScreen(
        uiState = uiState,
        onIntent = viewModel::acceptIntent,
    )
}


@Composable
fun TrainerDetailsScreen(
    uiState: TrainerDetailsUiState,
    onIntent: (TrainerDetailsIntent) -> Unit = {}
) {
    Column(
        Modifier.fillMaxSize()
    ) {
        if (uiState.isLoading.not())
            ProfileHeader(
                uiState.trainer,
                isButtonLoading = uiState.isSendRequestLoading,
                onSendRequest = { onIntent.invoke(TrainerDetailsIntent.SendSubscriptionRequest) },
                onUnSendRequest = { onIntent.invoke(TrainerDetailsIntent.UnSendSubscriptionRequest) })
    }
}

@Composable
fun ProfileHeader(
    trainer: TrainerDm,
    onSendRequest: () -> Unit,
    onUnSendRequest: () -> Unit,
    isButtonLoading: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
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
                            painter = painterResource(id = az.kodcraft.core.R.drawable.profile_image_placeholder),
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(90.dp)
                )
            } ?: Icon(
                painter = painterResource(id = az.kodcraft.core.R.drawable.profile_image_placeholder),
                contentDescription = "ProfileImage",
                modifier = Modifier.size(90.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            ProfileStats(
                trainer.stats,
                Modifier
                    .size(80.dp)
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )

        }

        Spacer(modifier = Modifier.height(18.dp))
        Text(text = trainer.username, style = MaterialTheme.typography.body)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = trainer.bio,
            style = MaterialTheme.typography.footNoteLight,
            modifier = Modifier.fillMaxWidth(0.65f)
        )
        Spacer(modifier = Modifier.height(21.dp))
        ButtonPrimaryLightWithLoader(
            text = trainer.subState.btnTitle,
            onClick = { when(trainer.subState){
                TrainerDm.SubStatus.REQUEST_SENT -> onUnSendRequest()
                TrainerDm.SubStatus.SUBSCRIBED -> onUnSendRequest()
                TrainerDm.SubStatus.NONE -> onSendRequest()
            }
            },
            isLoading = isButtonLoading,
            color = trainer.subState.color ,
            iconResId = trainer.subState.icon
        )
    }
}

@Composable
fun ProfileStats(stats: TrainerDm.TrainerStatsDm, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(text = stats.experienceYears.toString(), style = bodyLargeLight)
            Text(text = "years", style = MaterialTheme.typography.bodySmallLight)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(text = stats.studentsCount.toString(), style = bodyLargeLight)
            Text(text = "students", style = MaterialTheme.typography.bodySmallLight)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(text = stats.averageRating.toString(), style = bodyLargeLight)
            Text(text = "rating", style = MaterialTheme.typography.bodySmallLight)
        }
    }
}


@Preview
@Composable
fun TrainerDetailsPreview() = BasePreviewContainer {
    TrainerDetailsScreen(uiState = TrainerDetailsUiState().copy(trainer = TrainerDm.MOCK))
}