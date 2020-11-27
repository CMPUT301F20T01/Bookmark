package com.example.bookmark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bookmark.models.Book;
import com.example.bookmark.models.User;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

/**
 * Perform intent testing on the MyBookDetailsActivity
 * <p>
 * Outstanding Issues/TODOs
 * Test that the text values match title, author, etc
 *
 * @author Mitch Adam.
 */
public class MyBookDetailsActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MyBookDetailsActivity> rule =
        new ActivityTestRule<>(MyBookDetailsActivity.class, true, false);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        User testUser = new User("test",
            "test",
            "test",
            "test",
            "test"
        );
        Bundle bundle = new Bundle();
        bundle.putSerializable("User", testUser);
        bundle.putSerializable("Book", new Book(testUser, "test", "test", "test"));
        Intent intent = new Intent();
        intent.putExtras(bundle);
        rule.launchActivity(intent);
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets the Activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
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
