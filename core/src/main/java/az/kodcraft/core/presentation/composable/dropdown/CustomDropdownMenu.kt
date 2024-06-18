package az.kodcraft.core.presentation.composable.dropdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import az.kodcraft.core.presentation.theme.PrimaryLight
import az.kodcraft.core.utils.noRippleClickable


@Composable
fun CustomDropdownMenu(
    expanded: Boolean,
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit
) {
    if (expanded) {
        LazyColumn(
            modifier = modifier
                .background(PrimaryLight.copy(0.3f))
                .padding(vertical = 4.dp),
            content = content
        )
    }
}

@Composable
fun CustomDropdownMenuItem(
    text: @Composable (() -> Unit),
    onClick: () -> Unit,
    modifier: Modifier,
    icon: @Composable (() -> Unit)? = null // Optional icon
) {
    Row(
        modifier = modifier
            .noRippleClickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.invoke() // This will draw the icon if one is provided
        Spacer(modifier = Modifier.width(16.dp)) // Space between icon and text
        text()
    }
}


data class DropdownItem(val name: String, val id: String)