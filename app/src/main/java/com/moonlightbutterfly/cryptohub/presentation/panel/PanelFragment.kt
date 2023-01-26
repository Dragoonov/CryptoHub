package com.moonlightbutterfly.cryptohub.presentation.panel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.annotation.ExperimentalCoilApi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PanelFragment : Fragment() {

    private val args: PanelFragmentArgs by navArgs()

    @OptIn(ExperimentalCoilApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel: CryptoAssetPanelViewModel by viewModels()
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent { CryptoAssetPanelScreen(cryptoAssetSymbol = args.symbol, viewModel) }
        }
    }
}
