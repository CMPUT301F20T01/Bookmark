package com.example.bookmark;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bookmark.abstracts.ListingBooksActivity;
import com.example.bookmark.mocks.MockModels;
import com.example.bookmark.mocks.MockStorageService;
import com.example.bookmark.models.Book;
import com.example.bookmark.models.Request;
import com.example.bookmark.server.StorageServiceProvider;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static android.content.Context.MODE_PRIVATE;

/**
 * Perform intent testing on the BorrowBookActivity
 *
 * @author Nayan Prakash
 */
public class BorrowBookActivityTest {
    private Solo solo;
    private Book book;
    private Request request;

    @Rule
    public ActivityTestRule<BorrowBookActivity> rule =
        new ActivityTestRule<BorrowBookActivity>(BorrowBookActivity.class, true, true) {
            @Override
            protected Intent getActivityIntent() {
                book = MockModels.getMockBook2();
                request = MockModels.getMockRequest2();
                Intent intent = new Intent();
                intent.putExtra(ListingBooksActivity.EXTRA_BOOK, book);
                intent.putExtra(BorrowBookActivity.EXTRA_REQUEST, request);
                return intent;
            }
        };

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
        solo.assertCurrentActivity("WRONG ACTIVITY", BorrowBookActivity.class);
    }

    /**
     * Test opening Scan ISBN activity
     */
    @Test
    public void openScanISBN() {
        View buttonView = rule.getActivity().findViewById(R.id.borrow_book_scan_isbn_button);
        solo.clickOnView(buttonView);
        solo.assertCurrentActivity("WRONG ACTIVITY", ScanIsbnActivity.class);
    }
}
