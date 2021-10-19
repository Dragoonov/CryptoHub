package com.moonlightbutterfly.cryptohub.ui.composables

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.ui.Screen

@ExperimentalCoilApi
@Composable
fun AppLayout(
    navController: NavHostController,
    backStackEntry: NavBackStackEntry?,
) {
    Scaffold(
        bottomBar = { AppBottomNavigation(navController, backStackEntry) }
    ) {
        NavHost(navController, startDestination = Screen.CRYPTOASSETS_LIST.route, Modifier.padding(it)) {
            destinations(navController)
        }
    }
}

@Composable
fun AppBottomNavigation(navController: NavHostController, backStackEntry: NavBackStackEntry?) {
    BottomNavigation {
        val allScreens = Screen.bottomNavigationScreens()
        allScreens.forEach { screen ->
            AppBottomNavigationItem(screen = screen, navController = navController, backStackEntry = backStackEntry)
        }
    }
}

@Composable
fun RowScope.AppBottomNavigationItem(
    screen: Screen,
    navController: NavHostController,
    backStackEntry: NavBackStackEntry?
) {
    val currentRoute = backStackEntry?.destination?.route
    BottomNavigationItem(
        icon = { Icon(screen.getIconConsideringCurrentRoute(currentRoute), contentDescription = null) },
        label = { Text(stringResource(screen.nameResourceId)) },
        selected = currentRoute == screen.route,
        onClick = { navController navigateTo screen.route }
    )
}

private fun Screen.getIconConsideringCurrentRoute(route: String?): ImageVector {
    return if (this.route == route) {
        iconSelected
    } else {
        iconUnselected
    }
}

private infix fun NavHostController.navigateTo(route: String) {
    navigate(route)
}

@ExperimentalCoilApi
fun NavGraphBuilder.destinations(navController: NavHostController) {
    composable(Screen.CRYPTOASSETS_LIST.route) {
        CryptoassetsListScreen {
            navController navigateTo "${Screen.CRYPTOASSET_PANEL.route}/$it"
        }
    }
    composable(Screen.SETTINGS.route) {
        SettingsScreen()
    }
    composable("${Screen.CRYPTOASSET_PANEL.route}/{cryptoSymbol}") {
        CryptoassetPanelScreen(it.arguments?.getString("cryptoSymbol")!!)
    }
}
