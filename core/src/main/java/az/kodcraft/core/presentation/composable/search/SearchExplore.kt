package az.kodcraft.core.presentation.composable.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import az.kodcraft.core.R
import az.kodcraft.core.presentation.theme.AccentBlue
import az.kodcraft.core.presentation.theme.body


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