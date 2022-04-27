package com.moonlightbutterfly.cryptohub.presentation.ui.composables.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.presentation.ui.Screen
import com.moonlightbutterfly.cryptohub.presentation.ui.SignInFlowProvider
import com.moonlightbutterfly.cryptohub.presentation.ui.composables.CryptoAssetPanelScreen
import com.moonlightbutterfly.cryptohub.presentation.ui.composables.CryptoAssetsListScreen
import com.moonlightbutterfly.cryptohub.presentation.ui.composables.HOME_ROUTE
import com.moonlightbutterfly.cryptohub.presentation.ui.composables.LoginScreen
import com.moonlightbutterfly.cryptohub.presentation.ui.composables.SearchScreen
import com.moonlightbutterfly.cryptohub.presentation.ui.composables.SettingsScreen
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoilApi
fun NavGraphBuilder.appGraph(
    navController: NavHostController,
    signInFlowProvider: SignInFlowProvider
) {
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
    composable(Screen.SIGN_IN_PANEL.route) {
        LoginScreen(
            signInFlowProvider
        ) {
            navController.navigate(HOME_ROUTE) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }
    }
}

private infix fun NavHostController.navigateTo(route: String) {
    navigate(route)
}
