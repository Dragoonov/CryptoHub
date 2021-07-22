package com.moonlightbutterfly.cryptohub.ui.composables

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.moonlightbutterfly.cryptohub.ui.Screen

@Composable
fun AppLayout(navController: NavHostController,
              backStackEntry: NavBackStackEntry?,) {
    Scaffold(
        bottomBar = { AppBottomNavigation(navController, backStackEntry) }
    ) {
        NavHost(navController, startDestination = Screen.CRYPTOCURRENCIES_LIST.route, Modifier.padding(it)) {
            destinations()
        }
    }
}

@Composable
fun AppBottomNavigation(navController: NavHostController, backStackEntry: NavBackStackEntry?) {
    BottomNavigation {
        val allScreens = Screen.values()
        allScreens.forEach { screen ->
            AppBottomNavigationItem(screen = screen, navController = navController, backStackEntry)
        }
    }
}

@Composable
fun RowScope.AppBottomNavigationItem(
    screen: Screen,
    navController: NavHostController,
    backStackEntry: NavBackStackEntry?) {
    val currentRoute = backStackEntry?.destination?.route
        BottomNavigationItem(
        icon = { Icon(screen.getIconConsideringCurrentRoute(currentRoute), contentDescription = null) },
        label = { Text(stringResource(screen.nameResourceId)) },
        selected = currentRoute == screen.route,
        onClick = { navController navigateTo screen }
    )
}

private fun Screen.getIconConsideringCurrentRoute(route: String?): ImageVector {
    return if (this.route == route) {
        iconSelected
    } else {
        iconUnselected
    }
}

private infix fun NavHostController.navigateTo(screen: Screen) {
    navigate(screen.route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

fun NavGraphBuilder.destinations() {
    composable(Screen.CRYPTOCURRENCIES_LIST.route) {
        CryptocurrenciesListScreen()
    }
    composable(Screen.SETTINGS.route) {
        SettingsScreen()
    }
}
