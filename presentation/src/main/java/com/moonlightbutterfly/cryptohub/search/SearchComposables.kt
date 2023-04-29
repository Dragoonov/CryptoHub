package com.moonlightbutterfly.cryptohub.search

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.core.ErrorHandler
import com.moonlightbutterfly.cryptohub.list.CryptoAssetLogoFor
import com.moonlightbutterfly.cryptohub.list.CryptoAssetNameColumnForAsset
import com.moonlightbutterfly.cryptohub.models.CryptoAsset
import com.moonlightbutterfly.presentation.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalCoilApi
@Composable
fun SearchScreen(onCancelSearch: () -> Unit, onItemClicked: (asset: String) -> Unit, viewModel: SearchViewModel) {

    val error by viewModel.errorMessageFlow.collectAsStateWithLifecycle(null)
    error?.let { ErrorHandler(error) }

    val query by viewModel.currentSearchQuery.collectAsStateWithLifecycle(stringResource(id = R.string.search))
    val results by viewModel.cryptoAssetsResults.collectAsStateWithLifecycle(emptyList())
    val recents by viewModel.recents.collectAsStateWithLifecycle(emptyList())
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle(false)
    val searchFocusRequester = FocusRequester()

    Scaffold(
        topBar = {
            TopAppBar {
                SearchHeader(
                    query = query,
                    onQueryChange = viewModel::onQueryChange,
                    onCancelSearch = onCancelSearch,
                    focusRequester = searchFocusRequester
                )
            }
        },
    ) {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            if (query.isNotEmpty()) {
                if (isLoading) {
                    ProgressBar()
                } else {
                    results.forEach {
                        CryptoAssetSearchListItem(
                            asset = it,
                            onItemClicked = { asset ->
                                viewModel.onResultClicked(asset)
                                onItemClicked(asset.symbol)
                            }
                        )
                    }
                }
            } else {
                RecentlyViewedHeader(viewModel::onDeleteRecentsClicked)
                recents.reversed().forEach {
                    CryptoAssetSearchListItem(
                        asset = it,
                        onItemClicked = { asset ->
                            viewModel.onResultClicked(asset)
                            onItemClicked(asset.symbol)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    onCancelSearch: () -> Unit,
    focusRequester: FocusRequester
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            textStyle = TextStyle(fontSize = 18.sp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                cursorColor = MaterialTheme.colors.onPrimary,
                focusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .focusRequester(focusRequester)
                .semantics {
                    contentDescription = "SearchInputField"
                }
        )
        SideEffect {
            focusRequester.requestFocus()
        }
        Text(
            text = stringResource(id = R.string.cancel),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { onCancelSearch() }
        )
    }
}

@Composable
fun ProgressBar() {
    Row(
        Modifier
            .fillMaxWidth()
            .height(100.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun RecentlyViewedHeader(onDeleteRecentsClicked: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.recently_viewed),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(10.dp)
        )
        Text(
            text = stringResource(id = R.string.delete),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { onDeleteRecentsClicked() }
        )
    }
}

@ExperimentalCoilApi
@Composable
fun CryptoAssetSearchListItem(
    asset: CryptoAsset,
    onItemClicked: (asset: CryptoAsset) -> Unit,
) {
    Row(
        Modifier
            .padding(20.dp)
            .height(50.dp)
            .clickable { onItemClicked(asset) }
    ) {
        CryptoAssetLogoFor(asset)
        CryptoAssetNameColumnForAsset(asset)
    }
}
