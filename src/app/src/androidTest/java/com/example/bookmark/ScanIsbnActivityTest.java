package com.example.bookmark;

import android.app.Activity;
import android.view.View;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;


/**
 * Perform intent testing on the ScanIsbnActivity
 * <p>
 * Outstanding Issues/TODOs
 * Test camera opens
 *
 * @author Mitch Adam.
 */
public class ScanIsbnActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<ScanIsbnActivity> rule =
        new ActivityTestRule<>(ScanIsbnActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets the Activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Close activity after each test
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
