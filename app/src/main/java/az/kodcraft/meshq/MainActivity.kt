package az.kodcraft.meshq

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import az.kodcraft.auth.navigation.AuthRouteConstants
import az.kodcraft.core.navigation.BottomBar
import az.kodcraft.core.navigation.TopLevelDestination
import az.kodcraft.core.presentation.theme.MeshqTheme
import az.kodcraft.dashboard.navigation.navigateToDashboard
import az.kodcraft.dashboard.navigation.navigateToExerciseLibrary
import az.kodcraft.dashboard.navigation.navigateToFinishedWorkouts
import az.kodcraft.meshq.navigation.MeshqNavHost
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //installSplashScreen()
        setContent {
            MeshqTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    val currentDestination: NavDestination? =
                        navController.currentBackStackEntryAsState().value?.destination

                    val shouldShowBottomSheet =
                        (currentDestination?.route == TopLevelDestination.DASHBOARD.route || currentDestination?.route == TopLevelDestination.EXERCISE_LIBRARY.route || currentDestination?.route == TopLevelDestination.FINISHED_WORKOUTS.route)

                    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
                        Log.d(
                            "BOTTOM_NAVIGATION",
                            "navigateToTopLevelDestination: topLevelDestination = ${topLevelDestination.route}"
                        )

                        when (topLevelDestination) {
                            TopLevelDestination.DASHBOARD -> navController.navigateToDashboard()
                            TopLevelDestination.FINISHED_WORKOUTS -> {
                                navController.navigateToFinishedWorkouts()
                            }

                            TopLevelDestination.EXERCISE_LIBRARY -> {
                                navController.navigateToExerciseLibrary()
                            }
                        }

                    }

                    Box {
                        MeshqNavHost(navController,
                            padding = PaddingValues(bottom = 76.dp),
                            startDestination = AuthRouteConstants.LOGIN_SCREEN,
                            onLoginClicked = {
                                sendVerificationCode(phoneNumber = it,
                                    onCodeSent = { verificationId, token ->
                                        Log.d(TAG, "onCreate: onCodeSent $it")
                                    },
                                    onVerificationFailed = {
                                        Log.d(TAG, "onCreate: onVerificationFailed $it")
                                    },
                                    onVerificationCompleted = {
                                        Log.d(TAG, "onCreate: onVerificationCompleted $it")
                                    })
                            })

                        if (shouldShowBottomSheet) {
                            BottomBar(
                                modifier = Modifier.align(Alignment.BottomCenter),
                                destinations = TopLevelDestination.entries,
                                onNavigateToDestination = { navigateToTopLevelDestination(it) },
                                currentDestination = currentDestination,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun sendVerificationCode(
        phoneNumber: String,
        onVerificationCompleted: (credential: PhoneAuthCredential) -> Unit,
        onVerificationFailed: (e: FirebaseException) -> Unit,
        onCodeSent: (verificationId: String, token: PhoneAuthProvider.ForceResendingToken) -> Unit,
    ) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                //signInWithPhoneAuthCredential(credential)
                onVerificationCompleted.invoke(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // Log error or notify user
                onVerificationFailed.invoke(e)
            }

            override fun onCodeSent(
                verificationId: String, token: PhoneAuthProvider.ForceResendingToken
            ) {
                // Save verificationId and token so they can be used later
                onCodeSent.invoke(verificationId, token)
            }
        }

        val options =
            PhoneAuthOptions.newBuilder(auth).setPhoneNumber(phoneNumber) // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this) // Activity (for callback binding)
                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    companion object {
        private const val TAG = "salamDaglar"
    }
}

