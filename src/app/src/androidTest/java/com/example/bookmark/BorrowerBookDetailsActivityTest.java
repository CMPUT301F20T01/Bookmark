package com.example.bookmark;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

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
import org.junit.runner.RunWith;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;

/**
 * Perform intent testing on the BorrowerBookDetailsActivity
 *
 * @author Nayan Prakash.
 */
@RunWith(AndroidJUnit4.class)
public class BorrowerBookDetailsActivityTest {

    private Solo solo;

    /**
     * Set storage provider and current user
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
    public ActivityTestRule<BorrowerBookDetailsActivity> rule =
        new ActivityTestRule(BorrowerBookDetailsActivity.class, true, true) {
        @Override
        protected Intent getActivityIntent() {
            Book book = MockModels.getMockBook3();
            User user = MockModels.getMockRequester();
            Intent intent = new Intent();
            intent.putExtra(ListingBooksActivity.USER, user);
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
        solo.assertCurrentActivity("WRONG ACTIVITY", BorrowerBookDetailsActivity.class);
    }

    /**
     * Test text of action button on a unrequested book
     */
    @Test
    public void requestBook() {
        Button actionButton = rule.getActivity().findViewById(R.id.borrower_book_details_action_btn);
        assertEquals("REQUEST", actionButton.getText());
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
