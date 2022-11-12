package com.moonlightbutterfly.cryptohub.framework

import com.moonlightbutterfly.cryptohub.data.Result
import com.moonlightbutterfly.cryptohub.data.getOrThrow
import com.moonlightbutterfly.cryptohub.framework.data.UserDataSourceImpl
import com.moonlightbutterfly.cryptohub.models.User
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class UserDataCacheImplTest {

    private val userDataCache = UserDataSourceImpl

    @Test
    fun `should manage user`() {
        // GIVEN
        val user = User("test")

        // WHEN
        var result = userDataCache.getUserData()
        var isUserSignedIn = userDataCache.isUserSignedIn().getOrThrow()

        // THEN
        assertTrue(result is Result.Failure)
        assertFalse(isUserSignedIn)

        // WHEN
        userDataCache.signInUser(user)
        isUserSignedIn = userDataCache.isUserSignedIn().getOrThrow()
        result = userDataCache.getUserData()

        // THEN
        assertTrue(isUserSignedIn)
        assertEquals(user, result.getOrThrow())

        // WHEN
        userDataCache.signOutUser()
        isUserSignedIn = userDataCache.isUserSignedIn().getOrThrow()
        result = userDataCache.getUserData()

        // THEN
        assertTrue(result is Result.Failure)
        assertFalse(isUserSignedIn)
    }
}
