package com.moonlightbutterfly.cryptohub.signincontrollers

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.moonlightbutterfly.cryptohub.domain.models.UserData

/**
 * The controller which creates an [ActivityResultLauncher] used to launch the sign in flow.
 */
interface GoogleSignInIntentController {

    /**
     * Returns a launcher to launch a Google sign in flow.
     * @param actionOnSuccess Callback to invoke when flow ends successfully
     * @param actionOnFailure Callback to invoke when flow ends with failure
     */
    fun getLauncher(
        actionOnSuccess: (userData: UserData) -> Unit,
        actionOnFailure: (message: String) -> Unit
    ): ActivityResultLauncher<Intent>
}
