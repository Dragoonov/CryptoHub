package com.moonlightbutterfly.cryptohub.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.dataobjects.CryptocurrencyItem
import com.moonlightbutterfly.cryptohub.ui.LocalViewModelFactory
import com.moonlightbutterfly.cryptohub.viewmodels.CryptocurrenciesListViewModel

@ExperimentalCoilApi
@Composable
fun CryptocurrenciesListScreen(onItemClicked: (symbol: String) -> Unit) {
    val cryptocurrencyItems: LazyPagingItems<CryptocurrencyItem> = viewModel<CryptocurrenciesListViewModel>(
        factory = LocalViewModelFactory.current
    )
        .cryptocurrencies
        .collectAsLazyPagingItems()
    LazyColumn {
        items(cryptocurrencyItems) {
            CryptocurrencyListItem(
                item = it!!,
                onItemClicked = onItemClicked
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
fun CryptocurrencyListItem(item: CryptocurrencyItem, onItemClicked: (symbol: String) -> Unit) {
    Row(
        Modifier
            .padding(20.dp)
            .height(50.dp)
            .clickable { onItemClicked(item.symbol) }
    ) {
        CryptocurrencyLogoFor(item)
        CryptocurrencyNameColumnFor(item)
        CryptocurrencyPriceColumnFor(item)
    }
}

@ExperimentalCoilApi
@Composable
fun CryptocurrencyLogoFor(item: CryptocurrencyItem) {
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
fun RowScope.CryptocurrencyNameColumnFor(item: CryptocurrencyItem) {
    Column(
        Modifier
            .weight(3f)
            .fillMaxHeight(),
        Arrangement.SpaceBetween
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
fun RowScope.CryptocurrencyPriceColumnFor(item: CryptocurrencyItem) {
    Column(
        Modifier
            .weight(2f)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        val textModifier = Modifier.align(Alignment.End)
        Text(
            modifier = textModifier,
            text = "${item.price} USD"
        )
        Text(
            modifier = textModifier,
            text = "${item.dayChanges.priceChangePercent}%",
            style = getPriceChangeTextStyleFor(item)
        )
    }
}
