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
 * Perform intent testing on the EditBookActivity
 * <p>
 * Outstanding Issues/TODOs
 * Test Data entry
 * Test successfully edited book to correct user
 * Test delete book
 * Test delete photo
 * Test add photo
 *
 * @author Mitch Adam.
 */
public class EditBookActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<EditBookActivity> rule =
        new ActivityTestRule<>(EditBookActivity.class, true, true);

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
     * Test opening Scan ISBN activity
     */
    @Test
    public void scanISBN() {
        // Image 0 is the camera icon in the ISBN text field
        solo.clickOnImageButton(0);
        solo.assertCurrentActivity("WRONG ACTIVITY", ScanIsbnActivity.class);
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
