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

public class MyBookDetailsActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MyBookDetailsActivity> rule =
        new ActivityTestRule<>(MyBookDetailsActivity.class, true, true);

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
     * Test Opening Edit book activity
     */
    @Test
    public void editBook() {
        View scanISBNBtn = rule.getActivity().findViewById(R.id.menu_edit_edit_btn);
        solo.clickOnView(scanISBNBtn);
        solo.assertCurrentActivity("WRONG ACTIVITY", EditBookActivity.class);
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
