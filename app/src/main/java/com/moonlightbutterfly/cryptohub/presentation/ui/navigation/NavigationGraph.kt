package com.moonlightbutterfly.cryptohub.presentation.ui.navigation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.presentation.ui.Screen
import com.moonlightbutterfly.cryptohub.presentation.ui.composables.CryptoAssetPanelScreen
import com.moonlightbutterfly.cryptohub.presentation.ui.composables.CryptoAssetsListScreen
import com.moonlightbutterfly.cryptohub.presentation.ui.composables.EmailSignInScreen
import com.moonlightbutterfly.cryptohub.presentation.ui.composables.HOME_ROUTE
import com.moonlightbutterfly.cryptohub.presentation.ui.composables.PhoneSignInScreen
import com.moonlightbutterfly.cryptohub.presentation.ui.composables.SearchScreen
import com.moonlightbutterfly.cryptohub.presentation.ui.composables.SettingsScreen
import com.moonlightbutterfly.cryptohub.presentation.ui.composables.SignInScreen
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoilApi
@Composable
fun AppGraph(
    navController: NavHostController,
    padding: PaddingValues
) {
    val context = LocalContext.current
    NavHost(
        navController,
        startDestination = Screen.SIGN_IN_PANEL.route,
        modifier = Modifier.padding(padding)
    ) {
        signInGraph(navController, context)
        mainGraph(navController)
    }
}

@ExperimentalCoilApi
private fun NavGraphBuilder.signInGraph(navController: NavHostController, context: Context) {

    val onSignedIn = {
        navController.navigate(HOME_ROUTE) {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }
    val onSignInFailed = { message: String ->
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
    composable(Screen.SIGN_IN_PANEL.route) {
        SignInScreen(
            onSignedIn,
            onSignInFailed,
            {
                navController navigateTo Screen.EMAIL_SIGN_IN_PANEL.route
            },
            {
                navController navigateTo Screen.PHONE_SIGN_IN_PANEL.route
            },
        )
    }
    composable(Screen.EMAIL_SIGN_IN_PANEL.route) {
        EmailSignInScreen(onSignedIn, onSignInFailed)
    }
    composable(Screen.PHONE_SIGN_IN_PANEL.route) {
        PhoneSignInScreen(onSignedIn, onSignInFailed)
    }
}

@FlowPreview
@ExperimentalCoilApi
private fun NavGraphBuilder.mainGraph(navController: NavHostController) {
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
