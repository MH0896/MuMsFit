package com.example.maxhi_000.mumsfit;

import android.app.Instrumentation;
import android.content.Context;
import android.graphics.Movie;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import android.test.TouchUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule
            = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void prepareQueryList() throws InterruptedException {
        //onView(withId(R.id.addPlan)).perform(click());
        //Let Query & UI load
        Thread.sleep(1000);
    }

    @Test
    public void useAppContext() {

        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.maxhi_000.mumsfit", appContext.getPackageName());

    }

    @Test
    public void newPlan() throws  Exception {
        onView(withId(R.id.addPlan)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.calabash)).perform(ViewActions.typeText("Plan"));
        onView(withText("Okay")).perform(click());
        assertTrue(mActivityRule.getActivity().isFinishing());
    }

    @Test
    public void viewPlan() throws Exception {
        final ListView ViewPlan = (ListView) mActivityRule.getActivity().findViewById(R.id.viewPlans);
        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewPlan.performItemClick(ViewPlan.getAdapter().getView(0, null, null), 0, ViewPlan.getAdapter().getItemId(0));
            }
        });
        assertFalse(mActivityRule.getActivity().isFinishing());
    }

    @Test
    public void deletePlan() throws Exception {
        final ListView ViewPlan = (ListView) mActivityRule.getActivity().findViewById(R.id.viewPlans);
        String firstPlan = ViewPlan.getItemAtPosition(0).toString();
        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewPlan.setItemChecked(0, true);
            }
        });
        Thread.sleep(1000);
        onView(withId(R.id.item_delete)).perform(click());
        Thread.sleep(1000);
        onView(withText("Okay")).perform(click());
        assertEquals(firstPlan, ViewPlan.getItemAtPosition(0).toString());
    }

    @Test
    public void selectAll() throws Exception {
        final ListView ViewPlan = (ListView) mActivityRule.getActivity().findViewById(R.id.viewPlans);
        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewPlan.setItemChecked(2, true);
            }
        });
        Thread.sleep(1000);
        onView(withId(R.id.item_select_all)).perform(click());
        assertTrue(ViewPlan.getCheckedItemCount() == ViewPlan.getCount());

    }

    @Test
    public void rename() throws Exception {
        final ListView ViewPlan = (ListView) mActivityRule.getActivity().findViewById(R.id.viewPlans);
        mActivityRule.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewPlan.setItemChecked(1, true);
            }
        });
        Thread.sleep(1000);
        onView(withId(R.id.item_menu_overflow)).perform(click());
        Thread.sleep(1000);
        onView(withText("Edit")).perform(click());
        onView(withId(R.id.calabash)).perform(ViewActions.clearText());
        Thread.sleep(1000);
        onView(withId(R.id.calabash)).perform(ViewActions.typeText("Unit-Test"));
        onView(withText("Okay")).perform(click());
        Thread.sleep(1000);
        assertFalse(ViewPlan.getItemAtPosition(1) == "Unit-Test");
    }

/*
    @Test
    public void seeMovieDetails() throws MovieDbException {
        Instrumentation.ActivityMonitor monitor = InstrumentationRegistry.getInstrumentation().addMonitor(MovieDetailsActivity.class.getName(), null, false);

        final AddMovieActivity addMovieActivity = mActivityRule.getActivity();

        final ListView queryList = (ListView) addMovieActivity.findViewById(R.id.queryListView);

        addMovieActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                queryList.performItemClick(queryList, 0, 0);

            }
        });

        MovieInfo selectedMovie = (MovieInfo) queryList.getAdapter().getItem(0);

        View row = queryList.getChildAt(0);

        row.findViewById(R.id.movieImage);

        String ratingScore;

        if (selectedMovie.getPopularity() > 10) {
            ratingScore = "10";
        } else {
            ratingScore = Float.toString(selectedMovie.getPopularity()).substring(0, 3);
        }

        String expectedDesc = selectedMovie.getOverview();
        String expectedGenre = HelperFunctions.getInstance().getGenreMap("de").get(selectedMovie.getGenreIds().get(0)) + ", " + selectedMovie.getReleaseDate().substring(0, 4);
        String expectedTitle = selectedMovie.getTitle();
        String expectedRating = ratingScore + "/10";

        MovieDetailsActivity movieDetails = (MovieDetailsActivity) monitor.waitForActivity();

        assertNotNull(movieDetails);

        TextView desc = (TextView) movieDetails.findViewById(R.id.movie_desc);
        TextView title = (TextView) movieDetails.findViewById(R.id.title);
        TextView rating = (TextView) movieDetails.findViewById(R.id.rating);
        TextView genre = (TextView) movieDetails.findViewById(R.id.genre);

        assertEquals(desc.getText(),expectedDesc);
        assertEquals(title.getText(), expectedTitle);
        assertEquals(rating.getText(), expectedRating);
        assertEquals(genre.getText(), expectedGenre);
    }*/
}