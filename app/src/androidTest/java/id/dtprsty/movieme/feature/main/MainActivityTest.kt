package id.dtprsty.movieme.feature.main

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import id.dtprsty.movieme.R.id.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest{
    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testAppBehaviour(){
        Espresso.onView(ViewMatchers.withId(spinner))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Thread.sleep(4000)
        Espresso.onView(ViewMatchers.withId(rvHighlight))
            .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()))
        Espresso.onView(ViewMatchers.withId(rvHighlight)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(2))
        Thread.sleep(4000)
        Espresso.onView(ViewMatchers.withId(nestedScrollview))
            .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()))
        Espresso.onView(ViewMatchers.withId(nestedScrollview)).perform(ViewActions.swipeUp(), ViewActions.click())
        Espresso.onView(ViewMatchers.withId(rvMovie))
            .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()))
        Espresso.onView(ViewMatchers.withId(rvMovie)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
        Espresso.onView(ViewMatchers.withId(rvMovie)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(10, ViewActions.click()))
        Thread.sleep(4000)
        Espresso.pressBack()
        Thread.sleep(4000)
        Espresso.onView(ViewMatchers.withId(spinner)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText("Upcoming")).perform(ViewActions.click())
        Thread.sleep(7000)
        Espresso.onView(ViewMatchers.withId(nestedScrollview)).perform(ViewActions.swipeUp(), ViewActions.click())
        Espresso.onView(ViewMatchers.withId(rvMovie))
            .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()))
        Espresso.onView(ViewMatchers.withId(rvMovie)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(8))
        Espresso.onView(ViewMatchers.withId(rvMovie)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(8, ViewActions.click()))
        Thread.sleep(4000)
        Espresso.pressBack()
        Thread.sleep(4000)
        Espresso.onView(ViewMatchers.withId(spinner)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText("Most Popular")).perform(ViewActions.click())
        Thread.sleep(7000)
        Espresso.onView(ViewMatchers.withId(nestedScrollview)).perform(ViewActions.swipeUp(), ViewActions.click())
        Espresso.onView(ViewMatchers.withId(rvMovie))
            .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()))
        Espresso.onView(ViewMatchers.withId(rvMovie)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(8))
        Espresso.onView(ViewMatchers.withId(rvMovie)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(8, ViewActions.click()))
        Thread.sleep(4000)

        Espresso.onView(ViewMatchers.withId(add_to_favorite))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(add_to_favorite)).perform(ViewActions.click())
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withText("Added To Favorite"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.pressBack()
        Thread.sleep(4000)
        Espresso.onView(ViewMatchers.withId(spinner)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText("Favorit")).perform(ViewActions.click())
        Thread.sleep(7000)
        Espresso.onView(ViewMatchers.withId(nestedScrollview)).perform(ViewActions.swipeUp(), ViewActions.click())
        Espresso.onView(ViewMatchers.withId(rvMovie))
            .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()))
        Espresso.onView(ViewMatchers.withId(rvMovie)).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(8))
        Espresso.onView(ViewMatchers.withId(rvMovie)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(8, ViewActions.click()))
        Thread.sleep(4000)
        Espresso.pressBack()
    }
}