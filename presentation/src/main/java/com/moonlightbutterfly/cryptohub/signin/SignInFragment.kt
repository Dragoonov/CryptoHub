package com.moonlightbutterfly.cryptohub.signin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.core.CryptoHubTheme
import com.moonlightbutterfly.cryptohub.core.LoadingBar
import com.moonlightbutterfly.cryptohub.core.MainActivity
import com.moonlightbutterfly.cryptohub.core.SetStatusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    @OptIn(ExperimentalCoilApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel: SignInViewModel by viewModels()
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                if (uiState.isLoading) {
                    LoadingBar()
                } else {
                    CryptoHubTheme(darkTheme = uiState.nightModeEnabled!!) {
                        SignInScreen(
                            onSignedIn = {
                                startActivity(Intent(activity, MainActivity::class.java))
                                activity?.finish()
                            },
                            viewModel
                        )
                        SetStatusBarColor()
                    }
                }
            }
        }
    }
}
