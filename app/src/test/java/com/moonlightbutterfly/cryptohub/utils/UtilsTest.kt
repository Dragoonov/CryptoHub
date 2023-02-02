package com.moonlightbutterfly.cryptohub.utils

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Test

class UtilsTest {

    private val contextMock = mockk<Context> {
        every { getString(any()) } returns "mln"
    }

    @Test
    fun `should return proper abbreviation`() {
        // GIVEN
        var value = 234.0
        // WHEN
        var result = value.toStringAbbr(contextMock)
        // THEN
        assertEquals("234.0", result)

        // GIVEN
        value = 234_456_000.0
        // WHEN
        result = value.toStringAbbr(contextMock)
        // THEN
        assertEquals("234.46 mln", result)
    }
}
