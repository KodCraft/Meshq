package az.kodcraft.core.presentation.composable.textField

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import az.kodcraft.core.presentation.composable.dropdown.CustomDropdownMenu
import az.kodcraft.core.presentation.composable.dropdown.CustomDropdownMenuItem
import az.kodcraft.core.presentation.composable.dropdown.DropdownItem
import az.kodcraft.core.presentation.theme.PrimaryTurq
import az.kodcraft.core.presentation.theme.body
import az.kodcraft.core.presentation.theme.largeHeadLine


@Composable
fun DropdownTextField(
    modifier: Modifier = Modifier,
    value: String,
    isLoading: Boolean,
    onValueChange: (String) -> Unit,
    onItemSelected: (DropdownItem) -> Unit,
    list: List<DropdownItem>,
    placeholder: String = ""
) {
    var expanded by remember { mutableStateOf(false) }
    val localSoftwareKeyboardController = LocalSoftwareKeyboardController.current
    Column(modifier = modifier) {
        Row {
            Column {
                BasicTextField(
                    value = value,
                    onValueChange = {
                        onValueChange(it)
                        expanded = it.isNotEmpty()
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            expanded = false
                            localSoftwareKeyboardController?.hide()
                        }
                    ),
                    singleLine = true,
                    cursorBrush = SolidColor(Color.White),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(Modifier.weight(1f)) {
                                if (value.isEmpty() && placeholder.isNotBlank()) {
                                    Text(
                                        text = placeholder,
                                        style = MaterialTheme.typography.largeHeadLine.copy(Color.White.copy(0.5f)),
                                    )
                                }
                                innerTextField()
                            }
                            IconButton(
                                onClick = { expanded = !expanded },
                                modifier = Modifier.align(Alignment.CenterVertically)
                            ) {
                                Icon(Icons.Filled.ArrowDropDown, "dropdown", tint = Color.White)
                            }

                        }

                    },
                    textStyle = MaterialTheme.typography.largeHeadLine.copy(Color.White),
                )
                if (isLoading) LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp),
                    color = PrimaryTurq
                )
                else
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.White)
                    )

                if (list.any())
                    CustomDropdownMenu(
                        expanded = expanded,
                        //onDismissRequest = { expanded = false },
                        modifier = Modifier.heightIn(max = 100.dp)
                            .fillMaxWidth()
                    ) {
                        items(list) { item ->
                            CustomDropdownMenuItem(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                onClick = {
                                    onItemSelected(item)
                                    expanded = false
                                    localSoftwareKeyboardController?.hide()
                                }, text = {
                                    Text(
                                        text = item.name,
                                        style = MaterialTheme.typography.body.copy(Color.White)
                                    )
                                })
                        }

                    }
            }
        }
    }
}