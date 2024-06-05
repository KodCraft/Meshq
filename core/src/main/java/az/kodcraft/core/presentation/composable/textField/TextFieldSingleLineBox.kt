package az.kodcraft.core.presentation.composable.textField

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import az.kodcraft.core.presentation.theme.PrimaryBlue
import az.kodcraft.core.presentation.theme.PrimaryLight

@Composable
fun TextFieldSingleLineBox(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit = {},
    textStyle: TextStyle = TextStyle.Default,
    isEditable: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
    onClearFocus: () -> Unit = {},
    isFocused: Boolean = false,
    keyboardController: SoftwareKeyboardController? = null
) {
    val focusRequester = FocusRequester()
    var _isFocused by remember { mutableStateOf(isFocused) }

    LaunchedEffect(Unit) {
        if (isFocused) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(
                if (_isFocused) PrimaryLight.copy(alpha = 0.5f)
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
                    _isFocused = focusState.isFocused
                },
            singleLine = true,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = if (keyboardController != null) KeyboardActions(onDone = {
                focusRequester.freeFocus()
                onClearFocus()
            }) else KeyboardActions.Default
        )
    }

}

@Composable
fun RangeSelectionTextField(
    initialRange: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default,
    onRangeChanged: (String) -> Unit,
    onClearFocus: () -> Unit = {},
) {
    var startValue by remember {
        mutableStateOf(
            if (initialRange.isEmpty() || initialRange == "0") "0" else initialRange.split(
                "-"
            ).getOrElse(0) { "" })
    }
    var endValue by remember {
        mutableStateOf(
            if (initialRange.isEmpty() || initialRange == "0") "0" else initialRange.split("-")
                .getOrElse(1) { "" })
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = modifier.padding(3.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextFieldSingleLineBox(
                value = startValue,
                textStyle = textStyle,
                onValueChange = {
                    startValue = it
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next, keyboardType = KeyboardType.Number ),
                modifier = Modifier.weight(1f),
                onClearFocus = onClearFocus,
                isFocused = true,
                keyboardController = keyboardController
            )
            Text("-", modifier = Modifier.padding(horizontal = 3.dp))
            TextFieldSingleLineBox(
                value = endValue,
                textStyle = textStyle,
                onValueChange = {
                    endValue = it
                    onRangeChanged("$startValue-$endValue")
                },
                modifier = Modifier.weight(1f),
                onClearFocus = { onClearFocus() },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number ),
                keyboardController = keyboardController
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewTextFieldRange() {
    RangeSelectionTextField("0", onRangeChanged = { })
}


