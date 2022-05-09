package com.moonlightbutterfly.cryptohub.di

import android.content.Context
import androidx.activity.ComponentActivity
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.moonlightbutterfly.cryptohub.signincontrollers.EmailSignInController
import com.moonlightbutterfly.cryptohub.signincontrollers.FacebookSignInController
import com.moonlightbutterfly.cryptohub.signincontrollers.GoogleSignInController
import com.moonlightbutterfly.cryptohub.signincontrollers.GoogleSignInIntentController
import com.moonlightbutterfly.cryptohub.signincontrollers.PhoneSignInController
import com.moonlightbutterfly.cryptohub.signincontrollers.SignInManager
import com.moonlightbutterfly.cryptohub.signincontrollers.SignInManagerImpl
import com.moonlightbutterfly.cryptohub.signincontrollers.TwitterSignInController
import dagger.Module
import dagger.Provides

@Module
class SignInModule {

    @Provides
    fun provideSignInManager(
        googleSignInController: GoogleSignInController,
        emailSignInController: EmailSignInController,
        phoneSignInController: PhoneSignInController,
        facebookSignInController: FacebookSignInController,
        twitterSignInController: TwitterSignInController
    ): SignInManager {
        return SignInManagerImpl(
            googleSignInController,
            emailSignInController,
            phoneSignInController,
            facebookSignInController,
            twitterSignInController
        )
    }

    @Provides
    fun provideEmailSignInController(context: Context): EmailSignInController {
        return EmailSignInController(context as ComponentActivity, FirebaseAuth.getInstance())
    }

    @Provides
    fun provideGoogleSignInController(googleSignInIntentController: GoogleSignInIntentController): GoogleSignInController {
        return GoogleSignInController(googleSignInIntentController)
    }

    @Provides
    fun providePhoneSignInController(context: Context): PhoneSignInController {
        return PhoneSignInController(context as ComponentActivity, FirebaseAuth.getInstance())
    }

    @Provides
    fun provideTwitterSignInController(context: Context): TwitterSignInController {
        return TwitterSignInController(context as ComponentActivity, FirebaseAuth.getInstance())
    }

    @Provides
    fun provideFacebookSignInController(context: Context): FacebookSignInController {
        return FacebookSignInController(
            context as ComponentActivity,
            LoginManager.getInstance(),
            FirebaseAuth.getInstance()
        )
    }
}
