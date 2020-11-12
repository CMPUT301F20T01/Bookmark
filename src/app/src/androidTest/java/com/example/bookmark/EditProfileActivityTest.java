package com.example.bookmark;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


/**
 * Perform intent testing on EditProfileActivity
 * <p>
 * Outstanding Issues/TODOs
 * Test that user details are actually updated?
 * @author Konrad Staniszewski.
 */
public class EditProfileActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<EditProfileActivity> rule =
        new ActivityTestRule<>(EditProfileActivity.class, true, true);

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
    public void doneEditing() {
        // figure out how to test this screen given no one is "logged" in
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
