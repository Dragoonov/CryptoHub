package com.moonlightbutterfly.cryptohub.presentation.signin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.presentation.core.CryptoHubTheme
import com.moonlightbutterfly.cryptohub.presentation.core.MainActivity
import com.moonlightbutterfly.cryptohub.presentation.core.SetStatusBarColor
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
                val isNightMode by viewModel.isNightModeEnabled.collectAsState(false)
                CryptoHubTheme(darkTheme = isNightMode) {
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
