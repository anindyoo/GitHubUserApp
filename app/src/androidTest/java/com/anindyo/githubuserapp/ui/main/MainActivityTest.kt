package com.anindyo.githubuserapp.ui.main

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.anindyo.githubuserapp.R
import org.junit.Before
import org.junit.Test

class MainActivityTest {
    private val dummySearchInputText = "anindyoo"

    @Before
    fun setup(){
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun assertUserDetail() {
        onView(withId(R.id.tf_input_edit)).perform(typeText(dummySearchInputText), closeSoftKeyboard())
        onView(withId(R.id.btn_search)).perform(click())
        onView(withId(R.id.rv_users)).check(ViewAssertions.matches(isDisplayed()))
        Thread.sleep(2000)

        onView(withId(R.id.rv_users))
            .perform(RecyclerViewActions.actionOnItemAtPosition<ListUserAdapter.ListViewHolder>(0, click()))
        onView(withId(R.id.tv_detail_name)).check(ViewAssertions.matches(isDisplayed()))
        Thread.sleep(1500)
    }

    @Test
    fun assertSetRemoveFavorite() {
        assertUserDetail()
        onView(withId(R.id.fab_add_favorite)).perform(click())
        onView(isRoot()).perform(pressBack())
        onView(withId(R.id.action_to_favorite_list)).perform(click())
        Thread.sleep(1500)

        onView(withText(dummySearchInputText)).perform(click())
        onView(withId(R.id.tv_detail_name)).check(ViewAssertions.matches(isDisplayed()))
        Thread.sleep(1500)

        onView(withId(R.id.fab_add_favorite)).perform(click())
        onView(isRoot()).perform(pressBack())
    }
}