package com.moonlightbutterfly.cryptohub.list

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.core.Favourite
import com.moonlightbutterfly.cryptohub.core.LoadingBar
import com.moonlightbutterfly.cryptohub.core.getImagePainterFor
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.models.CryptoAssetMarketInfo
import com.moonlightbutterfly.cryptohub.presentation.R
import com.moonlightbutterfly.cryptohub.utils.round
import kotlinx.coroutines.FlowPreview

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@FlowPreview
@ExperimentalCoilApi
@Composable
fun CryptoAssetsListScreen(
    onItemClicked: (symbol: String) -> Unit,
    onSearchClicked: () -> Unit,
    viewModel: CryptoAssetsListViewModel
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cryptoAssets = uiState.assets.collectAsLazyPagingItems()
    var isFavouritesSelected by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar {
                Header(onSearchClicked)
            }
        }
    ) {
        Box {
            if (uiState.isLoading || cryptoAssets.loadState.refresh is LoadState.Loading) {
                LoadingBar(transparentBackground = true)
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Filters(isFavouritesSelected = isFavouritesSelected) {
                    isFavouritesSelected = !isFavouritesSelected
                }
                ListItems(
                    isFavouritesSelected = isFavouritesSelected,
                    favourites = uiState.favourites,
                    onItemClicked = onItemClicked,
                    onLiked = { asset ->
                        viewModel.acceptIntent(
                            CryptoAssetsListIntent.AddToFavourites(
                                asset
                            )
                        )
                    },
                    onDisliked = { asset ->
                        viewModel.acceptIntent(
                            CryptoAssetsListIntent.RemoveFromFavourites(asset)
                        )
                    },
                    cryptoAssets = cryptoAssets,
                    isLiked = { viewModel.isCryptoInFavourites(it.asset) }
                )
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
@SuppressWarnings("LongParameterList")
private fun ListItems(
    isFavouritesSelected: Boolean,
    favourites: List<CryptoAssetMarketInfo>,
    isLiked: (CryptoAssetMarketInfo) -> Boolean,
    onItemClicked: (symbol: String) -> Unit,
    onLiked: (CryptoAssetMarketInfo) -> Unit,
    onDisliked: (CryptoAssetMarketInfo) -> Unit,
    cryptoAssets: LazyPagingItems<CryptoAssetMarketInfo>,
) {
    LazyColumn {
        if (isFavouritesSelected) {
            items(favourites) {
                ListItemContent(
                    item = it,
                    isLiked = true,
                    onItemClicked = onItemClicked,
                    onLiked = onLiked,
                    onDisliked = onDisliked
                )
            }
        } else {
            items(cryptoAssets) {
                ListItemContent(
                    item = it!!,
                    isLiked = isLiked(it),
                    onItemClicked = onItemClicked,
                    onLiked = onLiked,
                    onDisliked = onDisliked,
                )
            }
        }
    }
}

@Composable
private fun Filters(
    isFavouritesSelected: Boolean,
    onSelectionChanged: () -> Unit
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
            onSelectionChanged = onSelectionChanged
        )
    }
}

@Composable
private fun Header(onSearchClicked: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.app_name),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(id = R.string.search),
            modifier = Modifier.clickable { onSearchClicked() }
        )
    }
}

@ExperimentalCoilApi
@Composable
private fun ListItemContent(
    item: CryptoAssetMarketInfo,
    isLiked: Boolean,
    onItemClicked: (symbol: String) -> Unit,
    onLiked: (CryptoAssetMarketInfo) -> Unit,
    onDisliked: (CryptoAssetMarketInfo) -> Unit
) {
    CryptoAssetListItem(
        asset = item.asset,
        assetPrice = item.price,
        onItemClicked = onItemClicked,
        isLiked = isLiked,
        onLiked = {
            if (it) {
                onLiked(item)
            } else {
                onDisliked(item)
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
    asset: CryptoAsset,
    assetPrice: Double,
    onItemClicked: (symbol: String) -> Unit,
    isLiked: Boolean,
    onLiked: (liked: Boolean) -> Unit
) {
    Row(
        Modifier
            .padding(20.dp)
            .height(50.dp)
            .clickable { onItemClicked(asset.symbol) }
    ) {
        CryptoAssetLogoFor(asset.logoUrl)
        CryptoAssetNameColumnForAsset(asset.name, asset.symbol)
        CryptoAssetPriceColumnForAsset(assetPrice)
        Favourite(
            modifier = Modifier.padding(start = 10.dp),
            isInFavourites = isLiked,
            onSelectionChanged = onLiked
        )
    }
}

@ExperimentalCoilApi
@Composable
fun CryptoAssetLogoFor(logoUrl: String) {
    val painter = getImagePainterFor(logoUrl)
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
fun RowScope.CryptoAssetNameColumnForAsset(name: String, symbol: String) {
    Column(
        Modifier
            .weight(1f)
            .fillMaxHeight()
            .padding(end = 10.dp),
        Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = symbol,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
fun RowScope.CryptoAssetPriceColumnForAsset(price: Double) {
    Column(
        Modifier
            .weight(1f)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
    ) {
        val textModifier = Modifier.align(Alignment.End)
        Text(
            modifier = textModifier,
            text = "${price.round(2)} USD"
        )
    }
}
