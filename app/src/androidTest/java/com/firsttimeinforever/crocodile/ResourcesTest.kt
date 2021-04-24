package com.firsttimeinforever.crocodile

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class ResourcesTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.firsttimeinforever.crocodile", appContext.packageName)
    }

    @Test
    fun ensureThemesCountInSync() {
        val expected = ApplicationConfig.THEMES_COUNT;
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val names = appContext.resources.getStringArray(R.array.game_theme_names_array)
        val descriptions = appContext.resources.getStringArray(R.array.game_theme_descriptions_array)
        assertEquals(expected, names.size)
        assertEquals(expected, descriptions.size)
    }

    @Test
    fun ensureEachThemeHasWords() {
        val expected = ApplicationConfig.THEMES_COUNT;
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val words = appContext.resources.getStringArray(R.array.theme_words)
        assertEquals(expected, words.size)
    }

    @Test
    fun ensureThemeWordsAreNonEmpty() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val words = appContext.resources.getStringArray(R.array.theme_words)
        for (theme in words) {
            assertTrue(theme.split(',').isNotEmpty())
        }
    }
}
