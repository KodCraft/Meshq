package az.kodcraft.core.navigation

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import az.kodcraft.core.presentation.theme.AccentBlue
import az.kodcraft.core.utils.noRippleClickable


@Composable
fun NavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Surface(
        color = AccentBlue,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 4.dp,
        modifier = modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }

}

@Composable
fun RowScope.NavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: Int,
    selectedIcon: Int,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .fillMaxHeight()
            .weight(1.0f)
            .noRippleClickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val iconSize by animateDpAsState(targetValue = if (selected) 28.dp else 24.dp, label = "")
        Icon(
            painter = painterResource(id = if (selected) selectedIcon else icon),
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.size(iconSize)

        )
        AnimatedVisibility(visible = selected) {
            Log.d("BOTTOM_NAVIGATION", "AnimatedVisibility: SELECTED = $selected")
            Box(
                Modifier
                    .padding(top = 4.dp)
                    .clip(CircleShape)
                    .size(5.dp)
                    .background(Color.LightGray)
            )
        }
    }

}