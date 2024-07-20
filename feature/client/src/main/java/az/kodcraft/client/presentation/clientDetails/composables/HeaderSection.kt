package az.kodcraft.client.presentation.clientDetails.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import az.kodcraft.client.domain.model.ClientDm
import az.kodcraft.core.R
import az.kodcraft.core.presentation.composable.image.ShimmerEffect
import az.kodcraft.core.presentation.theme.AccentBlue
import az.kodcraft.core.presentation.theme.PrimaryTurq
import az.kodcraft.core.presentation.theme.body
import az.kodcraft.core.presentation.theme.header
import az.kodcraft.core.utils.noRippleClickable
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

@Composable
fun HeaderSection(clientDetails: ClientDm, isLoading: Boolean, onBackClick: () -> Unit) {
    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(AccentBlue),
        ) {
            Icon(
                modifier = Modifier
                    .padding(16.dp)
                    .noRippleClickable { onBackClick() },
                painter = painterResource(id = R.drawable.ic_back),
                tint = Color.White ,
                contentDescription = null
            )
            if (isLoading)
                LinearProgressIndicator(
                    modifier = Modifier.align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .height(2.dp),
                    color = PrimaryTurq,
                    trackColor = AccentBlue
                )
        }
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp)
        ) {
            Spacer(modifier = Modifier.height(25.dp))
            if (isLoading)
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(100.dp)
                ) {
                    ShimmerEffect()
                }
            else
                clientDetails.profilePicUrl.ifEmpty { null }?.let { imageUrl ->
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .build(),
                        contentDescription = "Profile image",
                        contentScale = ContentScale.FillWidth,
                        loading = {
                            ShimmerEffect()
                        },
                        error = {
                            Image(
                                painter = painterResource(id = R.drawable.profile_image_placeholder),
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(100.dp)
                    )
                } ?: Image(
                    painter = painterResource(id = R.drawable.profile_image_placeholder),
                    contentDescription = "ProfileImage",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = clientDetails.name,
                color = Color.White,
                style = MaterialTheme.typography.header
            )

            Text(
                text = if (isLoading) "" else {
                    "@" + clientDetails.username
                },
                color = Color.Gray,
                style = MaterialTheme.typography.body,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}