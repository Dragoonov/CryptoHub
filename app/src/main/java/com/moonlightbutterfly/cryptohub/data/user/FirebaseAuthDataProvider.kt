package com.moonlightbutterfly.cryptohub.data.user

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult

/**
 * Interface for communication between Firebase authentication framework and android classes:
 * ie. registering contracts and launching intents.
 */
interface FirebaseAuthDataProvider {
    var configurationData: ConfigurationData?
    var actionOnResult: (FirebaseAuthUIAuthenticationResult) -> Unit

    data class ConfigurationData(
        val intentLauncher: ActivityResultLauncher<Intent>,
        @StyleRes val theme: Int,
        @DrawableRes val logo: Int,
    )
}
