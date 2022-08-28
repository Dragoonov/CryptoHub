package com.moonlightbutterfly.cryptohub.di

import android.content.Context
import androidx.activity.ComponentActivity
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.moonlightbutterfly.cryptohub.signincontrollers.EmailSignInController
import com.moonlightbutterfly.cryptohub.signincontrollers.FacebookSignInController
import com.moonlightbutterfly.cryptohub.signincontrollers.GoogleSignInController
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
    fun provideEmailSignInController(): EmailSignInController {
        return EmailSignInController(FirebaseAuth.getInstance())
    }

    @Provides
    fun provideGoogleSignInController(): GoogleSignInController {
        return GoogleSignInController()
    }

    @Provides
    fun providePhoneSignInController(): PhoneSignInController {
        return PhoneSignInController(FirebaseAuth.getInstance())
    }

    @Provides
    fun provideTwitterSignInController(): TwitterSignInController {
        return TwitterSignInController(FirebaseAuth.getInstance())
    }

    @Provides
    fun provideFacebookSignInController(): FacebookSignInController {
        return FacebookSignInController(
            LoginManager.getInstance(),
            FirebaseAuth.getInstance()
        )
    }

    @Provides
    fun provideComponentActivity(context: Context): ComponentActivity {
        return context as ComponentActivity
    }
}
