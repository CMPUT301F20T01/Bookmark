package com.example.bookmark;

import android.app.Activity;
import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


/**
 * Perform intent testing on the SignupActivity
 * <p>
 * Outstanding Issues/TODOs
 * Test Signing Up (how to make sure that user exists?)
 *
 * @author Konrad Staniszewski.
 */
public class SignupActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<SignupActivity> rule =
        new ActivityTestRule<>(SignupActivity.class, true, true);

    /**
     * Runs before all tests and create the solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Test going back to login
     */
    @Test
    public void backToLogin() {
    }

    /**
     * Close the activity after each test
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}

