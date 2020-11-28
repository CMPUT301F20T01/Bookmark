package com.example.bookmark;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.bookmark.mocks.MockModels;
import com.example.bookmark.mocks.MockStorageService;
import com.example.bookmark.models.Book;
import com.example.bookmark.models.User;
import com.example.bookmark.server.StorageServiceProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * Perform intent testing on the MyBooks
 *
 * @author Mitch Adam.
 */
@RunWith(AndroidJUnit4.class)
public class MyBooksActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MyBooksActivity> rule =
        new ActivityTestRule<>(MyBooksActivity.class, true, false);

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
     * This class sets up the shared preferences prior to the activity being triggered.
     */
    @BeforeClass
    public static void setUpSharedPref() {
        StorageServiceProvider.setStorageService(MockStorageService.getMockStorageService());

        SharedPreferences sharedPreferences = InstrumentationRegistry.getInstrumentation().getTargetContext()
            .getSharedPreferences("LOGGED_IN_USER", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USER_NAME", "john.smith42").commit();
    }

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        rule.launchActivity(new Intent());
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
     * Check the count of displayed books
     */
    @Test
    public void numberOfBooks() {
        ListView books_list = (ListView) solo.getView(R.id.my_books_listview);
        User owner = MockModels.getMockOwner();
        StorageServiceProvider.getStorageService().retrieveBooksByOwner(owner, books -> {
            assertEquals(books.size(), books_list.getCount());
        }, e -> fail("An error occurred while retrieving the books by owner."));
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

