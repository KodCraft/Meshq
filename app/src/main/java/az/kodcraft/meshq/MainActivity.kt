package az.kodcraft.meshq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import az.kodcraft.dashboard.navigation.DashboardRouteConstants
import az.kodcraft.meshq.navigation.MeshqNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            az.kodcraft.core.presentation.theme.MeshqTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    MeshqNavHost(
                        navController,
                        startDestination = DashboardRouteConstants.DASHBOARD_SCREEN
                    )
                }
            }
        }
    }
}
