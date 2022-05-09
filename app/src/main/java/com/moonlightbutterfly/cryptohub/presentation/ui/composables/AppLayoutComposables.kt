package com.moonlightbutterfly.cryptohub.presentation.ui.composables

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.presentation.ui.Screen
import com.moonlightbutterfly.cryptohub.presentation.ui.Screen.Companion.isSignInScreen
import com.moonlightbutterfly.cryptohub.presentation.ui.navigation.AppGraph
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoilApi
@Composable
fun AppLayout(
    navController: NavHostController,
    backStackEntry: NavBackStackEntry?
) {
    Scaffold(
        bottomBar = {
            if (navController.currentDestination?.route?.isSignInScreen() == false) {
                AppBottomNavigation(navController, backStackEntry)
            }
        }
    ) {
        AppGraph(navController = navController, padding = it)
    }
}

@Composable
fun AppBottomNavigation(navController: NavHostController, backStackEntry: NavBackStackEntry?) {
    BottomNavigation {
        val allScreens = Screen.bottomNavigationScreens()
        allScreens.forEach { screen ->
            AppBottomNavigationItem(
                screen = screen,
                navController = navController,
                backStackEntry = backStackEntry
            )
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
        icon = {
            Icon(
                screen.getIconConsideringCurrentRoute(currentRoute),
                contentDescription = null
            )
        },
        label = { Text(stringResource(screen.nameResourceId)) },
        selected = currentRoute == screen.route,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(HOME_ROUTE)
                launchSingleTop = true
            }
        }
    )
}

private fun Screen.getIconConsideringCurrentRoute(route: String?): ImageVector {
    return if (this.route == route) {
        iconSelected
    } else {
        iconUnselected
    }
}

val HOME_ROUTE = Screen.CRYPTO_ASSETS_LIST.route
