package com.moonlightbutterfly.cryptohub

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.StyleRes
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthDataProviderImpl @Inject constructor() : FirebaseAuthDataProvider {

    override lateinit var actionOnResult: (FirebaseAuthUIAuthenticationResult) -> Unit

    override var configurationData: FirebaseAuthDataProvider.ConfigurationData? = null

    data class ConfigurationData(
        val intentLauncher: ActivityResultLauncher<Intent>,
        @StyleRes val theme: Int,
    )
}
