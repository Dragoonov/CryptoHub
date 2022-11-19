package com.moonlightbutterfly.cryptohub.framework

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.StyleRes
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult

interface FirebaseAuthDataProvider {
    fun getConfigurationData(): ConfigurationData
    fun setActionOnResult(action: (FirebaseAuthUIAuthenticationResult) -> Unit)

    data class ConfigurationData(
        val intentLauncher: ActivityResultLauncher<Intent>,
        @StyleRes val theme: Int,
    )
}