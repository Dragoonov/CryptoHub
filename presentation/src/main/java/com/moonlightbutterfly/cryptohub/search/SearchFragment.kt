package com.moonlightbutterfly.cryptohub.search

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

@AndroidEntryPoint
class SearchFragment : Fragment() {

    @OptIn(ExperimentalCoilApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel: SearchViewModel by viewModels()

        val onCancelSearch: () -> Unit = { findNavController().popBackStack() }

        val onItemClicked: (String) -> Unit = {
            findNavController()
                .navigate(SearchFragmentDirections.actionSearchFragmentToPanelFragment(it))
        }

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                SearchScreen(
                    onCancelSearch = onCancelSearch,
                    onItemClicked = onItemClicked,
                    viewModel
                )
            }
        }
    }
}
