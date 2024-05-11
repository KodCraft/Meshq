package az.kodcraft.workout.presentation.createWorkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import az.kodcraft.core.R
import az.kodcraft.core.presentation.bases.BasePreviewContainer
import az.kodcraft.core.presentation.composable.appBar.TopAppBar
import az.kodcraft.core.presentation.theme.PrimaryLight
import az.kodcraft.core.presentation.theme.PrimaryTurq
import az.kodcraft.core.presentation.theme.body
import az.kodcraft.core.presentation.theme.largeHeadLine
import az.kodcraft.core.utils.noRippleClickable
import az.kodcraft.workout.domain.model.CreateWorkoutDm
import az.kodcraft.workout.presentation.createWorkout.contract.CreateWorkoutUiState

@Composable
fun CreateWorkoutRoute(
    viewModel: CreateWorkoutViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    if (uiState.isLoading)
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                color = PrimaryTurq,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    else
        CreateWorkoutScreen(
            uiState = uiState,
            onSaveWorkoutClicked = { },
            navigateBack = navigateBack
        )
}


@Composable
fun CreateWorkoutScreen(
    uiState: CreateWorkoutUiState,
    onSaveWorkoutClicked: () -> Unit,
    navigateBack: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
    ) {
        TopAppBar(
            content = {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_close), contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.noRippleClickable { navigateBack() }//TODO: show warning popup before exit
                )
            }
        )
        Box(
            Modifier.fillMaxSize()
        ) {
            var text by remember { mutableStateOf("") }
            val suggestions = listOf("Apple", "Banana", "Cherry", "Date")

            // Filtered list based on the text
            val filteredSuggestions = suggestions.filter {
                it.startsWith(text, ignoreCase = true)
            }

            DropdownTextField(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 60.dp),
                value = text,
                onValueChange = { text = it },
                suggestions = filteredSuggestions,
                placeholder = "Exercise"
            )

        }

    }
}

@Composable
fun DropdownTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    suggestions: List<String>,
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
                                        style = MaterialTheme.typography.largeHeadLine.copy(Color.White),
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.White)
                )

                CustomDropdownMenu(
                    expanded = expanded,
                    //onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    suggestions.forEach { suggestion ->
                        CustomDropdownMenuItem(
                            onClick = {
                                onValueChange(suggestion)
                                expanded = false
                            }, text = {
                                Text(
                                    text = suggestion,
                                    style = MaterialTheme.typography.body.copy(Color.White)
                                )
                            })
                    }

                }
            }
        }

    }
}

@Composable
fun CustomDropdownMenu(
    expanded: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    if (expanded) {
        Column(
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
    icon: @Composable (() -> Unit)? = null // Optional icon
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.invoke() // This will draw the icon if one is provided
        Spacer(modifier = Modifier.width(16.dp)) // Space between icon and text
        text()
    }
}

@Preview
@Composable
fun CreateWorkoutPreview() = BasePreviewContainer {
    CreateWorkoutScreen(
        uiState = CreateWorkoutUiState(workout = CreateWorkoutDm.MOCK),
        onSaveWorkoutClicked = { /*TODO*/ }) {
    }
}
