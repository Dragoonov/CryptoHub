package com.moonlightbutterfly.cryptohub.presentation.ui

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.core.app.ActivityOptionsCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.di.DaggerTestAppComponent
import com.moonlightbutterfly.cryptohub.domain.models.UserData
import com.moonlightbutterfly.cryptohub.presentation.ui.composables.AppLayout
import com.moonlightbutterfly.cryptohub.signincontrollers.GoogleSignInIntentController
import kotlinx.coroutines.FlowPreview
import org.junit.Before
import org.junit.Rule

open class CryptoHubAndroidTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    protected lateinit var navController: NavHostController

    private val launcher: ActivityResultLauncher<Intent> = object :
        ActivityResultLauncher<Intent>() {
        override fun launch(input: Intent?, options: ActivityOptionsCompat?) {
            // Fake
        }

        override fun unregister() {
            // Fake
        }

        override fun getContract(): ActivityResultContract<Intent, *> {
            return object : ActivityResultContract<Intent, Any>() {
                override fun createIntent(context: Context, input: Intent?): Intent {
                    return Intent()
                }

                override fun parseResult(resultCode: Int, intent: Intent?): Any {
                    return 1
                }
            }
        }
    }

    @FlowPreview
    @ExperimentalCoilApi
    @Before
    fun setupNavHost() {
        composeTestRule.setContent {
            val viewModelFactory = DaggerTestAppComponent.factory().create(
                LocalContext.current,
                object : GoogleSignInIntentController {
                    override fun getLauncher(
                        actionOnSuccess: (UserData) -> Unit,
                        actionOnFailure: (String) -> Unit
                    ): ActivityResultLauncher<Intent> {
                        return launcher
                    }
                }
            ).viewModelFactory()
            CompositionLocalProvider(LocalViewModelFactory provides viewModelFactory) {
                navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()
                AppLayout(navController = navController, backStackEntry = backStackEntry)
            }
        }
    }
}
