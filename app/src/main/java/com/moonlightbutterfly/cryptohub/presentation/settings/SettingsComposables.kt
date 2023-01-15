package com.moonlightbutterfly.cryptohub.presentation.settings

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.firebase.ui.auth.AuthUI
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.presentation.ui.composables.ErrorHandler

@Composable
fun SettingsScreen(onSignOutClicked: () -> Unit) {

    val viewModel: SettingsViewModel = hiltViewModel()
    val nightModeEnabled by viewModel.isNightModeEnabled.observeAsState(false)

    val error by viewModel.errorMessageFlow.collectAsState(null)
    error?.let { ErrorHandler(error) }

    val isUserSignedIn by viewModel.isUserSignedIn.collectAsState(false)

    val onSignedOut = {
        viewModel.onSignedOut()
        onSignOutClicked()
    }
    Column(Modifier.fillMaxWidth()) {
        DeviceSpecificSection(
            nightModeEnabled = nightModeEnabled,
            onNightModeChanged = viewModel::onNightModeChanged
        )
        if (isUserSignedIn) {
            SignOutButton(onSignedOut)
        }
    }
}

@Composable
fun DeviceSpecificSection(
    nightModeEnabled: Boolean,
    onNightModeChanged: (Boolean) -> Unit
) {
    Title(stringResource(R.string.device_specific))
    Entries(nightModeEnabled = nightModeEnabled, onNightModeChanged = onNightModeChanged)
}

@Composable
fun SignOutButton(onSignedOut: () -> Unit) {
    val context = LocalContext.current
    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        TextButton(
            onClick = {
                signOut(context, onSignedOut)
            }
        ) {
            Text(
                text = stringResource(id = R.string.sign_out),
                fontSize = 20.sp,
                modifier = Modifier.padding(30.dp)
            )
        }
    }
}

@Composable
fun Title(title: String) {
    Text(
        modifier = Modifier.padding(20.dp),
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )
}

@Composable
fun Entries(nightModeEnabled: Boolean, onNightModeChanged: (Boolean) -> Unit) {
    Column {
        NightModeEntry(nightModeEnabled, onNightModeChanged)
    }
}

@Composable
fun NightModeEntry(nightModeEnabled: Boolean, onNightModeChanged: (Boolean) -> Unit) {
    EntryRow {
        Text(text = stringResource(id = R.string.night_mode))
        Switch(checked = nightModeEnabled, onCheckedChange = onNightModeChanged)
    }
}

@Composable
fun EntryRow(content: @Composable () -> Unit) {
    Row(
        Modifier
            .padding(top = 10.dp, bottom = 10.dp, start = 20.dp, end = 20.dp)
            .height(50.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        content()
    }
}

fun signOut(context: Context, onSignedOut: () -> Unit) {
    AuthUI.getInstance()
        .signOut(context)
        .addOnCompleteListener {
            onSignedOut()
        }
}
