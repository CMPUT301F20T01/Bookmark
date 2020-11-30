package com.example.bookmark;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.bookmark.abstracts.ListingBooksActivity;
import com.example.bookmark.mocks.MockModels;
import com.example.bookmark.mocks.MockStorageService;
import com.example.bookmark.models.Book;
import com.example.bookmark.models.User;
import com.example.bookmark.server.StorageServiceProvider;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import static android.content.Context.MODE_PRIVATE;

/**
 * Perform intent testing on the MyBookDetailsActivity
 *
 * @author Mitch Adam.
 */
public class MyBookDetailsActivityTest {
    private Solo solo;

    /**
     * Set Storage provider and current user
     */
    @BeforeClass
    public static void setUpOnce() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("LOGGED_IN_USER", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USER_NAME", "john.smith42").commit();
        StorageServiceProvider.setStorageService(MockStorageService.getMockStorageService());
    }

    @Rule
    public ActivityTestRule<MyBookDetailsActivity> rule =
        new ActivityTestRule<MyBookDetailsActivity>(MyBookDetailsActivity.class, true, true) {
            @Override
            protected Intent getActivityIntent() {
                Book book = MockModels.getMockBook2();
                Intent intent = new Intent();
                intent.putExtra(ListingBooksActivity.EXTRA_BOOK, book);
                return intent;
            }
        };

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
    public void openEditBook() {
        View scanISBNBtn = rule.getActivity().findViewById(R.id.menu_edit_edit_btn);
        solo.clickOnView(scanISBNBtn);
        solo.assertCurrentActivity("WRONG ACTIVITY", EditBookActivity.class);
    }

    /**
     * Test manage book request
     */
    @Test
    public void manageRequest() {
        View scanISBNBtn = rule.getActivity().findViewById(R.id.book_details_action_btn);
        solo.clickOnView(scanISBNBtn);
        solo.assertCurrentActivity("WRONG ACTIVITY", ManageRequestsActivity.class);
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
