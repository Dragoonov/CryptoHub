package com.moonlightbutterfly.cryptohub.data.user

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.StyleRes
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interface for communication between Firebase authentication framework and android classes:
 * ie. registering contracts and launching intents.
 */
@Singleton
class FirebaseAuthDataProviderImpl @Inject constructor(): FirebaseAuthDataProvider {

    override lateinit var actionOnResult: (FirebaseAuthUIAuthenticationResult) -> Unit

    override var configurationData: FirebaseAuthDataProvider.ConfigurationData? = null

    data class ConfigurationData(
        val intentLauncher: ActivityResultLauncher<Intent>,
        @StyleRes val theme: Int,
    )
}
