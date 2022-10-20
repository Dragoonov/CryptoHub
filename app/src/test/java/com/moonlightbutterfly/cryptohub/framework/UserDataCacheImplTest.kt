package com.moonlightbutterfly.cryptohub.framework

import com.moonlightbutterfly.cryptohub.models.UserData
import junit.framework.TestCase.assertEquals
import org.junit.Test

class UserDataCacheImplTest {

    private val userDataCache = UserDataCacheImpl

    @Test
    fun `should manage user`() {
        // GIVEN
        val userData = UserData("test")

        // WHEN
        var result = userDataCache.getUserData()

        // THEN
        assertEquals(null, result)

        // WHEN
        userDataCache.signInUser(userData)
        result = userDataCache.getUserData()

        // THEN
        assertEquals(userData, result)

        // WHEN
        userDataCache.signOutUser()
        result = userDataCache.getUserData()

        // THEN
        assertEquals(null, result)
    }
}
