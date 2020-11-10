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
 * Perform intent testing on the MainActivity
 * <p>
 * Outstanding Issues/TODOs
 * Test logging in (how to make sure that user exists?)
 * @author Konrad Staniszewski.
 */
public class MainActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule =
        new ActivityTestRule<>(MainActivity.class, true, true);

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
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Test opening signup activity
     */
    @Test
    public void signUp() {
        View signUpBtn = rule.getActivity().findViewById(R.id.login_signup_button);
        solo.clickOnView(signUpBtn);
        solo.assertCurrentActivity("WRONG ACTIVITY", SignUpActivity.class);
    }

    /**
     * Close the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}

