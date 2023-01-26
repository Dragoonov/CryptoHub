package com.moonlightbutterfly.cryptohub

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import org.junit.rules.TestRule

fun <R : TestRule, A : ComponentActivity> AndroidComposeTestRule<R, A>.string(@StringRes id: Int): String = this.activity.getString(id)
