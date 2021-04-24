package com.firsttimeinforever.crocodile

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameWalkThroughTest {
    @Rule
    @JvmField
    val activityRule = ActivityScenarioRule(MainMenuActivity::class.java)

    @Test
    fun walkThroughToMidGame() {
        onView(withId(R.id.new_game_button))
            .check(matches(CoreMatchers.notNullValue()))
            .perform(click())
        onView(withId(R.id.game_theme_select_recycler_view))
            .check(matches(CoreMatchers.notNullValue()))
            .perform(click())
        onView(withId(R.id.game_settings_start_game_button))
            .check(matches(CoreMatchers.notNullValue()))
            .perform(click())
        onView(withId(R.id.mid_turn_stop_game_button))
            .check(matches(CoreMatchers.notNullValue()))
            .perform(click())
        onView(withText("Do you really want to stop current game?"))
            .inRoot(isDialog())
    }
}
