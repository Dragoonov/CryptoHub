package com.moonlightbutterfly.cryptohub.presentation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.annotation.ExperimentalCoilApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview

@AndroidEntryPoint
class ListFragment : Fragment() {

    @OptIn(FlowPreview::class, ExperimentalCoilApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val viewModel: CryptoAssetsListViewModel by viewModels()

        val onItemClicked: (String) -> Unit = { findNavController().navigate(ListFragmentDirections.actionListFragmentToPanelFragment(it)) }

        val onSearchClicked = { findNavController().navigate(ListFragmentDirections.actionListFragmentToSearchFragment()) }

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                CryptoAssetsListScreen(
                    onItemClicked = onItemClicked,
                    onSearchClicked = onSearchClicked,
                    viewModel
                )
            }
        }
    }
}
