package com.moonlightbutterfly.cryptohub.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.dataobjects.CryptoassetItem

fun getPriceChangeTextStyleFor(item: CryptoassetItem): TextStyle {
    return TextStyle(
        if (item.dayChanges.priceChangePercent > 0) {
            Color.Green
        } else {
            Color.Red
        }
    )
}

fun getMarketCapChangeTextStyleFor(item: CryptoassetItem): TextStyle {
    return TextStyle(
        if (item.dayChanges.marketCapChangePct > 0) {
            Color.Green
        } else {
            Color.Red
        }
    )
}

fun getVolumeChangeTextStyleFor(item: CryptoassetItem): TextStyle {
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
fun getImagePainterFor(item: CryptoassetItem): ImagePainter {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .componentRegistry { add(SvgDecoder(LocalContext.current)) }
        .build()
    return rememberImagePainter(item.logoUrl, imageLoader)
}

@Composable
fun RowScope.Favourite(isInFavourites: Boolean, onSelectionChanged: (changed: Boolean) -> Unit) {
    Column(
        Modifier
            .weight(1f)
            .fillMaxHeight(),
        Arrangement.Center
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.End)
                .toggleable(
                value = isInFavourites,
                onValueChange = { onSelectionChanged(!isInFavourites) }),
            imageVector = if (isInFavourites) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = stringResource(id = R.string.favourites)
        )
    }
}
