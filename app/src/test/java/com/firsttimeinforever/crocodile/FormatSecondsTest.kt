package com.firsttimeinforever.crocodile

import org.junit.Test

import org.junit.Assert.*

class FormatSecondsTest {
    @Test
    fun `test 5 seconds`() {
        assertEquals("00:05", Utility.formatSeconds(5))
    }

    @Test
    fun `test 59 seconds`() {
        assertEquals("00:59", Utility.formatSeconds(59))
    }

    @Test
    fun `test 60 seconds`() {
        assertEquals("01:00", Utility.formatSeconds(60))
    }

    @Test
    fun `test 70 seconds`() {
        assertEquals("01:10", Utility.formatSeconds(70))
    }

    @Test
    fun `test 120 seconds`() {
        assertEquals("02:00", Utility.formatSeconds(120))
    }
}
