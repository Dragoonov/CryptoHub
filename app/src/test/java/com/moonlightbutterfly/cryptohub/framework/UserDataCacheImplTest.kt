package com.moonlightbutterfly.cryptohub.framework

import com.google.firebase.auth.FirebaseAuth
import com.moonlightbutterfly.cryptohub.domain.models.UserData
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Test

class UserDataCacheImplTest {

    private val firebaseAuth: FirebaseAuth = mockk {
        every { signOut() } just Runs
    }

    private val userDataCache = UserDataCacheImpl(firebaseAuth)

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
