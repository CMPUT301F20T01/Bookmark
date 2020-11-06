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
 * Perform intent testing on the AddBookActivity
 * <p>
 * Outstanding Issues/TODOs
 * Test Data entry
 * Test successfully added book to correct user
 * Test add photo
 *
 * @author Mitch Adam.
 */
public class AddBookActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<AddBookActivity> rule =
        new ActivityTestRule<>(AddBookActivity.class, true, true);

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
     * Test opening scanISBN activity
     */
    @Test
    public void scanISBN() {
        View scanISBNBtn = rule.getActivity().findViewById(R.id.add_book_scan_isbn_btn);
        solo.clickOnView(scanISBNBtn);
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
