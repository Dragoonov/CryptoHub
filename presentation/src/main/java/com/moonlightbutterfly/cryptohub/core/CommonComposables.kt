package com.moonlightbutterfly.cryptohub.core

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import coil.size.Scale
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.moonlightbutterfly.cryptohub.data.common.Error
import com.moonlightbutterfly.cryptohub.presentation.R

@Composable
fun SetStatusBarColor() {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight
    val statusBarColor = MaterialTheme.colors.primary
    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = useDarkIcons
        )
    }
}
@ExperimentalCoilApi
@Composable
fun getImagePainterFor(logoUrl: String): ImagePainter {
    val imageLoader = LocalImageLoader.current
    return rememberImagePainter(
        data = logoUrl,
        imageLoader = imageLoader,
        builder = {
            scale(Scale.FILL)
        }
    )
}

@Composable
fun Favourite(modifier: Modifier, isInFavourites: Boolean, onSelectionChanged: (changed: Boolean) -> Unit) {
    Column(
        modifier
            .wrapContentWidth()
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

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoadingBar(transparentBackground: Boolean = false) {
    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(if (transparentBackground) Color.Transparent else MaterialTheme.colors.onPrimary),
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
            )
        }
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
