package az.kodcraft.core.presentation.composable.button

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import az.kodcraft.core.presentation.theme.buttonTypo


@Composable
fun ButtonSecondary(text: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.border(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            width = 1.dp
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(12.dp),
            text = text, style = MaterialTheme.typography.buttonTypo.copy(color = Color.White)
        )
    }
}

@Composable
@Preview
fun Preview(){
    ButtonSecondary("meow")
}
