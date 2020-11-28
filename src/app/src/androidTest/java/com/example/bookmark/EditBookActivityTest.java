package com.example.bookmark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

    @BeforeClass
    public static void setUpOnce() {
        StorageServiceProvider.setStorageService(MockStorageService.getMockStorageService());
    }

    @Rule
    public ActivityTestRule<EditBookActivity> rule =
        new ActivityTestRule<EditBookActivity>(EditBookActivity.class, true, true) {
            @Override
            protected Intent getActivityIntent() {
                User owner = MockModels.getMockOwner();
                Book book = new Book(owner, "TEST", "TEST", "TEST");
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("Book", book);
                intent.putExtras(bundle);
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
