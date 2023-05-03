package com.moonlightbutterfly.cryptohub.settings

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.firebase.ui.auth.AuthUI
import com.moonlightbutterfly.cryptohub.core.ErrorHandler
import com.moonlightbutterfly.cryptohub.presentation.R

@Composable
fun SettingsScreen(onSignOutClicked: () -> Unit, viewModel: SettingsViewModel) {

    val nightModeEnabled by viewModel.isNightModeEnabled.collectAsStateWithLifecycle(false)
    val notificationsEnabled by viewModel.areNotificationsEnabled.collectAsStateWithLifecycle(false)

    val configurations by viewModel.notificationsSymbols.collectAsStateWithLifecycle(emptyList())

    val error by viewModel.errorMessageFlow.collectAsStateWithLifecycle(null)
    error?.let { ErrorHandler(error) }

    val isUserSignedIn by viewModel.isUserSignedIn.collectAsStateWithLifecycle(false)

    val onSignedOut = {
        viewModel.onSignedOut()
        onSignOutClicked()
    }
    Column(Modifier.fillMaxWidth()) {
        DeviceSpecificSection(
            nightModeEnabled = nightModeEnabled,
            onNightModeChanged = viewModel::onNightModeChanged,
            notificationsEnabled = notificationsEnabled,
            onNotificationsChange = {
                if (it) {
                    viewModel.onEnabledNotifications()
                } else {
                    viewModel.onDisabledNotifications()
                }
            },
            configurations
        )
        if (isUserSignedIn) {
            SignOutButton(onSignedOut)
        }
    }
}

@Composable
fun DeviceSpecificSection(
    nightModeEnabled: Boolean,
    onNightModeChanged: (Boolean) -> Unit,
    notificationsEnabled: Boolean,
    onNotificationsChange: (Boolean) -> Unit,
    notifications: List<String>
) {
    Title(stringResource(R.string.device_specific))
    Entries(
        nightModeEnabled,
        onNightModeChanged,
        notificationsEnabled,
        onNotificationsChange,
        notifications
    )
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
fun Entries(
    nightModeEnabled: Boolean,
    onNightModeChanged: (Boolean) -> Unit,
    notificationsEnabled: Boolean,
    onNotificationsChange: (Boolean) -> Unit,
    notificationsList: List<String>
) {
    Column {
        NightModeEntry(nightModeEnabled, onNightModeChanged)
        NotificationsEntry(
            notificationsEnabled = notificationsEnabled,
            onNotificationsChange = onNotificationsChange,
            notifications = notificationsList
        )
    }
}

@Composable
fun NightModeEntry(nightModeEnabled: Boolean, onNightModeChanged: (Boolean) -> Unit) {
    EntryRow {
        Text(text = stringResource(id = R.string.night_mode))
        Switch(
            checked = nightModeEnabled,
            onCheckedChange = onNightModeChanged,
            modifier = Modifier.testTag("nightModeSwitch")
        )
    }
}

@Composable
fun NotificationsEntry(
    notificationsEnabled: Boolean,
    onNotificationsChange: (Boolean) -> Unit,
    notifications: List<String>,
) {
    EntryRow {
        Text(text = stringResource(id = R.string.post_notifications))
        Switch(
            checked = notificationsEnabled,
            onCheckedChange = onNotificationsChange,
            modifier = Modifier.testTag("notificationsSwitch")
        )
    }
    if (notificationsEnabled) {
        EntryRow {
            Text(
                text = stringResource(id = R.string.saved_notifications) + " " + notifications.toString()
                    .drop(1).dropLast(1),
                modifier = Modifier.padding(start = 20.dp)
            )
        }
    }
}

@Composable
fun EntryRow(content: @Composable () -> Unit) {
    Row(
        Modifier
            .padding(top = 10.dp, bottom = 10.dp, start = 20.dp, end = 20.dp)
            .height(50.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
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
