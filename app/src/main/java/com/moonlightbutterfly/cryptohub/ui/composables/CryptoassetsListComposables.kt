package com.moonlightbutterfly.cryptohub.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.dataobjects.CryptoassetItem
import com.moonlightbutterfly.cryptohub.ui.LocalViewModelFactory
import com.moonlightbutterfly.cryptohub.viewmodels.CryptoassetsListViewModel

@ExperimentalCoilApi
@Composable
fun CryptoassetsListScreen(onItemClicked: (symbol: String) -> Unit) {

    val viewModel = viewModel<CryptoassetsListViewModel>(
        factory = LocalViewModelFactory.current
    )
    val cryptoassetItems = viewModel.cryptoassets.collectAsLazyPagingItems()

    val favourites = viewModel.favourites.collectAsLazyPagingItems()

    var isFavouritesSelected by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
        Row(
            Modifier
                .padding(10.dp)
                .fillMaxWidth(), Arrangement.Start
        ) {
            Chip(
                name = stringResource(id = R.string.favourites),
                isSelected = isFavouritesSelected,
                onSelectionChanged = { isFavouritesSelected = !isFavouritesSelected }
            )
        }
        LazyColumn {
            items(if (isFavouritesSelected) favourites else cryptoassetItems) { item ->
                val isLiked by viewModel.isCryptoInFavourites(item!!.symbol).observeAsState(false)
                CryptoassetListItem(
                    item = item!!,
                    onItemClicked = onItemClicked,
                    isLiked = isLiked,
                    onLiked = {
                        if (it) {
                            viewModel.addCryptoToFavourites(item.symbol)
                        } else {
                            viewModel.removeCryptoFromFavourites(item.symbol)
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Chip(
    name: String = "Chip",
    isSelected: Boolean = false,
    onSelectionChanged: () -> Unit = {},
) {
    Surface(
        modifier = Modifier
            .clip(CircleShape)
            .border(1.dp, MaterialTheme.colors.primary, CircleShape)
            .toggleable(
                value = isSelected,
                onValueChange = { onSelectionChanged() }),
        color = if (isSelected) MaterialTheme.colors.primary else Color.Transparent
    ) {
        Text(
            text = name,
            color = if (isSelected) Color.White else MaterialTheme.colors.primary,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@ExperimentalCoilApi
@Composable
fun CryptoassetListItem(
    item: CryptoassetItem,
    onItemClicked: (symbol: String) -> Unit,
    isLiked: Boolean,
    onLiked: (liked: Boolean) -> Unit
) {
    Row(
        Modifier
            .padding(20.dp)
            .height(50.dp)
            .clickable { onItemClicked(item.symbol) }
    ) {
        CryptoassetLogoFor(item)
        CryptoassetNameColumnFor(item)
        CryptoassetPriceColumnFor(item)
        Favourite(isInFavourites = isLiked, onSelectionChanged = onLiked)

    }
}

@ExperimentalCoilApi
@Composable
fun CryptoassetLogoFor(item: CryptoassetItem) {
    val painter = getImagePainterFor(item)
    Image(
        painter = painter,
        modifier = Modifier
            .padding(end = 10.dp)
            .size(50.dp),
        contentScale = ContentScale.Inside,
        contentDescription = stringResource(R.string.cryptoasset_item_list_image_description),
    )
}

@Composable
fun RowScope.CryptoassetNameColumnFor(item: CryptoassetItem) {
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
fun RowScope.CryptoassetPriceColumnFor(item: CryptoassetItem) {
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
