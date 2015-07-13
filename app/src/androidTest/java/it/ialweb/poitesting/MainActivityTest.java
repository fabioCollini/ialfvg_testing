package it.ialweb.poitesting;

import android.support.test.rule.ActivityTestRule;

import com.squareup.spoon.Spoon;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import retrofit.Callback;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

public class MainActivityTest {
    @Rule public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, false, false);

    private ParseService parseService;

    private AvailableStationsManager availableStationsManager;

    @Before
    public void setUp() {
        parseService = Mockito.mock(ParseService.class);
        availableStationsManager = Mockito.mock(AvailableStationsManager.class);
        DownloaderManager.setInstance(new DownloaderManager(parseService, availableStationsManager));
    }

    @Test
    public void testNoData() throws InterruptedException {
        when(availableStationsManager.isAvailable(any(Station.class))).thenReturn(true);
        defineMock(parseService, new StationList());

        rule.launchActivity(null);

        onView(withId(R.id.no_data)).check(matches(isDisplayed()));

        Spoon.screenshot(rule.getActivity(), "No_data");
    }

    @Test
    public void testError() throws InterruptedException {
        when(availableStationsManager.isAvailable(any(Station.class))).thenReturn(true);
        doAnswer(new Answer<Object>() {

            private boolean first = true;

            @Override public Object answer(InvocationOnMock invocation) throws Throwable {
                Callback<StationList> callback = (Callback<StationList>) invocation.getArguments()[0];
                if (first) {
                    callback.failure(null);
                    first = false;
                } else {
                    callback.success(new StationList(new Station(0, 0, "aaaa")), null);
                }
                return null;
            }
        }).when(parseService).getStations(any(Callback.class));

        rule.launchActivity(null);

        Spoon.screenshot(rule.getActivity(), "Error");

        onView(withId(R.id.error_layout)).check(matches(isDisplayed()));

        onView(withId(R.id.retry)).perform(click());

        onView(withId(R.id.error_layout)).check(matches(not(isDisplayed())));
        onView(withId(R.id.total_count)).check(matches(withText("1")));

        Spoon.screenshot(rule.getActivity(), "Reload_after_error");
    }

    @Test
    public void testSingleStation() throws InterruptedException {
        when(availableStationsManager.isAvailable(any(Station.class))).thenReturn(true);
        defineMock(parseService, new StationList(new Station(0, 0, "aaaa")));

        rule.launchActivity(null);

        onView(withId(R.id.total_count)).check(matches(withText("1")));

        Spoon.screenshot(rule.getActivity(), "Single_station");
    }

    private void defineMock(ParseService parseService, final StationList result) {
        doAnswer(new Answer<Object>() {
            @Override public Object answer(InvocationOnMock invocation) throws Throwable {
                Callback<StationList> callback = (Callback<StationList>) invocation.getArguments()[0];
                callback.success(result, null);
                return null;
            }
        }).when(parseService).getStations(any(Callback.class));
    }
}
