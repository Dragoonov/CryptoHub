package com.moonlightbutterfly.cryptohub.signin

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import coil.annotation.ExperimentalCoilApi
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.moonlightbutterfly.cryptohub.FirebaseAuthDataProvider
import com.moonlightbutterfly.cryptohub.core.MainActivity
import com.moonlightbutterfly.cryptohub.presentation.R
import com.moonlightbutterfly.cryptohub.presentation.databinding.ActivitySignInBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    private val viewModel: SignInViewModel by viewModels()

    @Inject
    lateinit var firebaseAuthDataProvider: FirebaseAuthDataProvider

    private val intentLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result ->
        firebaseAuthDataProvider.actionOnResult(result)
        firebaseAuthDataProvider.configurationData = null
    }

    @OptIn(ExperimentalCoilApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuthDataProvider.configurationData = getConfigurationData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setSplashScreenTheme(firebaseAuthDataProvider.configurationData!!.theme)
        }
        installSplashScreen()
        val isUserSignedIn = viewModel.uiState.value.isUserSignedIn!!
        if (isUserSignedIn) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        setContent {
            AndroidViewBinding(ActivitySignInBinding::inflate)
        }
    }

    private fun getConfigurationData(): FirebaseAuthDataProvider.ConfigurationData {
        val nightModeEnabled = viewModel.uiState.value.nightModeEnabled!!
        val theme = if (nightModeEnabled) {
            R.style.Theme_CryptoHub_Night
        } else {
            R.style.Theme_CryptoHub_Day
        }
        return FirebaseAuthDataProvider.ConfigurationData(
            intentLauncher,
            theme,
            R.drawable.crypto_logo
        )
    }

    override fun onDestroy() {
        firebaseAuthDataProvider.configurationData = null
        super.onDestroy()
    }
}
