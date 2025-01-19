package de.check.check_android

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.check.check_android.pages.home.data.HomeState
import de.check.check_android.pages.home.presentation.HomeScreen
import de.check.check_android.pages.home.presentation.HomeScreenViewModel


@Composable
fun Navigation() {

    val homeViewModel: HomeScreenViewModel = viewModel<HomeScreenViewModel>()
    val homeState: HomeState by homeViewModel.state.collectAsState()

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeScreen
    ) {

        composable<HomeScreen> {
            HomeScreen(
                onEvent = homeViewModel::onEvent,
                state = homeState
            )
        }


    }

}