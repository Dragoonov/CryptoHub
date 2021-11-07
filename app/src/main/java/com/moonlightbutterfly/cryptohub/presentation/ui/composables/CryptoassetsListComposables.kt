package com.moonlightbutterfly.cryptohub.presentation.ui.composables

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAssetMarketInfo
import com.moonlightbutterfly.cryptohub.presentation.ui.LocalViewModelFactory
import com.moonlightbutterfly.cryptohub.presentation.viewmodels.CryptoAssetsListViewModel
import com.moonlightbutterfly.cryptohub.utils.round
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoilApi
@Composable
fun CryptoAssetsListScreen(onItemClicked: (symbol: String) -> Unit) {

    val viewModel = viewModel<CryptoAssetsListViewModel>(
        factory = LocalViewModelFactory.current
    )
    val cryptoAssets = viewModel.cryptoAssets.collectAsLazyPagingItems()

    val favourites by viewModel.favourites.collectAsState(initial = emptyList())

    var isFavouritesSelected by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Row(
            Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            Arrangement.Start
        ) {
            Chip(
                name = stringResource(id = R.string.favourites),
                isSelected = isFavouritesSelected,
                onSelectionChanged = { isFavouritesSelected = !isFavouritesSelected }
            )
        }
        LazyColumn {
            if (isFavouritesSelected) {
                items(favourites) {
                    ListItemContent(item = it, viewModel = viewModel, onItemClicked = onItemClicked)
                }
            } else {
                items(cryptoAssets) {
                    ListItemContent(item = it, viewModel = viewModel, onItemClicked = onItemClicked)
                }
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
private fun ListItemContent(
    item: CryptoAssetMarketInfo?,
    viewModel: CryptoAssetsListViewModel,
    onItemClicked: (symbol: String) -> Unit
) {
    val isLiked by viewModel.isCryptoInFavourites(item!!.asset).observeAsState(false)
    CryptoAssetListItem(
        asset = item!!,
        onItemClicked = onItemClicked,
        isLiked = isLiked,
        onLiked = {
            if (it) {
                viewModel.addToFavourites(item.asset)
            } else {
                viewModel.removeFromFavourites(item.asset)
            }
        }
    )
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
                onValueChange = { onSelectionChanged() }
            ),
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
fun CryptoAssetListItem(
    asset: CryptoAssetMarketInfo,
    onItemClicked: (symbol: String) -> Unit,
    isLiked: Boolean,
    onLiked: (liked: Boolean) -> Unit
) {
    Row(
        Modifier
            .padding(20.dp)
            .height(50.dp)
            .clickable { onItemClicked(asset.asset.symbol) }
    ) {
        CryptoAssetLogoFor(asset)
        CryptoAssetNameColumnFor(asset)
        CryptoAssetPriceColumnFor(asset)
        Favourite(isInFavourites = isLiked, onSelectionChanged = onLiked)
    }
}

@ExperimentalCoilApi
@Composable
fun CryptoAssetLogoFor(asset: CryptoAssetMarketInfo) {
    val painter = getImagePainterFor(asset)
    Image(
        painter = painter,
        modifier = Modifier
            .padding(end = 10.dp)
            .size(50.dp),
        contentScale = ContentScale.Fit,
        contentDescription = stringResource(R.string.crypto_asset_item_list_image_description),
    )
}

@Composable
fun RowScope.CryptoAssetNameColumnFor(asset: CryptoAssetMarketInfo) {
    Column(
        Modifier
            .weight(3f)
            .fillMaxHeight(),
        Arrangement.SpaceBetween
    ) {
        Text(
            text = asset.asset.name,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = asset.asset.symbol,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
fun RowScope.CryptoAssetPriceColumnFor(asset: CryptoAssetMarketInfo) {
    Column(
        Modifier
            .weight(2f)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        val textModifier = Modifier.align(Alignment.End)
        Text(
            modifier = textModifier,
            text = "${asset.price.round(2)} USD"
        )
    }
}
