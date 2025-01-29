package de.check.check_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.check.check_android.pages.home.data.PoolState
import de.check.check_android.pages.home.presentation.HomeScreen
import de.check.check_android.pages.home.presentation.HomeScreenViewModel
import de.check.check_android.ui.theme.CheckAndroidTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CheckAndroidTheme {
                val homeViewModel: HomeScreenViewModel = viewModel<HomeScreenViewModel>()
                val poolState: PoolState by homeViewModel.state.collectAsState()

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = HomeScreen
                ) {
                    composable<HomeScreen> {
                        HomeScreen(
                            onEvent = homeViewModel::onEvent,
                            state = poolState
                        )
                    }
                }
            }
        }
    }
}
