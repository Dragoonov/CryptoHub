package com.moonlightbutterfly.cryptohub.utils

import junit.framework.TestCase.assertEquals
import org.junit.Test

class UtilsTest {

    @Test
    fun `should return proper abbrevation`() {
        // GIVEN
        var value = 234.0
        // WHEN
        var result = value.toStringAbbr()
        // THEN
        assertEquals("234.0", result)

        // GIVEN
        value = 234_456_000.0
        // WHEN
        result = value.toStringAbbr()
        // THEN
        assertEquals("234.46 mln", result)

        // GIVEN
        value = 234_355_324_543.4
        // WHEN
        result = value.toStringAbbr()
        // THEN
        assertEquals("234.36 bln", result)
    }
}
