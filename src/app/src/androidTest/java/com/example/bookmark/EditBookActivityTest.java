package com.example.bookmark;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.bookmark.mocks.MockModels;
import com.example.bookmark.mocks.MockStorageService;
import com.example.bookmark.models.Book;
import com.example.bookmark.models.User;
import com.example.bookmark.server.StorageServiceProvider;
import com.google.android.material.textfield.TextInputLayout;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Perform intent testing on the EditBookActivity
 *
 * @author Mitch Adam.
 */
public class EditBookActivityTest {
    private Solo solo;
    private Book testBook;

    @Rule
    public ActivityTestRule<EditBookActivity> rule =
        new ActivityTestRule<EditBookActivity>(EditBookActivity.class, true, true) {
            @Override
            protected Intent getActivityIntent() {
                testBook = MockModels.getMockBook3();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("Book", testBook);
                intent.putExtras(bundle);
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

        Activity activity = rule.getActivity();
    }

    /**
     * Test opening Scan ISBN activity
     */
    @Test
    public void openScanISBN() {
        // Image 0 is the camera icon in the ISBN text field
        solo.clickOnImageButton(0);
        solo.assertCurrentActivity("WRONG ACTIVITY", ScanIsbnActivity.class);
    }

    /**
     * Test Editing a book
     */
    @Test
    public void editBook() {
        final String title = "Edited Title";
        final String author = "Edited Author";
        final String description = "Edited description";

        solo.clearEditText(((TextInputLayout) solo.getView(R.id.add_edit_book_title)).getEditText());
        solo.clearEditText(((TextInputLayout) solo.getView(R.id.add_edit_book_author)).getEditText());
        solo.clearEditText(((TextInputLayout) solo.getView(R.id.add_edit_book_description)).getEditText());
        solo.enterText(((TextInputLayout) solo.getView(R.id.add_edit_book_title)).getEditText(),
            title);
        solo.enterText(((TextInputLayout) solo.getView(R.id.add_edit_book_author)).getEditText(),
            author);
        solo.enterText(((TextInputLayout) solo.getView(R.id.add_edit_book_description)).getEditText(),
            description);

        solo.clickOnButton("DONE");
        User owner = MockModels.getMockOwner();
        StorageServiceProvider.getStorageService().retrieveBook(owner, testBook.getIsbn(), book -> {
            assertEquals(title, book.getTitle());
            assertEquals(author, book.getAuthor());
            assertEquals(description, book.getDescription());
        }, e -> fail("An error occurred while retrieving the book."));
    }

    /**
     * Test Back Button does not edit book
     */
    public void doNotEditBook() {
    }

    /**
     * Test Delete Book
     */
    @Test
    public void deleteBook() {

        solo.clickOnButton("Delete");
        User owner = MockModels.getMockOwner();
        StorageServiceProvider.getStorageService().retrieveBook(owner, testBook.getIsbn(), book2 ->
                StorageServiceProvider.getStorageService().storeBook(testBook, aVoid2 -> {
                    assertNull(book2);
                }, e -> fail("An error occurred while recreating the deleted book.")),
            e -> fail("An error occurred while retrieving the deleted book."));
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
