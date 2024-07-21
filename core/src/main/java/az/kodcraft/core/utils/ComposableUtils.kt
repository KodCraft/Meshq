package az.kodcraft.core.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    return this
        .semantics { this.role = Role.Button }
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        )
}


@Composable
fun Modifier.topBorder(
    color: Color,
    width: Dp = 1.dp
) = this.then(
    Modifier.drawBehind {
        val strokeWidth = width.value * density
        val y = size.height - strokeWidth / 2

        drawLine(
            color,
            Offset(0f, y),
            Offset(size.width, y),
            strokeWidth
        )
    }
)

@Composable
fun Modifier.bottomBorder(
    color: Color,
    width: Dp = 1.dp
) = this.then(
    Modifier.drawBehind {
        val strokeWidthPx = width.toPx()
        val y = size.height - strokeWidthPx / 2
        drawLine(
            color = color,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = strokeWidthPx,
            cap = StrokeCap.Square
        )
    }
)

@Composable
fun Modifier.topAndBottomBorder(
    color: Color,
    width: Dp = 1.dp
) = this.then(
    Modifier.drawBehind {
        val strokeWidthPx = width.toPx()
        val halfStrokeWidth = strokeWidthPx / 2
        val topY = halfStrokeWidth
        val bottomY = size.height - halfStrokeWidth

        // Draw top border
        drawLine(
            color = color,
            start = Offset(0f, topY),
            end = Offset(size.width, topY),
            strokeWidth = strokeWidthPx,
            cap = StrokeCap.Square
        )

        // Draw bottom border
        drawLine(
            color = color,
            start = Offset(0f, bottomY),
            end = Offset(size.width, bottomY),
            strokeWidth = strokeWidthPx,
            cap = StrokeCap.Square
        )
    }
)