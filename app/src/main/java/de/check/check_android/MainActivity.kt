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
import de.check.check_android.pages.pools.data.PoolState
import de.check.check_android.pages.pools.presentation.HomeScreen
import de.check.check_android.pages.pools.presentation.PoolScreenViewModel
import de.check.check_android.ui.theme.CheckAndroidTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CheckAndroidTheme {
                val homeViewModel: PoolScreenViewModel = viewModel<PoolScreenViewModel>()
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
