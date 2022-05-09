package com.moonlightbutterfly.cryptohub.presentation.ui.composables

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.presentation.ui.LocalViewModelFactory
import com.moonlightbutterfly.cryptohub.presentation.viewmodels.SignInViewModel

val SIGN_IN_CONTENT_UNDER_LOGO_HEIGHT = 400.dp

@Composable
fun SignInScreen(
    onSignedIn: () -> Unit,
    onSignInFailed: (String) -> Unit,
    onEmailButtonClicked: () -> Unit,
    onPhoneButtonClicked: () -> Unit
) {
    val viewModel: SignInViewModel = viewModel(
        factory = LocalViewModelFactory.current
    )
    val isNightMode by viewModel.isNightModeEnabled.observeAsState(false)
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Logo()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.height(SIGN_IN_CONTENT_UNDER_LOGO_HEIGHT)
        ) {
            ProviderSignInButton(
                onClicked = { viewModel.signInThroughGoogle(onSignedIn, onSignInFailed) },
                buttonBackgroundColor = if (isNightMode) {
                    MaterialTheme.colors.primary
                } else {
                    colorResource(R.color.fui_bgGoogle)
                },
                buttonIcon = R.drawable.fui_ic_googleg_color_24dp,
                buttonText = R.string.sign_in_google
            )
            ProviderSignInButton(
                onClicked = onEmailButtonClicked,
                buttonBackgroundColor = colorResource(R.color.fui_bgEmail),
                buttonIcon = R.drawable.fui_ic_mail_white_24dp,
                buttonText = R.string.sign_in_email
            )
            ProviderSignInButton(
                onClicked = onPhoneButtonClicked,
                buttonBackgroundColor = colorResource(R.color.fui_bgPhone),
                buttonIcon = R.drawable.fui_ic_phone_white_24dp,
                buttonText = R.string.sign_in_phone
            )
            ProviderSignInButton(
                onClicked = { viewModel.signInThroughFacebook(onSignedIn, onSignInFailed) },
                buttonBackgroundColor = colorResource(R.color.fui_bgFacebook),
                buttonIcon = R.drawable.fui_ic_facebook_white_22dp,
                buttonText = R.string.sign_in_facebook
            )
            ProviderSignInButton(
                onClicked = { viewModel.signInThroughTwitter(onSignedIn, onSignInFailed) },
                buttonBackgroundColor = colorResource(R.color.fui_bgTwitter),
                buttonIcon = R.drawable.fui_ic_twitter_bird_white_24dp,
                buttonText = R.string.sign_in_twitter
            )
            Text(text = stringResource(id = R.string.or), modifier = Modifier.padding(5.dp))
            AnonymousSignInButton(onSignedInIn = onSignedIn)
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
    @DrawableRes buttonIcon: Int,
    @StringRes buttonText: Int,
) {
    Button(
        onClick = onClicked,
        modifier = Modifier
            .padding(bottom = 10.dp)
            .width(230.dp)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = buttonBackgroundColor
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProviderIcon(buttonIcon)
            Text(text = stringResource(id = buttonText), modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun ProviderIcon(@DrawableRes drawableId: Int, contentDescription: String? = null) {
    Image(
        painterResource(id = drawableId),
        contentDescription = contentDescription,
        modifier = Modifier
            .size(30.dp)
            .padding(end = 10.dp)
    )
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
