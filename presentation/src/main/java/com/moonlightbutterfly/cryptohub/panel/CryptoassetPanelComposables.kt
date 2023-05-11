@file:Suppress("TooManyFunctions")

package com.moonlightbutterfly.cryptohub.panel

import android.app.TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.core.ErrorHandler
import com.moonlightbutterfly.cryptohub.core.Favourite
import com.moonlightbutterfly.cryptohub.core.getImagePainterFor
import com.moonlightbutterfly.cryptohub.list.Chip
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoAssetMarketInfo
import com.moonlightbutterfly.cryptohub.models.NotificationConfiguration
import com.moonlightbutterfly.cryptohub.models.NotificationInterval
import com.moonlightbutterfly.cryptohub.models.NotificationTime
import com.moonlightbutterfly.cryptohub.presentation.R
import com.moonlightbutterfly.cryptohub.utils.round
import com.moonlightbutterfly.cryptohub.utils.toStringAbbr
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalCoilApi
@Composable
fun CryptoAssetPanelScreen(viewModel: CryptoAssetPanelViewModel) {
    val asset by viewModel.asset.collectAsStateWithLifecycle(CryptoAssetMarketInfo.EMPTY)
    val isLiked by viewModel.isCryptoInFavourites().collectAsStateWithLifecycle(false)
    val isSavedForNotifications by viewModel.isCryptoInNotifications().collectAsStateWithLifecycle(false)
    val areNotificationsEnabled by viewModel.areNotificationsEnabled().collectAsStateWithLifecycle(false)
    val error by viewModel.errorMessageFlow.collectAsStateWithLifecycle(null)
    error?.let { ErrorHandler(error) }
    val bottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )
    val scope = rememberCoroutineScope()
    val notificationTimes = getNotificationTimes()
    val selectedChips = remember {
        mutableStateMapOf<NotificationInterval, Boolean>().apply {
            putAll(NotificationInterval.values().associateWith { false })
        }
    }

    BottomSheetScaffold(
        scaffoldState = bottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetElevation = 15.dp,
        sheetContent = {
            BottomSheetContent(notificationTimes, selectedChips, viewModel, asset, scope, bottomSheetState)
        },
        sheetPeekHeight = 0.dp
    ) {
        Column(
            Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
                .clickable(MutableInteractionSource(), null) {
                    scope.launch { bottomSheetState.bottomSheetState.collapse() }
                }
        ) {
            Header(
                asset.asset, isLiked,
                {
                    if (it) {
                        viewModel.addCryptoToFavourites()
                    } else {
                        viewModel.removeCryptoFromFavourites()
                    }
                },
                isSavedForNotifications,
                areNotificationsEnabled,
                { scope.launch { bottomSheetState.bottomSheetState.expand() } }
            )
            PriceChangeRow(asset = asset)
            DescriptionRow(asset = asset)
            Statistics(asset = asset)
        }
    }
}

@Composable
private fun getNotificationTimes() = mapOf(
    NotificationInterval.MINUTES_30 to stringResource(id = R.string.notification_interval_30_minutes),
    NotificationInterval.HOUR to stringResource(id = R.string.notification_interval_hour),
    NotificationInterval.HOURS_2 to stringResource(id = R.string.notification_interval_2_hours),
    NotificationInterval.HOURS_5 to stringResource(id = R.string.notification_interval_5_hours),
    NotificationInterval.DAY to stringResource(id = R.string.notification_interval_day),
    NotificationInterval.WEEK to stringResource(id = R.string.notification_interval_week)
)

@Composable
fun Statistics(asset: CryptoAssetMarketInfo) {
    Section(name = stringResource(id = R.string.statistics)) {
        Stat(
            title = stringResource(id = R.string.market_cap),
            value = "${asset.marketCap.toStringAbbr(LocalContext.current)} USD",
        )
        Stat(
            title = stringResource(id = R.string.volume),
            value = "${asset.volume24H.toStringAbbr(LocalContext.current)} USD",
        )
        Stat(
            title = stringResource(id = R.string.circulating_supply),
            value = asset.circulatingSupply.toStringAbbr(LocalContext.current)
        )
        Stat(
            title = stringResource(id = R.string.max_supply),
            value = asset.maxSupply.toStringAbbr(LocalContext.current)
        )
        Stat(
            title = stringResource(id = R.string.rank),
            value = "${asset.rank}"
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
@SuppressWarnings("LongParameterList")
fun BottomSheetContent(
    notificationTimes: Map<NotificationInterval, String>,
    selectedChips: SnapshotStateMap<NotificationInterval, Boolean>,
    viewModel: CryptoAssetPanelViewModel,
    asset: CryptoAssetMarketInfo,
    coroutineScope: CoroutineScope,
    bottomSheetScaffoldState: BottomSheetScaffoldState
) {
    Column(
        Modifier
            .fillMaxWidth()
            .height(300.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val initialConfiguration by viewModel.getConfigurationForCrypto()
            .collectAsStateWithLifecycle(NotificationConfiguration(asset.asset.symbol))

        var selectedNotificationConfiguration: NotificationConfiguration by remember {
            mutableStateOf(initialConfiguration)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ChipsGroup(notificationTimes = notificationTimes, selectedChips = selectedChips) {
                val wasSelected = selectedChips[it]!!
                selectedChips.clearChips()
                selectedChips[it] = !wasSelected
                selectedNotificationConfiguration =
                    selectedNotificationConfiguration.copy(
                        symbol = asset.asset.symbol,
                        notificationInterval = if (selectedChips[it] == true) it else null
                    )
            }
            Text(text = stringResource(id = R.string.or))
            TimePicker(
                { hour, minute ->
                    selectedNotificationConfiguration = selectedNotificationConfiguration.copy(
                        symbol = asset.asset.symbol,
                        notificationTime = NotificationTime(hour, minute)
                    )
                    selectedChips.clearChips()
                },
                {
                    selectedNotificationConfiguration = selectedNotificationConfiguration.copy(
                        symbol = asset.asset.symbol,
                        notificationTime = null
                    )
                }
            )
            SummaryText(selectedNotificationConfiguration, notificationTimes)
            Row {
                SaveButton(
                    viewModel,
                    selectedNotificationConfiguration,
                    coroutineScope,
                    bottomSheetScaffoldState
                )
                Spacer(modifier = Modifier.padding(10.dp))
                DisableButton {
                    selectedNotificationConfiguration =
                        NotificationConfiguration(asset.asset.symbol)
                    viewModel.removeCryptoFromNotifications()
                    selectedChips.clearChips()
                    coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.collapse() }
                }
            }
        }
    }
}

private fun SnapshotStateMap<NotificationInterval, Boolean>.clearChips() {
    NotificationInterval.values().forEach { this[it] = false }
}

@Composable
fun SummaryText(
    selectedNotificationConfiguration: NotificationConfiguration,
    notificationTimes: Map<NotificationInterval, String>
) {
    val intervalString =
        selectedNotificationConfiguration.notificationInterval?.let {
            stringResource(R.string.every) + " " +
                (notificationTimes[selectedNotificationConfiguration.notificationInterval])
        } ?: ""
    val timeString = selectedNotificationConfiguration.notificationTime?.let {
        val comma = if (intervalString.isEmpty()) "" else ", "
        "$comma${it.hour}:${it.minute}"
    } ?: ""
    val finalString = if (intervalString.isNotEmpty() || timeString.isNotEmpty()) {
        stringResource(id = R.string.selected) + " $intervalString$timeString"
    } else {
        ""
    }
    Text(text = finalString)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SaveButton(
    viewModel: CryptoAssetPanelViewModel,
    selectedNotificationConfiguration: NotificationConfiguration,
    coroutineScope: CoroutineScope,
    bottomSheetScaffoldState: BottomSheetScaffoldState
) {
    Button(
        onClick = {
            viewModel.addCryptoToNotifications(
                selectedNotificationConfiguration.notificationTime,
                selectedNotificationConfiguration.notificationInterval
            )
            coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.collapse() }
        }
    ) {
        Text(text = stringResource(id = R.string.save))
    }
}

@Composable
fun DisableButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = stringResource(id = R.string.disable))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChipsGroup(
    notificationTimes: Map<NotificationInterval, String>,
    selectedChips: SnapshotStateMap<NotificationInterval, Boolean>,
    onSelectionChanged: (NotificationInterval) -> Unit
) {
    FlowRow(Modifier.padding(20.dp), horizontalArrangement = Arrangement.Center) {
        NotificationInterval.values().forEach {
            Chip(
                name = notificationTimes.getValue(it),
                isSelected = selectedChips.getValue(it),
                onSelectionChanged = { onSelectionChanged(it) }
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
@SuppressWarnings("LongParameterList")
fun Header(
    asset: CryptoAsset,
    isInFavourites: Boolean,
    onFavouriteClicked: (Boolean) -> Unit,
    isSavedAsNotification: Boolean,
    areNotificationsEnabled: Boolean,
    onNotifyClicked: () -> Unit
) {
    Row(
        Modifier
            .height(100.dp)
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        val painter = getImagePainterFor(asset)
        Image(
            painter = painter,
            modifier = Modifier
                .padding(end = 10.dp)
                .size(100.dp),
            contentScale = ContentScale.Fit,
            contentDescription = stringResource(R.string.crypto_asset_item_list_image_description),
        )
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            Arrangement.SpaceBetween
        ) {
            Text(
                text = asset.name,
                modifier = Modifier.padding(5.dp),
                fontSize = 30.sp
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(5.dp),
                Arrangement.SpaceBetween
            ) {
                Text(
                    text = asset.symbol,
                    fontSize = 20.sp
                )
                UtilIcons(
                    areNotificationsEnabled = areNotificationsEnabled,
                    isSavedAsNotification = isSavedAsNotification,
                    onNotifyClicked = onNotifyClicked,
                    isInFavourites = isInFavourites,
                    onFavouriteClicked = onFavouriteClicked
                )
            }
        }
    }
}

@Composable
fun UtilIcons(
    areNotificationsEnabled: Boolean,
    isSavedAsNotification: Boolean,
    onNotifyClicked: () -> Unit,
    isInFavourites: Boolean,
    onFavouriteClicked: (Boolean) -> Unit
) {
    Row(Modifier.wrapContentWidth(), Arrangement.Center) {
        if (areNotificationsEnabled) {
            Notification(
                isSavedForNotifications = isSavedAsNotification,
                onSelectionChanged = onNotifyClicked
            )
        }
        Favourite(
            modifier = Modifier,
            isInFavourites = isInFavourites,
            onSelectionChanged = onFavouriteClicked
        )
    }
}

@Composable
fun Notification(isSavedForNotifications: Boolean, onSelectionChanged: () -> Unit) {
    Column(
        Modifier
            .wrapContentWidth()
            .fillMaxHeight(),
        Arrangement.Center
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.End)
                .toggleable(
                    value = isSavedForNotifications,
                    onValueChange = { onSelectionChanged() }
                ),
            imageVector = if (isSavedForNotifications) Icons.Filled.Notifications else Icons.Outlined.Notifications,
            contentDescription = stringResource(id = R.string.notifications)
        )
    }
}

@Composable
fun PriceChangeRow(asset: CryptoAssetMarketInfo) {
    Row(
        Modifier
            .padding(bottom = 20.dp)
            .height(50.dp)
            .fillMaxWidth()
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            Arrangement.Top
        ) {
            Text(
                text = "${asset.price.round(2)} USD",
                fontSize = 30.sp
            )
        }
    }
}

@Composable
fun DescriptionRow(asset: CryptoAssetMarketInfo) {
    Row(
        Modifier
            .padding(bottom = 20.dp)
            .fillMaxWidth()
    ) {
        Text(text = asset.description)
    }
}

@Composable
fun Section(name: String, block: @Composable () -> Unit) {
    Text(
        text = name,
        fontSize = 30.sp,
        modifier = Modifier.padding(bottom = 20.dp)
    )
    block()
}

@Composable
fun Stat(title: String, value: String, style: TextStyle = LocalTextStyle.current) {
    Row(
        Modifier
            .height(50.dp)
            .fillMaxWidth(),
        Arrangement.SpaceBetween
    ) {
        Text(text = title, fontSize = 15.sp)
        Text(
            text = value,
            style = style,
            fontSize = 15.sp
        )
    }
}

@Composable
fun TimePicker(onSelectTime: (hour: Int, minute: Int) -> Unit, onClearTime: () -> Unit) {
    val mContext = LocalContext.current
    val mCalendar = Calendar.getInstance()
    val hour = mCalendar[Calendar.HOUR_OF_DAY]
    val minute = mCalendar[Calendar.MINUTE]

    val timePickerDialog = TimePickerDialog(
        mContext,
        R.style.Theme_CryptoHub_TimePicker,
        { _, mHour: Int, mMinute: Int ->
            onSelectTime(mHour, mMinute)
        },
        hour, minute, true
    )
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.Center) {
            Button(onClick = { timePickerDialog.show() }) {
                Text(text = stringResource(id = R.string.select_time))
            }
            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = onClearTime) {
                Text(text = stringResource(id = R.string.clear_time))
            }
        }
    }
}
