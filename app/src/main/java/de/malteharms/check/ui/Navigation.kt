package de.malteharms.check.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import de.malteharms.check.data.NestedRoutes
import de.malteharms.check.data.Screens
import de.malteharms.check.pages.profile.ui.ProfilePage
import de.malteharms.check.pages.cash.ui.Cash
import de.malteharms.check.pages.food.ui.Food
import de.malteharms.check.pages.home.ui.Home
import de.malteharms.check.pages.settings.ui.SettingsPage
import de.malteharms.check.pages.todo.ui.Todo
import de.malteharms.check.pages.welcome.ui.Welcome

@Composable
fun Navigation(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = NestedRoutes.MainRoute.route) {

        // navigation graph for authentication procedure
        navigation(
            startDestination = Screens.WelcomeRoute.route,
            route = NestedRoutes.AuthRoute.route
        ) {
            composable(Screens.WelcomeRoute.route) { Welcome(navController) }
            composable(Screens.LoginRoute.route) {  }
            composable(Screens.RegisterRoute.route) {  }
        }

        // navigation graph for main app usage
        navigation(
            startDestination = Screens.HomeRoute.route,
            route = NestedRoutes.MainRoute.route
        ) {
            // general pages available in main route
            composable(Screens.ProfileRoute.route) { ProfilePage(navController) }
            composable(Screens.SettingsRoute.route) { SettingsPage(navController) }

            // pages available through bottom navigation
            composable(Screens.HomeRoute.route) { Home(navController) }
            composable(Screens.CashRoute.route) { Cash(navController = navController) }
            composable(Screens.TodoRoute.route) { Todo(navController = navController) }
            composable(Screens.FoodRoute.route) { Food(navController = navController) }

        }
    }
}