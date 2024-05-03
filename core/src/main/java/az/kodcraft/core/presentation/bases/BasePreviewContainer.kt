package az.kodcraft.core.presentation.bases

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BasePreviewContainer(function: @Composable () -> Unit) {
    az.kodcraft.core.presentation.theme.MeshqTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            function()
        }
    }
}
