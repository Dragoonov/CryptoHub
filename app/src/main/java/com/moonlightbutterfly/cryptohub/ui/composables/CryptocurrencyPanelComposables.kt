package com.moonlightbutterfly.cryptohub.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.dataobjects.CryptocurrencyItem
import com.moonlightbutterfly.cryptohub.ui.LocalViewModelFactory
import com.moonlightbutterfly.cryptohub.utils.toStringAbbr
import com.moonlightbutterfly.cryptohub.viewmodels.CryptocurrencyPanelViewModel

@ExperimentalCoilApi
@Composable
fun CryptocurrencyPanelScreen(cryptocurrencySymbol: String) {
    val viewModel: CryptocurrencyPanelViewModel = viewModel(
        factory = LocalViewModelFactory.current
    )
    val cryptocurrency by viewModel.getCryptocurrency(cryptocurrencySymbol)
        .observeAsState(CryptocurrencyItem.EMPTY)
    CryptocurrencyPanel(cryptocurrency)
}

@ExperimentalCoilApi
@Composable
fun CryptocurrencyPanel(cryptocurrencyItem: CryptocurrencyItem) {
    Column(
        Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Header(cryptocurrencyItem = cryptocurrencyItem)
        PriceChangeRow(cryptocurrencyItem = cryptocurrencyItem)
        Section(name = stringResource(id = R.string.statistics)) {
            Stat(
                title = stringResource(id = R.string.market_cap),
                value = "${cryptocurrencyItem.marketCap.toStringAbbr()} USD (${cryptocurrencyItem.dayChanges.marketCapChangePct} %)",
                style = getMarketCapChangeTextStyleFor(item = cryptocurrencyItem)
            )
            Stat(
                title = stringResource(id = R.string.volume),
                value = "${cryptocurrencyItem.dayChanges.volume.toStringAbbr()} USD (${cryptocurrencyItem.dayChanges.volumeChangePct} %)",
                style = getVolumeChangeTextStyleFor(item = cryptocurrencyItem)
            )
            Stat(
                title = stringResource(id = R.string.circulating_supply),
                value = cryptocurrencyItem.circulatingSupply.toStringAbbr()
            )
            Stat(
                title = stringResource(id = R.string.max_supply),
                value = cryptocurrencyItem.maxSupply.toStringAbbr()
            )
            Stat(
                title = stringResource(id = R.string.rank),
                value = "${cryptocurrencyItem.rank}"
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
fun Header(cryptocurrencyItem: CryptocurrencyItem) {
    Row(
        Modifier
            .height(100.dp)
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        val painter = getImagePainterFor(cryptocurrencyItem)
        Image(
            painter = painter,
            modifier = Modifier
                .padding(end = 10.dp)
                .size(100.dp),
            contentScale = ContentScale.Inside,
            contentDescription = stringResource(R.string.cryptocurrency_item_list_image_description),
        )
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            Arrangement.SpaceBetween
        ) {
            Text(
                text = cryptocurrencyItem.name,
                modifier = Modifier.padding(5.dp),
                fontSize = 30.sp
            )
            Text(
                text = cryptocurrencyItem.symbol,
                modifier = Modifier.padding(5.dp),
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun PriceChangeRow(cryptocurrencyItem: CryptocurrencyItem) {
    Row(
        Modifier
            .height(100.dp)
            .fillMaxWidth()
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            Arrangement.Top
        ) {
            Text(
                text = "${cryptocurrencyItem.price} USD",
                modifier = Modifier.padding(bottom = 2.dp),
                fontSize = 30.sp
            )
            Text(
                text = if (cryptocurrencyItem.dayChanges.priceChange > 0) {
                    "+ "
                } else {
                    "- "
                } + "${cryptocurrencyItem.dayChanges.priceChange}" +
                    " (${cryptocurrencyItem.dayChanges.priceChangePercent}%)",
                modifier = Modifier.padding(top = 2.dp),
                fontSize = 15.sp,
                style = getPriceChangeTextStyleFor(cryptocurrencyItem)
            )
        }
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
