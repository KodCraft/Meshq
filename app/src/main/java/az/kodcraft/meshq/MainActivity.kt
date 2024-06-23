package az.kodcraft.meshq

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import az.kodcraft.client.navigation.navigateToClientList
import az.kodcraft.core.domain.UserManager
import az.kodcraft.core.domain.UserManager.UserRole.TRAINER
import az.kodcraft.core.domain.UserManager.userRole
import az.kodcraft.core.presentation.theme.AccentBlue
import az.kodcraft.core.presentation.theme.body
import az.kodcraft.core.utils.noRippleClickable
import az.kodcraft.dashboard.navigation.navigateToDashboard
import az.kodcraft.dashboard.navigation.navigateToTrainerDashboard
import az.kodcraft.meshq.navigation.BottomBar
import az.kodcraft.meshq.navigation.MeshqNavHost
import az.kodcraft.meshq.navigation.TopLevelDestination
import az.kodcraft.meshq.navigation.TopLevelDestinationTrainee
import az.kodcraft.meshq.navigation.TopLevelDestinationTrainer
import az.kodcraft.meshq.navigation.isTopLevelDestinationInHierarchy
import az.kodcraft.meshq.navigation.topLevelNavOptions
import az.kodcraft.trainer.navigation.navigateToTrainersList
import az.kodcraft.workout.navigation.navigateToWorkoutsLibrary
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                showPermissionRationale()
            }
        }
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FIREBASE_NOTIFICATIONS", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            Log.d("FIREBASE_NOTIFICATIONS", token)
        })


        setContent {

            az.kodcraft.core.presentation.theme.MeshqTheme {
                // A surface container using the 'background' color from the theme
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                val navController = rememberNavController()
                val userRole by userRole.collectAsState() //TODO: fetch from firebase in MainViewModel on init

                val currentDestination: NavDestination? = navController
                    .currentBackStackEntryAsState().value?.destination
                val shouldShowBottomSheet =
                    currentDestination?.route in TopLevelDestinationTrainee.entries.map { it.route }
                val shouldShowDrawer =
                    currentDestination?.route in TopLevelDestinationTrainer.entries.map { it.route }


                ModalNavigationDrawer(
                    drawerContent = {
                        DrawerContent(destinations = TopLevelDestinationTrainer.entries.toList(),
                            currentDestination = currentDestination,
                            onNavigateToDestination = {
                                scope.launch { drawerState.close() }
                                navController.navigateToTopLevelDestination(
                                    it
                                )
                            })
                    },
                    gesturesEnabled = shouldShowDrawer,
                    drawerState = drawerState
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Box {
                            MeshqNavHost(
                                navController,
                                padding = PaddingValues(bottom = 76.dp),
                                startDestination = TopLevelDestination.getStartDestinationForRole(
                                    userRole == TRAINER
                                ).route,
                                switchMode = { UserManager.switchUserRole() },
                                onMenuClick = { scope.launch { drawerState.open() } }
                            )

                            if (shouldShowBottomSheet) {
                                BottomBar(
                                    modifier = Modifier.align(Alignment.BottomCenter),
                                    destinations = TopLevelDestinationTrainee.entries.toList(),
                                    onNavigateToDestination = {
                                        navController.navigateToTopLevelDestination(
                                            it
                                        )
                                    },
                                    currentDestination = currentDestination,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showPermissionRationale() {
        AlertDialog.Builder(this)
            .setTitle("Notification Permission Needed")
            .setMessage("This app needs the notification permission to send you important updates.")
            .setPositiveButton("OK") { dialog, _ ->
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted
        } else {
            // Permission is denied
        }
    }

    companion object {
        private const val REQUEST_CODE_NOTIFICATION = 1001
    }
}

@Composable
fun DrawerContent(
    destinations: List<TopLevelDestinationTrainer>,
    onNavigateToDestination: (TopLevelDestinationTrainer) -> Unit,
    currentDestination: NavDestination?,
) {
    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.7f)
            .background(AccentBlue.copy(0.8f))
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            DrawerItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                selectedIcon = destination.selectedIconId,
                text = destination.title
            )
        }
    }
}

@Composable
fun DrawerItem(
    selected: Boolean,
    onClick: () -> Unit,
    text: String,
    selectedIcon: Int
) {
    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = selectedIcon), contentDescription = "",
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(if (selected) 24.dp else 18.dp),
            tint = Color.White,
        )
        Text(
            text,
            modifier = Modifier.noRippleClickable { onClick() },
            style = if (selected) MaterialTheme.typography.body.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            else MaterialTheme.typography.body.copy(
                color = Color.White,
                fontSize = 18.sp
            )
        )
    }
}


fun NavController.navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
    Log.d(
        "BOTTOM_NAVIGATION",
        "navigateToTopLevelDestination: topLevelDestination = ${topLevelDestination.route}"
    )

    when (topLevelDestination) {
        TopLevelDestinationTrainee.DASHBOARD -> navigateToDashboard(topLevelNavOptions)
        TopLevelDestinationTrainee.EXPLORE_TRAINERS -> navigateToTrainersList(topLevelNavOptions)
        TopLevelDestinationTrainee.MY_PROGRESS -> {}// navigateToExerciseLibrary()


        TopLevelDestinationTrainer.TRAINER_DASHBOARD -> navigateToTrainerDashboard(
            topLevelNavOptions
        )

        TopLevelDestinationTrainer.WORKOUTS_LIBRARY -> navigateToWorkoutsLibrary(topLevelNavOptions)
        TopLevelDestinationTrainer.EXERCISE_LIBRARY -> {}// navigateToTrainerExerciseLibrary()
        TopLevelDestinationTrainer.CLIENTS_LIST -> navigateToClientList(topLevelNavOptions)
    }
}

