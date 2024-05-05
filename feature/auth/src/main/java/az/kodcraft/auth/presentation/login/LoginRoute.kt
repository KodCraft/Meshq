package az.kodcraft.auth.presentation.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import az.kodcraft.auth.R
import az.kodcraft.auth.presentation.login.contract.LoginUiState
import az.kodcraft.core.presentation.theme.body

@Composable
fun LoginRoute(
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToDashboard: () -> Unit,
    navigateBack: () -> Unit,
    userId: String
) {
    val uiState by viewModel.uiState.collectAsState()

    /*  LaunchedEffect(userId) {
          // viewModel.acceptIntent(WorkoutDetailsIntent.GetWorkoutData(workoutId = workoutId))
      }*/
    val context = LocalContext.current
    Toast.makeText(context, "sala aslanm", Toast.LENGTH_SHORT).show()
    LoginScreen(
        uiState = uiState,
        onLoginClicked = { Log.d("salam", "LoginRoute: ") },
    )
}

@Composable
fun LoginScreen(
    uiState: LoginUiState, onLoginClicked: () -> Unit
) {
    val backgroundImage: Painter = painterResource(id = R.drawable.login_background)
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = backgroundImage,
            contentDescription = "Background",
            modifier = Modifier.matchParentSize(), // Ensures the Image fills the Box
            contentScale = ContentScale.Crop // This will crop the image to fill the bounds if necessary
        )

        // Your login screen's content
        GymLoginScreen() // Assuming GymLoginScreen is your Composable function for login inputs

    }
}

@Composable
fun GymLoginScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Text input for the phone number
        Spacer(modifier = Modifier.weight(1f))

        PhoneNumberInput()

        Spacer(modifier = Modifier.height(16.dp))

        // Proceed button
        Button(modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonColors(
                containerColor = Color(0xFF257178),
                contentColor = Color.White,
                disabledContainerColor = Color.Blue,
                disabledContentColor = Color.DarkGray
            ),
            onClick = {
                // Handle the proceed action
            }) {
            Text(
                style = MaterialTheme.typography.titleLarge.copy(
                    color = Color.White, fontSize = 18.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp),
                text = "Davam et"
            )
        }
        Spacer(modifier = Modifier.height(26.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneNumberInput() {
    var phoneNumber by remember { mutableStateOf("") }
    val countryCodes = listOf("+994", "+1", "+91", "+33", "+81")
    var expanded by remember { mutableStateOf(false) }
    var selectedCode by remember { mutableStateOf(countryCodes[0]) }

    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "Telefon nömrənizi daxil edin",
        style = MaterialTheme.typography.titleLarge.copy(
            color = Color.White, fontSize = 24.sp
        )
    )
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        text = "Məşqlərinizə keçid etmək üçün nömrənizi daxil edin",
        style = MaterialTheme.typography.body.copy(
            color = Color.Gray, fontSize = 13.sp
        )
    )
    val commonHeight = 56.dp
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        // Prefix Dropdown Button
        Box(modifier = Modifier
            .border(1.dp, Color.White, RoundedCornerShape(4.dp))
            .clip(RoundedCornerShape(4.dp))
            .clickable { expanded = true }
            .padding(horizontal = 16.dp)  // Padding similar to OutlinedTextField's internal padding
            .heightIn(56.dp)  // Explicitly setting the height to match OutlinedTextField
            .wrapContentHeight(Alignment.CenterVertically), contentAlignment = Alignment.Center) {
            Text(
                text = selectedCode, color = Color.White, fontSize = 18.sp
            )
        }

        // Phone number input field
        Spacer(modifier = Modifier.width(8.dp))

        // Phone number input field
        BasicTextField(modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            textStyle = TextStyle(color = Color.White, fontSize = 18.sp), // Set text color and size
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Transparent)  // Set the background to transparent
                , contentAlignment = Alignment.CenterStart) {
                    // Add a label that will disappear when text is typed
                    if (phoneNumber.isEmpty()) {
                        Text(
                            text = "Telefon nömrəsi", style = LocalTextStyle.current.copy(
                                color = Color.White, fontSize = 18.sp
                            ), modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        )
                    }
                    innerTextField()  // This is the actual text field
                }
            })

        // Dropdown Menu
        DropdownMenu(
            modifier = Modifier.width(70.dp),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            countryCodes.forEach { code ->
                DropdownMenuItem(text = { Text(code) }, onClick = {
                    selectedCode = code
                    expanded = false
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDashboard() {
    LoginScreen(uiState = LoginUiState(), onLoginClicked = {})
}