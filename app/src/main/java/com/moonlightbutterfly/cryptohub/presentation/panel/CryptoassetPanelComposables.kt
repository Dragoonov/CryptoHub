package com.moonlightbutterfly.cryptohub.presentation.panel

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoAssetMarketInfo
import com.moonlightbutterfly.cryptohub.presentation.core.ErrorHandler
import com.moonlightbutterfly.cryptohub.presentation.core.Favourite
import com.moonlightbutterfly.cryptohub.presentation.core.getImagePainterFor
import com.moonlightbutterfly.cryptohub.utils.round
import com.moonlightbutterfly.cryptohub.utils.toStringAbbr

@ExperimentalCoilApi
@Composable
fun CryptoAssetPanelScreen(cryptoAssetSymbol: String, viewModel: CryptoAssetPanelViewModel) {
    val asset by viewModel.getCryptoAssetMarketInfo(cryptoAssetSymbol)
        .observeAsState(CryptoAssetMarketInfo.EMPTY)

    val isLiked by viewModel.isCryptoInFavourites().collectAsState(false)

    val error by viewModel.errorMessageFlow.collectAsState(null)
    error?.let { ErrorHandler(error) }

    Column(
        Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
        Header(
            asset = asset.asset,
            isInFavourites = isLiked,
            onFavouriteClicked = {
                if (it) {
                    viewModel.addCryptoToFavourites()
                } else {
                    viewModel.removeCryptoFromFavourites()
                }
            }
        )
        PriceChangeRow(asset = asset)
        DescriptionRow(asset = asset)
        Section(name = stringResource(id = R.string.statistics)) {
            Stat(
                title = stringResource(id = R.string.market_cap),
                value = "${asset.marketCap.toStringAbbr()} USD",
            )
            Stat(
                title = stringResource(id = R.string.volume),
                value = "${asset.volume24H.toStringAbbr()} USD",
            )
            Stat(
                title = stringResource(id = R.string.circulating_supply),
                value = asset.circulatingSupply.toStringAbbr()
            )
            Stat(
                title = stringResource(id = R.string.max_supply),
                value = asset.maxSupply.toStringAbbr()
            )
            Stat(
                title = stringResource(id = R.string.rank),
                value = "${asset.rank}"
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
fun Header(
    asset: CryptoAsset,
    isInFavourites: Boolean,
    onFavouriteClicked: (Boolean) -> Unit
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
                    .padding(5.dp),
                Arrangement.SpaceBetween
            ) {
                Text(
                    text = asset.symbol,
                    fontSize = 20.sp
                )
                Favourite(isInFavourites = isInFavourites, onSelectionChanged = onFavouriteClicked)
            }
        }
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
