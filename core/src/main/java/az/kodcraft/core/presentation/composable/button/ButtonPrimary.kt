package az.kodcraft.core.presentation.composable.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import az.kodcraft.core.presentation.theme.PrimaryTurq
import az.kodcraft.core.presentation.theme.buttonTypo
import az.kodcraft.core.presentation.theme.buttonTypoLight


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
            style = MaterialTheme.typography.buttonTypoLight.copy(color = Color.White)
        )
    }
}

@Composable
@Preview
fun PreviewButtonPrimary() {
    ButtonPrimary("meow")
}
