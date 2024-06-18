package az.kodcraft.meshq

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import az.kodcraft.core.navigation.BottomBar
import az.kodcraft.core.navigation.TopLevelDestination
import az.kodcraft.core.navigation.TopLevelDestinationTrainee
import az.kodcraft.core.navigation.TopLevelDestinationTrainer
import az.kodcraft.core.navigation.isTopLevelDestinationInHierarchy
import az.kodcraft.core.presentation.theme.AccentBlue
import az.kodcraft.core.presentation.theme.body
import az.kodcraft.core.utils.noRippleClickable
import az.kodcraft.dashboard.navigation.navigateToClientsList
import az.kodcraft.dashboard.navigation.navigateToDashboard
import az.kodcraft.dashboard.navigation.navigateToExerciseLibrary
import az.kodcraft.dashboard.navigation.navigateToFinishedWorkouts
import az.kodcraft.dashboard.navigation.navigateToTrainerDashboard
import az.kodcraft.dashboard.navigation.navigateToTrainerExerciseLibrary
import az.kodcraft.dashboard.navigation.navigateToWorkoutsLibrary
import az.kodcraft.meshq.navigation.MeshqNavHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            az.kodcraft.core.presentation.theme.MeshqTheme {
                // A surface container using the 'background' color from the theme
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                val navController = rememberNavController()
                var isTrainer by remember { mutableStateOf(true) }//TODO: fetch from firebase in MainViewModel on init

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
                                    isTrainer
                                ).route,
                                switchMode = { isTrainer = !isTrainer },
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
        TopLevelDestinationTrainee.DASHBOARD -> navigateToDashboard()
        TopLevelDestinationTrainee.FINISHED_WORKOUTS -> navigateToFinishedWorkouts()
        TopLevelDestinationTrainee.EXERCISE_LIBRARY -> navigateToExerciseLibrary()


        TopLevelDestinationTrainer.TRAINER_DASHBOARD -> navigateToTrainerDashboard()
        TopLevelDestinationTrainer.WORKOUTS_LIBRARY -> navigateToWorkoutsLibrary()
        TopLevelDestinationTrainer.EXERCISE_LIBRARY -> navigateToTrainerExerciseLibrary()
        TopLevelDestinationTrainer.CLIENTS_LIST -> navigateToClientsList()
    }
}

