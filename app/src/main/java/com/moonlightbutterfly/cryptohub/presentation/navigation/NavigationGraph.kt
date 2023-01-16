package com.moonlightbutterfly.cryptohub.presentation.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.presentation.list.CryptoAssetsListScreen
import com.moonlightbutterfly.cryptohub.presentation.navigation.Screen
import com.moonlightbutterfly.cryptohub.presentation.panel.CryptoAssetPanelScreen
import com.moonlightbutterfly.cryptohub.presentation.search.SearchScreen
import com.moonlightbutterfly.cryptohub.presentation.settings.SettingsScreen
import com.moonlightbutterfly.cryptohub.presentation.ui.composables.HOME_ROUTE
import com.moonlightbutterfly.cryptohub.presentation.ui.composables.SignInScreen
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoilApi
@Composable
fun AppGraph(
    navController: NavHostController,
    padding: PaddingValues
) {
    NavHost(
        navController,
        startDestination = Screen.SIGN_IN_PANEL.route,
        modifier = Modifier.padding(padding)
    ) {
        signInGraph(navController)
        mainGraph(navController)
    }
}

@ExperimentalCoilApi
private fun NavGraphBuilder.signInGraph(
    navController: NavHostController,
) {

    val onSignedIn = {
        navController.navigate(HOME_ROUTE) {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }

    composable(Screen.SIGN_IN_PANEL.route) {
        SignInScreen(onSignedIn)
    }
}

@FlowPreview
@ExperimentalCoilApi
private fun NavGraphBuilder.mainGraph(
    navController: NavHostController,
) {
    val mainRoute = "main"
    navigation(startDestination = Screen.CRYPTO_ASSETS_LIST.route, route = mainRoute) {
        composable(Screen.CRYPTO_ASSETS_LIST.route) {
            CryptoAssetsListScreen(
                {
                    navController navigateTo "${Screen.CRYPTO_ASSET_PANEL.route}/$it"
                },
                {
                    navController navigateTo Screen.SEARCH_PANEL.route
                }
            )
        }
        composable(Screen.SETTINGS.route) {
            SettingsScreen {
                navController.navigate(Screen.SIGN_IN_PANEL.route) {
                    popUpTo(HOME_ROUTE) {
                        inclusive = true
                    }
                }
            }
        }
        composable("${Screen.CRYPTO_ASSET_PANEL.route}/{cryptoSymbol}") {
            CryptoAssetPanelScreen(it.arguments?.getString("cryptoSymbol")!!)
        }
        composable(Screen.SEARCH_PANEL.route) {
            SearchScreen(
                {
                    navController.popBackStack()
                },
                {
                    navController navigateTo "${Screen.CRYPTO_ASSET_PANEL.route}/$it"
                }
            )
        }
    }
}

private infix fun NavHostController.navigateTo(route: String) {
    navigate(route)
}
