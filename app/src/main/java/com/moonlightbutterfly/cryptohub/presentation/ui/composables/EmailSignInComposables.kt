package com.moonlightbutterfly.cryptohub.presentation.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.presentation.ui.LocalViewModelFactory
import com.moonlightbutterfly.cryptohub.presentation.viewmodels.SignInViewModel

@Composable
fun EmailSignInScreen(
    onSignedIn: () -> Unit,
    onSignInFailed: (String) -> Unit
) {
    val viewModel: SignInViewModel = viewModel(
        factory = LocalViewModelFactory.current
    )
    val (email, onEmailChange) = remember { mutableStateOf("") }
    val (password, onPasswordChange) = remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Logo()
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.height(SIGN_IN_CONTENT_UNDER_LOGO_HEIGHT)) {
            TextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text(stringResource(id = R.string.email)) },
                modifier = Modifier.padding(bottom = 5.dp)
            )
            TextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text(stringResource(id = R.string.password)) },
                modifier = Modifier.padding(bottom = 10.dp)
            )
            Button(
                onClick = {
                    viewModel.signInThroughEmail(
                        email = email,
                        password = password,
                        onSignInSuccess = onSignedIn,
                        onSignInFailure = onSignInFailed
                    )
                }
            ) {
                Text(text = stringResource(id = R.string.continue_sign_in))
            }
        }
    }
}
