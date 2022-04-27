package com.moonlightbutterfly.cryptohub.presentation.viewmodels

import com.moonlightbutterfly.cryptohub.domain.models.UserData
import junit.framework.TestCase.assertEquals
import org.junit.Test

class LoginViewModelTest {

    private val userData = UserData("test", "test", "test")
    private val viewModel = LoginViewModel(userData)

    @Test
    fun `should save user`() {
        // GIVEN
        val newData = UserData("new")

        // WHEN
        viewModel.saveUser(newData)

        // THEN
        assertEquals(newData.userId, userData.userId)
        assertEquals(newData.username, userData.username)
        assertEquals(newData.email, userData.email)
    }

    @Test
    fun `should clear user`() {
        // GIVEN
        assertEquals("test", userData.userId)
        assertEquals("test", userData.username)
        assertEquals("test", userData.email)

        // WHEN
        viewModel.clearUser()

        // THEN
        assertEquals("", userData.userId)
        assertEquals("", userData.username)
        assertEquals("", userData.email)
    }
}
