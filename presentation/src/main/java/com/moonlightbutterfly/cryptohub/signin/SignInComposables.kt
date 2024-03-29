package com.moonlightbutterfly.cryptohub.signin

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moonlightbutterfly.cryptohub.core.LoadingBar
import com.moonlightbutterfly.cryptohub.presentation.R

val SIGN_IN_CONTENT_UNDER_LOGO_HEIGHT = 400.dp

@Composable
fun SignInScreen(
    onSignedIn: () -> Unit,
    viewModel: SignInViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        LoadingBar()
    } else {
        if (uiState.isUserSignedIn!!) {
            onSignedIn()
        }
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Logo()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.height(SIGN_IN_CONTENT_UNDER_LOGO_HEIGHT)
            ) {
                ProviderSignInButton(
                    onClicked = { viewModel.acceptIntent(SignInIntent.SignIn) },
                    buttonBackgroundColor = MaterialTheme.colors.primary,
                    buttonText = R.string.sign_in
                )
                Text(text = stringResource(id = R.string.or), modifier = Modifier.padding(5.dp))
                AnonymousSignInButton(onSignedInIn = onSignedIn)
            }
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
fun ProviderSignInButton(
    onClicked: () -> Unit = {},
    buttonBackgroundColor: Color,
    @StringRes buttonText: Int,
) {
    Button(
        onClick = onClicked,
        modifier = Modifier
            .padding(bottom = 10.dp)
            .width(120.dp)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = buttonBackgroundColor
        )
    ) {
        Text(text = stringResource(id = buttonText))
    }
}

@Composable
fun AnonymousSignInButton(onSignedInIn: () -> Unit) {
    TextButton(
        onClick = {
            onSignedInIn()
        }
    ) {
        Text(text = stringResource(id = R.string.continue_without_sign_in))
    }
}
