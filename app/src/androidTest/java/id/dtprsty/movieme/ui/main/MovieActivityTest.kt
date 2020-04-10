package id.dtprsty.movieme.ui.main

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import id.dtprsty.movieme.R.id.*
import id.dtprsty.movieme.util.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieActivityTest {
    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.getEspressoIdlingResource())
    }

    @Test
    fun testAppBehaviour() {
        Espresso.onView(ViewMatchers.withId(spinner))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(rvHighlight))
            .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()))
        Espresso.onView(ViewMatchers.withId(rvHighlight))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(2))
        Espresso.onView(ViewMatchers.withId(rvMovie))
            .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()))
        Espresso.onView(ViewMatchers.withId(rvMovie))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(10))
        Espresso.onView(ViewMatchers.withId(rvMovie)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                10,
                ViewActions.click()
            )
        )
        Espresso.pressBack()
        Espresso.onView(ViewMatchers.withId(spinner)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText("Upcoming")).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(rvMovie))
            .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()))
        Espresso.onView(ViewMatchers.withId(rvMovie))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(8))
        Espresso.onView(ViewMatchers.withId(rvMovie)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                8,
                ViewActions.click()
            )
        )
        Espresso.onView(ViewMatchers.withId(add_to_favorite))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(add_to_favorite)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText("Added To Favorite"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.pressBack()

        Espresso.onView(ViewMatchers.withId(navigation))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(menu_tvshow)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(rvTvShow))
            .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()))


        Espresso.onView(ViewMatchers.withId(menu_favorite)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(rvMovie))
            .check(ViewAssertions.matches(ViewMatchers.isCompletelyDisplayed()))

        Espresso.onView(ViewMatchers.withId(spinner)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText("TV Show")).perform(ViewActions.click())

    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.getEspressoIdlingResource())
    }
}