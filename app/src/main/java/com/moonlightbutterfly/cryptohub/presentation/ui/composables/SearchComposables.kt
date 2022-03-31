package com.moonlightbutterfly.cryptohub.presentation.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.domain.models.CryptoAsset
import com.moonlightbutterfly.cryptohub.presentation.ui.LocalViewModelFactory
import com.moonlightbutterfly.cryptohub.presentation.viewmodels.SearchViewModel

@ExperimentalCoilApi
@Composable
fun SearchScreen(onCancelSearch: () -> Unit, onItemClicked: (asset: String) -> Unit) {
    val viewModel = viewModel<SearchViewModel>(
        factory = LocalViewModelFactory.current
    )
    val query by viewModel.currentSearchQuery.observeAsState(stringResource(id = R.string.search))
    val results by viewModel.cryptoAssetsResults.observeAsState(emptyList())
    val recents by viewModel.recents.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
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
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            textStyle = TextStyle(fontSize = 20.sp),
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
        CryptoAssetNameColumnFor(asset)
    }
}
