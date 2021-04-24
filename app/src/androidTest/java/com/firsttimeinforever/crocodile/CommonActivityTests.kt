package com.firsttimeinforever.crocodile

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CommonActivityTests {
    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(MainMenuActivity::class.java)

    @Ignore("Espresso does not want to cooperate for some reason")
    @Test
    fun ensurePagerHasPageForEachRule() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val rules = appContext.resources.getStringArray(R.array.game_rules_fragment_text_array)
        onView(allOf(isCompletelyDisplayed(), withId(R.id.game_rules_pager_fragment_text_view)))
            .check(matches(withText(rules[0])))
            .perform(ViewActions.swipeLeft())
            .check(matches(withText(rules[1])))
            .perform(ViewActions.swipeLeft())
            .check(matches(withText(rules[2])))
    }

    @Test
    fun testBackToTheMenu() {
        onView(withId(R.id.game_rules_button))
            .perform(click())
        pressBack()
        onView(withId(R.id.imageView)).check(matches(notNullValue()))
    }

    @Test
    fun testBackToTheMenuButton() {
        onView(withId(R.id.game_rules_button))
            .perform(click())
        onView(withId(R.id.game_rules_back_button))
            .perform(click())
        onView(withId(R.id.imageView)).check(matches(notNullValue()))
    }

    @Test
    fun testThemesBackToTheMenu() {
        onView(withId(R.id.new_game_button))
            .perform(click())
        pressBack()
        onView(withId(R.id.imageView)).check(matches(notNullValue()))
    }
}
