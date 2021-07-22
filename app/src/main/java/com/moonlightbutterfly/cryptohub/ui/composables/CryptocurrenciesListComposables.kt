package com.moonlightbutterfly.cryptohub.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.ImageLoader
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.decode.SvgDecoder
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.dataobjects.CryptocurrencyListItem
import com.moonlightbutterfly.cryptohub.ui.LocalViewModelFactory
import com.moonlightbutterfly.cryptohub.viewmodels.CryptocurrenciesListViewModel

@Composable
fun CryptocurrenciesListScreen() {
    val cryptocurrencyItems: LazyPagingItems<CryptocurrencyListItem> = viewModel<CryptocurrenciesListViewModel>(
        factory = LocalViewModelFactory.current
    )
        .cryptocurrencies
        .collectAsLazyPagingItems()
    LazyColumn {
        items(cryptocurrencyItems) {
            CryptocurrencyListItem(item = it!!)
        }
    }
}

@Composable
fun CryptocurrencyListItem(item: CryptocurrencyListItem) {
    Row(
        Modifier
            .padding(20.dp)
            .height(50.dp)) {
        CryptocurrencyLogoFor(item)
        CryptocurrencyNameColumnFor(item)
        CryptocurrencyPriceColumnFor(item)
    }
}

@Composable
fun CryptocurrencyLogoFor(item: CryptocurrencyListItem) {
    val painter = getImagePainterFor(item)
    Image(
        painter = painter,
        modifier = Modifier
            .padding(end = 10.dp)
            .size(50.dp),
        contentScale = ContentScale.Inside,
        contentDescription = stringResource(R.string.cryptocurrency_item_list_image_description),
    )
}

@Composable
private fun getImagePainterFor(item: CryptocurrencyListItem): ImagePainter {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .componentRegistry { add(SvgDecoder(LocalContext.current)) }
        .build()
    return rememberImagePainter(item.logoUrl, imageLoader)
}

@Composable
fun RowScope.CryptocurrencyNameColumnFor(item: CryptocurrencyListItem) {
    Column(
        Modifier
            .weight(3f)
            .fillMaxHeight(), Arrangement.SpaceBetween
    ) {
        Text(
            text = item.name,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = item.symbol,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
fun RowScope.CryptocurrencyPriceColumnFor(item: CryptocurrencyListItem) {
    Column(
        Modifier
            .weight(2f)
            .fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween
    ) {
        val textModifier = Modifier.align(Alignment.End)
        Text(
            modifier = textModifier,
            text = "${item.price} USD"
        )
        Text(
            modifier = textModifier,
            text = "${item.priceChange}%",
            style = getPriceChangeTextStyleFor(item)
        )
    }
}

private fun getPriceChangeTextStyleFor(item: CryptocurrencyListItem): TextStyle {
    return TextStyle(
        if (item.priceChange > 0) {
            Color.Green
        } else {
            Color.Red
        }
    )
}