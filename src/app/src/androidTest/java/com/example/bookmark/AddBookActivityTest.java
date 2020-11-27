package com.example.bookmark;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;

import com.example.bookmark.mocks.MockModels;
import com.example.bookmark.mocks.MockStorageService;
import com.example.bookmark.models.Book;
import com.example.bookmark.models.User;
import com.example.bookmark.server.InMemoryStorageService;
import com.example.bookmark.server.StorageService;
import com.example.bookmark.server.StorageServiceProvider;
import com.google.android.material.textfield.TextInputLayout;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Perform intent testing on the AddBookActivity
 *
 * @author Mitch Adam.
 */
public class AddBookActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<AddBookActivity> rule =
        new ActivityTestRule<>(AddBookActivity.class, true, true);

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
     * Test opening scanISBN activity
     */
    @Test
    public void openScanISBN() {
        // Image 0 is the camera icon in the ISBN text field
        solo.clickOnImageButton(0);
        solo.assertCurrentActivity("WRONG ACTIVITY", ScanIsbnActivity.class);
    }

    /**
     * Test adding a book
     */
    @Test
    public void addBook() {
        final String title = "Test Title";
        final String author = "Test Author";
        final String isbn = "1234567890";
        final String description = "Test description";

        solo.enterText(((TextInputLayout) solo.getView(R.id.add_edit_book_title)).getEditText(),
            title);
        solo.enterText(((TextInputLayout) solo.getView(R.id.add_edit_book_author)).getEditText(),
            author);
        solo.enterText(((TextInputLayout) solo.getView(R.id.add_edit_book_isbn)).getEditText(),
            isbn);
        solo.enterText(((TextInputLayout) solo.getView(R.id.add_edit_book_description)).getEditText(),
            description);

        solo.clickOnButton("DONE");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        User owner = MockModels.getMockOwner();
        StorageServiceProvider.getStorageService().retrieveBook(owner, isbn, book -> {
            assertEquals(book.getTitle(), title);
            assertEquals(book.getAuthor(), author);
            assertEquals(book.getDescription(), description);
            StorageServiceProvider.getStorageService().deleteBook(book, aVoid -> {
            }, e -> fail("Error deleting added book"));
        }, e -> fail("An error occurred while retrieving the book."));
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
