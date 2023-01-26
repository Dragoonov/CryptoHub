package com.moonlightbutterfly.cryptohub.presentation.signin

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.moonlightbutterfly.cryptohub.R
import com.moonlightbutterfly.cryptohub.string
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SignInTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<SignInActivity>()

    @Test
    fun testSignInPanelVisible() {
        composeTestRule.apply {
            onNodeWithText(string(R.string.sign_in)).assertIsDisplayed()
            onNodeWithText(string(R.string.or)).assertIsDisplayed()
            onNodeWithText(string(R.string.continue_without_sign_in)).assertIsDisplayed()
        }
    }
}
