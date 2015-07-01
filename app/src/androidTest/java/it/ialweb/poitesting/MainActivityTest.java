package it.ialweb.poitesting;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class MainActivityTest {
    @Rule public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, false, false);

    @Test
    public void testLoading() throws InterruptedException {
        rule.launchActivity(null);

        Thread.sleep(5000);

//        onView(withId(R.id.total_count)).check(matches(withText("???")));

        onView(withId(R.id.body)).check(matches(isDisplayed()));

        onView(withId(R.id.body)).perform(click());

        Thread.sleep(5000);
    }
}
