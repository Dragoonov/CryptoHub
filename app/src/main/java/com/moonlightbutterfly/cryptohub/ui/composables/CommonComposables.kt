package com.moonlightbutterfly.cryptohub.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import com.moonlightbutterfly.cryptohub.dataobjects.CryptocurrencyItem

fun getPriceChangeTextStyleFor(item: CryptocurrencyItem): TextStyle {
    return TextStyle(
        if (item.dayChanges.priceChangePercent > 0) {
            Color.Green
        } else {
            Color.Red
        }
    )
}

fun getMarketCapChangeTextStyleFor(item: CryptocurrencyItem): TextStyle {
    return TextStyle(
        if (item.dayChanges.marketCapChangePct > 0) {
            Color.Green
        } else {
            Color.Red
        }
    )
}

fun getVolumeChangeTextStyleFor(item: CryptocurrencyItem): TextStyle {
    return TextStyle(
        if (item.dayChanges.volumeChangePct > 0) {
            Color.Green
        } else {
            Color.Red
        }
    )
}

@ExperimentalCoilApi
@Composable
fun getImagePainterFor(item: CryptocurrencyItem): ImagePainter {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .componentRegistry { add(SvgDecoder(LocalContext.current)) }
        .build()
    return rememberImagePainter(item.logoUrl, imageLoader)
}
