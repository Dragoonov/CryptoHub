package com.moonlightbutterfly.cryptohub.presentation.ui

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

/**
 * Interface marking a class as a host for sign in flow that is entered through the [ActivityResultLauncher]
 * returned by the [getSignInIntentLauncher] method.
 */
interface SignInFlowProvider {

    /**
     * Returns the launcher which will launch the sign in flow in the app.
     * @param onSignInSuccess a callback invoked when the sign in was successful.
     * @param onSignInFailure a callback invoked when the sign in was unsuccessful.
     *
     * @return Launcher which will launch the sign in flow when invoked.
     */
    fun getSignInIntentLauncher(
        onSignInSuccess: () -> Unit,
        onSignInFailure: () -> Unit
    ): ActivityResultLauncher<Intent>
}
