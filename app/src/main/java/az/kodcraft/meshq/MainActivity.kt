package az.kodcraft.meshq

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import az.kodcraft.auth.navigation.AuthRouteConstants
import az.kodcraft.core.presentation.theme.MeshqTheme
import az.kodcraft.dashboard.navigation.DashboardRouteConstants
import az.kodcraft.dashboard.navigation.navigateToDashboard
import az.kodcraft.meshq.navigation.MeshqNavHost
import az.kodcraft.core.navigation.TopLevelDestination
import az.kodcraft.core.navigation.BottomBar
import az.kodcraft.dashboard.navigation.navigateToExerciseLibrary
import az.kodcraft.dashboard.navigation.navigateToFinishedWorkouts
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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

                    val currentDestination: NavDestination? = navController
                        .currentBackStackEntryAsState().value?.destination

                    val shouldShowBottomSheet =
                        (currentDestination?.route == TopLevelDestination.DASHBOARD.route ||
                                currentDestination?.route == TopLevelDestination.EXERCISE_LIBRARY.route ||
                                currentDestination?.route == TopLevelDestination.FINISHED_WORKOUTS.route)

                    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
                        Log.d("BOTTOM_NAVIGATION", "navigateToTopLevelDestination: topLevelDestination = ${topLevelDestination.route}")

                        when (topLevelDestination) {
                            TopLevelDestination.DASHBOARD -> navController.navigateToDashboard()
                            TopLevelDestination.FINISHED_WORKOUTS ->{navController.navigateToFinishedWorkouts()}
                            TopLevelDestination.EXERCISE_LIBRARY -> {navController.navigateToExerciseLibrary()}
                        }

                    }

                    Box {

                        MeshqNavHost(
                            navController,
                            padding = PaddingValues(bottom = 76.dp),
                            startDestination = TopLevelDestination.DASHBOARD.route
                        )

                        if (shouldShowBottomSheet) {
                            BottomBar(
                                modifier = Modifier.align(Alignment.BottomCenter),
                                destinations = TopLevelDestination.entries,
                                onNavigateToDestination = {navigateToTopLevelDestination(it)},
                                currentDestination = currentDestination,
                            )
                        }
                    }
                }
            }
        }
    }
}

