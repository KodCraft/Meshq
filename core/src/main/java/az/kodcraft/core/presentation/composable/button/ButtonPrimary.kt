package az.kodcraft.core.presentation.composable.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import az.kodcraft.core.R
import az.kodcraft.core.presentation.theme.PrimaryTurq
import az.kodcraft.core.presentation.theme.buttonTypo
import az.kodcraft.core.presentation.theme.buttonTypoLight
import az.kodcraft.core.utils.noRippleClickable


@Composable
fun ButtonPrimary(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = PrimaryTurq.copy(0.7f)
) {
    Column(
        modifier = modifier
            .clip(shape = RoundedCornerShape(12.dp))
            .background(color = color),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(12.dp),
            text = text,
            style = MaterialTheme.typography.buttonTypo.copy(color = Color.White)
        )
    }
}

@Composable
fun ButtonPrimaryLight(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = PrimaryTurq.copy(0.7f),
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .noRippleClickable { onClick() }
            .clip(shape = RoundedCornerShape(6.dp))
            .background(color = color),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 16.dp),
            text = text,
            style = MaterialTheme.typography.buttonTypoLight.copy(color = Color.White)
        )
    }
}

@Composable
fun ButtonPrimaryLightWithLoader(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = PrimaryTurq.copy(0.7f),
    isLoading: Boolean = false,
    iconResId:Int,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .noRippleClickable { onClick() }
            .clip(shape = RoundedCornerShape(6.dp))
            .background(color = color).padding(vertical = 6.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.buttonTypoLight.copy(color = Color.White)
        )
        Box(modifier = Modifier
            .padding(start = 4.dp)) {
            if (isLoading)
                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White)
            else
                Icon(painter = painterResource(id = iconResId), contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
@Preview
fun PreviewButtonPrimary() {
    ButtonPrimary("meow")
}

@Composable
@Preview
fun PreviewButtonPrimaryWithLoader() {
    ButtonPrimaryLightWithLoader("meow", isLoading = false, iconResId = R.drawable.ic_done)
}
