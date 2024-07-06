package az.kodcraft.notification.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import az.kodcraft.core.presentation.theme.bodySmallLight

@Composable
@ExperimentalMaterial3Api
fun SwipeableNotificationItem(
    content: @Composable RowScope.() -> Unit,
    onSwipeDelete: () -> Unit,
    enableDismissFromStartToEnd: Boolean = true,
    enableDismissFromEndToStart: Boolean = true
) {
    val swipeState = rememberSwipeToDismissBoxState(confirmValueChange =
    { swipeState ->
        if (swipeState == SwipeToDismissBoxValue.EndToStart && enableDismissFromEndToStart) {
            onSwipeDelete()
            true
        } else if (swipeState == SwipeToDismissBoxValue.StartToEnd && enableDismissFromStartToEnd) {
            // Handle archive action or other logic
           true
        } else {
            true
        }
    })

    SwipeToDismissBox(
        state = swipeState,
        backgroundContent = {
            if (swipeState.dismissDirection == SwipeToDismissBoxValue.StartToEnd && enableDismissFromStartToEnd) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Green)
                        .padding(8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        "Archive",
                        style = MaterialTheme.typography.bodySmallLight, color = Color.White
                    )
                }
            } else if (swipeState.dismissDirection == SwipeToDismissBoxValue.EndToStart && enableDismissFromEndToStart) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Red)
                        .padding(8.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    // Icon(painter = painterResource(id = R.drawable.ic_trash), tint = Color.White, contentDescription = "")
                    Text(
                        "Delete",
                        style = MaterialTheme.typography.bodySmallLight,
                        color = Color.White
                    )
                }
            }
        },
        modifier = Modifier.fillMaxWidth(),
        enableDismissFromStartToEnd = enableDismissFromStartToEnd,
        enableDismissFromEndToStart = enableDismissFromEndToStart,
        content = content
    )

//    LaunchedEffect(swipeState.currentValue) {
//        if (swipeState.currentValue == SwipeToDismissBoxValue.EndToStart && enableDismissFromEndToStart) {
//            onSwipeDelete()
//            swipeState.reset()
//        } else if (swipeState.currentValue == SwipeToDismissBoxValue.StartToEnd && enableDismissFromStartToEnd) {
//            // Handle archive action or other logic
//            swipeState.reset()
//        }
//    }
}
