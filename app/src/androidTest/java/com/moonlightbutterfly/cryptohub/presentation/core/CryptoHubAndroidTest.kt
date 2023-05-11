package com.moonlightbutterfly.cryptohub.presentation.core

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import coil.annotation.ExperimentalCoilApi
import com.moonlightbutterfly.cryptohub.core.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule

@HiltAndroidTest
open class CryptoHubAndroidTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @OptIn(ExperimentalCoilApi::class)
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
}
