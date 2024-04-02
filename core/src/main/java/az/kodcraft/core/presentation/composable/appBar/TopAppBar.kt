package az.kodcraft.core.presentation.composable.appBar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import az.kodcraft.core.R
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon


@Composable
fun TopAppBar(
    showMenuIcon: Boolean = false,
    onMenuClick: () -> Unit = {},
    showBackIcon: Boolean = false,
    iconsColor: Color = Color.White,
    onBackClick: () -> Unit = {},
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showMenuIcon)
            Icon(
                modifier = Modifier.clickable { onMenuClick() },
                painter = painterResource(id = R.drawable.ic_menu),
                tint = iconsColor,
                contentDescription = null
            )
        else if (showBackIcon)
            Icon(
                modifier = Modifier.padding().clickable { onBackClick() },
                painter = painterResource(id = R.drawable.ic_back),
                tint = iconsColor,
                contentDescription = null
            )
    }

}
