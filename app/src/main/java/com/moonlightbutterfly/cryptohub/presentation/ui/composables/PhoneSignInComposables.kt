package com.moonlightbutterfly.cryptohub.presentation.ui.composables

import androidx.activity.ComponentActivity
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.presentation.ui.LocalViewModelFactory
import com.moonlightbutterfly.cryptohub.presentation.viewmodels.SignInViewModel

@Composable
fun PhoneSignInScreen(
    onSignedIn: () -> Unit,
    onSignInFailed: (String) -> Unit
) {
    val viewModel: SignInViewModel = viewModel(
        factory = LocalViewModelFactory.current
    )

    val error by viewModel.errorMessageFlow.collectAsState(null)
    error?.let { ErrorHandler(error) }

    val isPhoneRequestInProgress by viewModel.isPhoneRequestInProcess.collectAsState(initial = false)
    val activity = LocalContext.current as ComponentActivity
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
            if (!isPhoneRequestInProgress) {
                PhoneNumberInputPanel(viewModel, onSignedIn, onSignInFailed, activity)
            } else {
                PhoneCodeInputPanel(viewModel, activity)
            }
        }
    }
}

@Composable
fun PhoneNumberInputPanel(
    viewModel: SignInViewModel,
    onSignedInIn: () -> Unit,
    onSignInFailed: (String) -> Unit,
    activity: ComponentActivity
) {
    val (phoneNumber, onPhoneNumberChange) = remember { mutableStateOf("") }
    Text(
        text = stringResource(id = R.string.phone_sign_in_description),
        modifier = Modifier.padding(bottom = 10.dp, start = 50.dp, end = 50.dp),
        textAlign = TextAlign.Center
    )
    TextField(
        value = phoneNumber,
        onValueChange = onPhoneNumberChange,
        label = { Text(stringResource(id = R.string.phone_number)) },
        modifier = Modifier.padding(bottom = 10.dp)
    )
    Button(
        onClick = {
            viewModel.signInThroughPhone(phoneNumber, onSignedInIn, onSignInFailed, activity)
        }
    ) {
        Text(text = stringResource(id = R.string.continue_sign_in))
    }
}

@Composable
fun PhoneCodeInputPanel(viewModel: SignInViewModel, activity: ComponentActivity) {
    val (code, onCodeChange) = remember { mutableStateOf("") }
    Text(
        text = stringResource(id = R.string.code_description),
        modifier = Modifier.padding(bottom = 10.dp),
        textAlign = TextAlign.Center
    )
    TextField(
        value = code,
        onValueChange = onCodeChange,
        label = { Text(stringResource(id = R.string.code)) },
        modifier = Modifier.padding(bottom = 5.dp)
    )
    Button(
        onClick = {
            viewModel.signInThroughPhoneWithCode(code, activity)
        }
    ) {
        Text(text = stringResource(id = R.string.continue_sign_in))
    }
}
