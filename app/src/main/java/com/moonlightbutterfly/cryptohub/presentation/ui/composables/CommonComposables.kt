package com.moonlightbutterfly.cryptohub.presentation.ui.composables

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import coil.size.Scale
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.data.Error
import com.moonlightbutterfly.cryptohub.models.CryptoAsset

@ExperimentalCoilApi
@Composable
fun getImagePainterFor(asset: CryptoAsset): ImagePainter {
    val imageLoader = LocalImageLoader.current
    return rememberImagePainter(
        data = asset.logoUrl,
        imageLoader = imageLoader,
        builder = {
            scale(Scale.FILL)
        }
    )
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
                    onValueChange = { onSelectionChanged(!isInFavourites) }
                ),
            imageVector = if (isInFavourites) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = stringResource(id = R.string.favourites)
        )
    }
}

@Composable
fun ErrorHandler(error: Error?) {
    val context = LocalContext.current
    val errorMessage = when (error) {
        is Error.AccessDenied -> stringResource(R.string.access_denied_error)
        is Error.Network -> stringResource(R.string.network_error)
        is Error.NotFound -> stringResource(R.string.not_found_error)
        is Error.ServiceUnavailable -> stringResource(R.string.service_unavailable_error)
        is Error.Unknown -> stringResource(R.string.unknown_error)
        null -> ""
    } + ": ${error?.message}"
    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(
                context,
                errorMessage,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
