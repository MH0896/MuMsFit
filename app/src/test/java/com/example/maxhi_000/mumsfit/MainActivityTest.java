package com.example.maxhi_000.mumsfit;

import org.junit.Before;

import static org.junit.Assert.*;

//imports from Roboelectronic
import android.app.Activity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)

public class MainActivityTest {

    private Activity cut;

    @Before
    public void setUp() throws Exception {
        cut = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void assertActivityTitle() throws Exception {
        String expectedTitle = cut.getString(R.string.app_name);
        assertEquals("Activity title did not match",
                expectedTitle,
                cut.getTitle().toString());
    }

}