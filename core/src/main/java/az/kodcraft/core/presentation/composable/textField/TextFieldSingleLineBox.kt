package az.kodcraft.core.presentation.composable.textField

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import az.kodcraft.core.presentation.theme.PrimaryBlue
import az.kodcraft.core.presentation.theme.PrimaryLight

@Composable
fun TextFieldSingleLineBox(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit, textStyle: TextStyle,
    isEditable: Boolean = true,
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()
    var isFocused by remember { mutableStateOf(false) }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(
                if (isFocused) PrimaryLight.copy(alpha = 0.5f)
                else PrimaryBlue.copy(alpha = 0.5f), RoundedCornerShape(6.dp)
            )
            .padding(6.dp)
    )
    {
        BasicTextField(
            value = value,
            readOnly = isEditable.not(),
            enabled = isEditable,
            onValueChange = onValueChange,
            modifier = Modifier
                .background(Color.Transparent)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            singleLine = true,
            textStyle = textStyle.copy(
                color = if (!isFocused) PrimaryLight
                else PrimaryBlue
            ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            })
        )
    }

}