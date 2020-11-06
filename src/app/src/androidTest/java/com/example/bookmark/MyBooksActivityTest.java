package com.example.bookmark;

import android.app.Activity;
import android.view.View;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Perform intent testing on the MyBooks
 * <p>
 * Outstanding Issues/TODOs
 * Test books correctly belong to user
 * Test search
 * Test filter
 * Test click on book to view details
 *
 * @author Mitch Adam.
 */
@RunWith(AndroidJUnit4.class)
public class MyBooksActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MyBooksActivity> rule =
        new ActivityTestRule<>(MyBooksActivity.class, true, true);

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
     * Test opening add book activity
     */
    @Test
    public void addBook() {
        View fab = rule.getActivity().findViewById(R.id.my_books_add_btn);
        solo.clickOnView(fab);
        solo.assertCurrentActivity("WRONG ACTIVITY", AddBookActivity.class);
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

