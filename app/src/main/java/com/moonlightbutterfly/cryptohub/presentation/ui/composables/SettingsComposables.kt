package com.moonlightbutterfly.cryptohub.presentation.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.presentation.ui.LocalViewModelFactory
import com.moonlightbutterfly.cryptohub.presentation.viewmodels.SettingsViewModel

@Composable
fun SettingsScreen() {
    val viewModel: SettingsViewModel = viewModel(
        factory = LocalViewModelFactory.current
    )
    val nightModeEnabled by viewModel.isNightModeEnabled.observeAsState(false)
    Column(Modifier.fillMaxWidth()) {
        GeneralSection(
            nightModeEnabled = nightModeEnabled,
            onNightModeChanged = viewModel::onNightModeChanged
        )
    }
}

@Composable
fun GeneralSection(nightModeEnabled: Boolean, onNightModeChanged: (Boolean) -> Unit) {
    Title(stringResource(R.string.general))
    Entries(nightModeEnabled = nightModeEnabled, onNightModeChanged = onNightModeChanged)
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
