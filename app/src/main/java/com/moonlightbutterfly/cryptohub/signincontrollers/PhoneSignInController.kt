package com.moonlightbutterfly.cryptohub.signincontrollers

import androidx.activity.ComponentActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.domain.models.UserData
import com.moonlightbutterfly.cryptohub.utils.toUserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * Controller that's responsible for the phone sign in flow.
 * @param hostActivity Activity that hosts the flow
 * @param firebaseAuth [FirebaseAuth] instance that manages the flow
 */
class PhoneSignInController(
    private val hostActivity: ComponentActivity,
    private val firebaseAuth: FirebaseAuth
) {

    private var verificationCode: String = ""
    val isPhoneRequestInProcess = MutableStateFlow(false)
    private var tickerJob: Job? = null
    private var signInActionOnSuccess: (UserData) -> Unit = {}
    private var signInActionOnFailure: (String) -> Unit = {}

    /**
     * Launches sign in flow.
     * @param phoneNumber Provided phone number
     * @param onSignInSuccess Callback to invoke if sign in flow ends successfully
     * @param onSignInFailure Callback to invoke if sign in flow ends with failure
     */
    fun signIn(
        phoneNumber: String,
        onSignInSuccess: (user: UserData) -> Unit,
        onSignInFailure: (message: String) -> Unit,
    ) {
        if (phoneNumber.isEmpty()) {
            onSignInFailure(hostActivity.getString(R.string.field_not_empty))
            return
        }
        signInActionOnSuccess = onSignInSuccess
        signInActionOnFailure = onSignInFailure
        try {
            val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(VERIFICATION_PHONE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .setActivity(hostActivity)
                .setCallbacks(PhoneSignInCallbacks())
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        } catch (e: Exception) {
            resetTicker()
            onSignInFailure(hostActivity.getString(R.string.sign_in_failed))
        }
    }

    /**
     * Signs in with code provided if the automatic sign in fails.
     * @param code The verification code that user received on the phone
     */
    fun signInWithCode(code: String) {
        try {
            val credential = PhoneAuthProvider.getCredential(verificationCode, code)
            signInWithPhoneAuthCredential(credential)
        } catch (e: Exception) {
            resetTicker()
            signInActionOnFailure(hostActivity.getString(R.string.sign_in_failed))
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(hostActivity) { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    signInActionOnSuccess(user?.toUserData() ?: UserData())
                } else {
                    signInActionOnFailure(
                        task.exception?.localizedMessage
                            ?: hostActivity.getString(R.string.sign_in_failed)
                    )
                }
                resetTicker()
            }
    }

    private fun startTimeoutTicker() {
        isPhoneRequestInProcess.value = true
        tickerJob?.cancel()
        tickerJob = CoroutineScope(Dispatchers.IO).launch {
            delay(1000 * VERIFICATION_PHONE_TIMEOUT_SECONDS)
            isPhoneRequestInProcess.value = false
        }
    }

    private fun resetTicker() {
        tickerJob?.cancel()
        isPhoneRequestInProcess.value = false
    }

    private companion object {
        private const val VERIFICATION_PHONE_TIMEOUT_SECONDS = 60L
    }

    private inner class PhoneSignInCallbacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            signInActionOnFailure(
                e.localizedMessage ?: hostActivity.getString(R.string.sign_in_failed)
            )
            resetTicker()
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            verificationCode = verificationId
            startTimeoutTicker()
        }
    }
}
