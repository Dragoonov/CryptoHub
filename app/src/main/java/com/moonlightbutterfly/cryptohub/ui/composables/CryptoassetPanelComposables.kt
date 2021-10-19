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
import com.moonlightbutterfly.cryptohub.dataobjects.CryptoassetItem
import com.moonlightbutterfly.cryptohub.ui.LocalViewModelFactory
import com.moonlightbutterfly.cryptohub.utils.toStringAbbr
import com.moonlightbutterfly.cryptohub.viewmodels.CryptoassetPanelViewModel

@ExperimentalCoilApi
@Composable
fun CryptoassetPanelScreen(cryptoassetSymbol: String) {
    val viewModel: CryptoassetPanelViewModel = viewModel(
        factory = LocalViewModelFactory.current
    )
    val cryptoasset by viewModel.getCryptoasset(cryptoassetSymbol)
        .observeAsState(CryptoassetItem.EMPTY)

    val isLiked by viewModel.isCryptoInFavourites(cryptoassetSymbol).observeAsState(false)

    Column(
        Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Header(
            cryptoassetItem = cryptoasset,
            isInFavourites = isLiked,
            onFavouriteClicked = {
                if (it) {
                    viewModel.addCryptoToFavourites(cryptoassetSymbol)
                } else {
                    viewModel.removeCryptoFromFavourites(cryptoassetSymbol)
                }
            })
        PriceChangeRow(cryptoassetItem = cryptoasset)
        Section(name = stringResource(id = R.string.statistics)) {
            Stat(
                title = stringResource(id = R.string.market_cap),
                value = "${cryptoasset.marketCap.toStringAbbr()} USD (${cryptoasset.dayChanges.marketCapChangePct} %)",
                style = getMarketCapChangeTextStyleFor(item = cryptoasset)
            )
            Stat(
                title = stringResource(id = R.string.volume),
                value = "${cryptoasset.dayChanges.volume.toStringAbbr()} USD (${cryptoasset.dayChanges.volumeChangePct} %)",
                style = getVolumeChangeTextStyleFor(item = cryptoasset)
            )
            Stat(
                title = stringResource(id = R.string.circulating_supply),
                value = cryptoasset.circulatingSupply.toStringAbbr()
            )
            Stat(
                title = stringResource(id = R.string.max_supply),
                value = cryptoasset.maxSupply.toStringAbbr()
            )
            Stat(
                title = stringResource(id = R.string.rank),
                value = "${cryptoasset.rank}"
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
fun Header(cryptoassetItem: CryptoassetItem, isInFavourites: Boolean, onFavouriteClicked: (Boolean) -> Unit) {
    Row(
        Modifier
            .height(100.dp)
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        val painter = getImagePainterFor(cryptoassetItem)
        Image(
            painter = painter,
            modifier = Modifier
                .padding(end = 10.dp)
                .size(100.dp),
            contentScale = ContentScale.Inside,
            contentDescription = stringResource(R.string.cryptoasset_item_list_image_description),
        )
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            Arrangement.SpaceBetween
        ) {
            Text(
                text = cryptoassetItem.name,
                modifier = Modifier.padding(5.dp),
                fontSize = 30.sp
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(5.dp), Arrangement.SpaceBetween) {
                Text(
                    text = cryptoassetItem.symbol,
                    fontSize = 20.sp
                )
                Favourite(isInFavourites = isInFavourites, onSelectionChanged = onFavouriteClicked)
            }
        }
    }
}

@Composable
fun PriceChangeRow(cryptoassetItem: CryptoassetItem) {
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
                text = "${cryptoassetItem.price} USD",
                modifier = Modifier.padding(bottom = 2.dp),
                fontSize = 30.sp
            )
            Text(
                text = if (cryptoassetItem.dayChanges.priceChange > 0) {
                    "+ "
                } else {
                    ""
                } + "${cryptoassetItem.dayChanges.priceChange}" +
                    " (${cryptoassetItem.dayChanges.priceChangePercent}%)",
                modifier = Modifier.padding(top = 2.dp),
                fontSize = 15.sp,
                style = getPriceChangeTextStyleFor(cryptoassetItem)
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
