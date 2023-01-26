package com.moonlightbutterfly.cryptohub.presentation.signin

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.viewinterop.AndroidViewBinding
import coil.annotation.ExperimentalCoilApi
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.data.user.FirebaseAuthDataProvider
import com.moonlightbutterfly.cryptohub.databinding.ActivitySignInBinding
import com.moonlightbutterfly.cryptohub.presentation.core.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    private val viewModel:SignInViewModel by viewModels()

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
        if (viewModel.isUserSignedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        setContent {
            AndroidViewBinding(ActivitySignInBinding::inflate)
        }
    }

    private fun getConfigurationData(): FirebaseAuthDataProvider.ConfigurationData {
        val nightModeEnabled = runBlocking { viewModel.isNightModeEnabled.first() }
        val theme = if (nightModeEnabled) {
            R.style.Theme_CryptoHub_NoActionBar_Night
        } else {
            R.style.Theme_CryptoHub_NoActionBar_Day
        }
        return FirebaseAuthDataProvider.ConfigurationData(intentLauncher, theme)
    }

    override fun onDestroy() {
        firebaseAuthDataProvider.configurationData = null
        super.onDestroy()
    }
}