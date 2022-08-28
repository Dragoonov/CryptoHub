package com.moonlightbutterfly.cryptohub.signincontrollers

import androidx.activity.ComponentActivity
import com.moonlightbutterfly.cryptohub.domain.models.UserData
import kotlinx.coroutines.flow.Flow

/**
 * A manager that acts as a facade to invoke many sign in flows.
 */
interface SignInManager {

    /**
     * Flow that informs if user requested the verification code during phone sign in flow.
     */
    val isPhoneRequestInProcess: Flow<Boolean>

    /**
     * Launches sign in flow with Google.
     * @param onSignInSuccess Callback to invoke if sign in flow ends successfully
     * @param onSignInFailure Callback to invoke if sign in flow ends with failure
     * @param googleSignInIntentController Controller returning the launcher
     */
    fun signInThroughGoogle(
        onSignInSuccess: (user: UserData) -> Unit,
        onSignInFailure: (message: String) -> Unit,
        googleSignInIntentController: GoogleSignInIntentController
    )

    /**
     * Launches sign in flow with Email credentials.
     * @param email Provided account's email
     * @param password Provided account's password
     * @param onSignInSuccess Callback to invoke if sign in flow ends successfully
     * @param onSignInFailure Callback to invoke if sign in flow ends with failure
     * @param componentActivity Activity hosting the flow
     */
    fun signInThroughEmail(
        email: String,
        password: String,
        onSignInSuccess: (user: UserData) -> Unit,
        onSignInFailure: (message: String) -> Unit,
        componentActivity: ComponentActivity
    )

    /**
     * Launches sign in flow with Phone number.
     * @param phoneNumber The phone number which should be connected to account
     * @param onSignInSuccess Callback to invoke if sign in flow ends successfully
     * @param onSignInFailure Callback to invoke if sign in flow ends with failure
     * @param componentActivity Activity hosting the flow
     */
    fun signInThroughPhone(
        phoneNumber: String,
        onSignInSuccess: (user: UserData) -> Unit,
        onSignInFailure: (message: String) -> Unit,
        componentActivity: ComponentActivity
    )

    /**
     * Tries to sign in with manually provided verification code in case of sign in with phone number.
     * @param code The verification code user received during sign in process
     * @param componentActivity Activity hosting the flow
     */
    fun signInThroughPhoneWithCode(code: String, componentActivity: ComponentActivity)

    /**
     * Launches sign in flow with Facebook.
     * @param onSignInSuccess Callback to invoke if sign in flow ends successfully
     * @param onSignInFailure Callback to invoke if sign in flow ends with failure
     * @param componentActivity Activity hosting the flow
     */
    fun signInThroughFacebook(
        onSignInSuccess: (user: UserData) -> Unit,
        onSignInFailure: (message: String) -> Unit,
        componentActivity: ComponentActivity
    )

    /**
     * Launches sign in flow with Twitter.
     * @param onSignInSuccess Callback to invoke if sign in flow ends successfully
     * @param onSignInFailure Callback to invoke if sign in flow ends with failure
     * @param componentActivity Activity hosting the flow
     */
    fun signInThroughTwitter(
        onSignInSuccess: (user: UserData) -> Unit,
        onSignInFailure: (message: String) -> Unit,
        componentActivity: ComponentActivity
    )
}
