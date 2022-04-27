package com.moonlightbutterfly.cryptohub.presentation.ui.composables

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.domain.models.UserData
import com.moonlightbutterfly.cryptohub.presentation.ui.LocalViewModelFactory
import com.moonlightbutterfly.cryptohub.presentation.ui.SignInFlowProvider
import com.moonlightbutterfly.cryptohub.presentation.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    signInFlowProvider: SignInFlowProvider,
    onSignedInIn: () -> Unit,
) {

    val viewModel: LoginViewModel = viewModel(
        factory = LocalViewModelFactory.current
    )
    val context = LocalContext.current
    val onSignInFailed = { Toast.makeText(context, R.string.sign_in_failed, Toast.LENGTH_LONG).show() }
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Logo()
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            GoogleSignInButton(
                signInFlowProvider = signInFlowProvider,
                viewModel = viewModel,
                onSignedInIn = onSignedInIn,
                onSignInFailed = onSignInFailed
            )
            Text(text = stringResource(id = R.string.or), modifier = Modifier.padding(5.dp))
            AnonymousSignInButton(viewModel = viewModel, onSignedInIn = onSignedInIn)
        }
    }
}

@Composable
fun Logo() {
    Text(
        text = stringResource(id = R.string.app_name),
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Italic,
        color = MaterialTheme.colors.primary,
        fontSize = 50.sp
    )
}

@Composable
fun GoogleSignInButton(
    signInFlowProvider: SignInFlowProvider,
    viewModel: LoginViewModel,
    onSignedInIn: () -> Unit,
    onSignInFailed: () -> Unit
) {
    Button(
        onClick = { launchSignInFlow(signInFlowProvider, viewModel, onSignedInIn, onSignInFailed) },
        modifier = Modifier.padding(bottom = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            GoogleIcon()
            Text(text = stringResource(id = R.string.sign_in_google))
        }
    }
}

@Composable
fun GoogleIcon() {
    Image(
        painterResource(id = R.drawable.google),
        contentDescription = stringResource(id = R.string.google_icon),
        modifier = Modifier
            .size(30.dp)
            .padding(end = 10.dp)
    )
}

@Composable
fun AnonymousSignInButton(viewModel: LoginViewModel, onSignedInIn: () -> Unit) {
    TextButton(
        onClick = {
            viewModel.clearUser()
            onSignedInIn()
        }
    ) {
        Text(text = stringResource(id = R.string.continue_without_sign_in))
    }
}

private fun launchSignInFlow(
    provider: SignInFlowProvider,
    viewModel: LoginViewModel,
    onSignedIn: () -> Unit,
    onSignInFailed: () -> Unit
) {
    val launcher = provider.getSignInIntentLauncher(
        {
            FirebaseAuth.getInstance().currentUser?.let {
                viewModel.saveUser(
                    UserData(
                        id = it.uid,
                        name = it.displayName ?: "",
                        mail = it.email ?: ""
                    )
                )
            }
            onSignedIn()
        },
        {
            onSignInFailed()
        }
    )
    val intent = createSignInIntent()
    launcher.launch(intent)
}

private fun createSignInIntent(): Intent {
    val providers = arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build()
    )
    return AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .build()
}
