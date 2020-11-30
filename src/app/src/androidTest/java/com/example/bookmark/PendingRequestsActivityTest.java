package com.example.bookmark;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ListView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bookmark.mocks.MockModels;
import com.example.bookmark.mocks.MockStorageService;
import com.example.bookmark.models.Book;
import com.example.bookmark.models.User;
import com.example.bookmark.server.StorageServiceProvider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * Perform intent testing on PendingRequestsActivity.
 *
 * @author Ryan Kortbeek.
 */
@RunWith(AndroidJUnit4.class)
public class PendingRequestsActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<PendingRequestsActivity> rule =
        new ActivityTestRule<>(PendingRequestsActivity.class, true, false);

    /**
     * Sets the storage provider and the user.
     */
    @BeforeClass
    public static void setUpOnce() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("LOGGED_IN_USER", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USER_NAME", "mary.jane9").commit();
        StorageServiceProvider.setStorageService(MockStorageService.getMockStorageService());
    }

    /**
     * Sets up the shared preferences.
     */
    @BeforeClass
    public static void setUpSharedPref() {
        StorageServiceProvider.setStorageService(MockStorageService.getMockStorageService());
        SharedPreferences sharedPreferences = InstrumentationRegistry.getInstrumentation().getTargetContext()
            .getSharedPreferences("LOGGED_IN_USER", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USER_NAME", "mary.jane9").commit();
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
     * Gets activity.
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        solo.assertCurrentActivity("WRONG ACTIVITY", PendingRequestsActivity.class);
    }

    /**
     * Tests search functionality. Specifically the TextWatcher,
     * toggleSearchVisibility(), updateAdapterFilter(), etc.
     */
    @Test
    public void search() {
        View searchBtn =
            rule.getActivity().findViewById(R.id.menu_filter_search_search_btn);
        TextInputLayout searchBarLayout =
            rule.getActivity().findViewById(R.id.search_bar_textInputLayout);
        assertEquals(View.GONE, searchBarLayout.getVisibility());
        solo.clickOnView(searchBtn);
        solo.waitForText("Search");
        assertEquals(View.VISIBLE, searchBarLayout.getVisibility());
        TextInputEditText searchEditText =
            rule.getActivity().findViewById(R.id.search_bar_textInput);
        ListView listView =
            rule.getActivity().findViewById(R.id.books_listview);

        // Changes description of mock books
        MockModels.getMockBook1().setDescription("Learning to code!");
        MockModels.getMockBook2().setDescription("LEARNT BEHAVIOUR...");
        MockModels.getMockBook5().setDescription("Trying to LeArN.");

        solo.enterText(searchEditText, "lEaRn");
        assertTrue(solo.searchText("Code Complete 2"));
        assertTrue(solo.searchText("Programming Pearls"));
        assertEquals(3, listView.getCount());
        solo.enterText(searchEditText, " behaviour...");
        assertTrue(solo.searchText("Programming Pearls"));
        assertEquals(1, listView.getCount());
    }

    /**
     * Tests filter functionality. Specifically the onFilterUpdate callback,
     * updateAdapterFilter(), etc.
     */
    @Test
    public void filter() {
        View filterBtn =
            rule.getActivity().findViewById(R.id.menu_filter_search_filter_btn);
        ListView listView =
            rule.getActivity().findViewById(R.id.books_listview);
        // Assert setup is as expected
        assertTrue(solo.searchText("Requested"));
        assertTrue(solo.searchText("Accepted"));
        assertEquals(3, listView.getCount());

        solo.clickOnView(filterBtn);
        assertTrue(solo.waitForText("Filter Books"));

        // Verify all boxes are enabled by default
        assertTrue(solo.isCheckBoxChecked(0));
        assertTrue(solo.isCheckBoxChecked(1));
        assertTrue(solo.isCheckBoxChecked(2));
        assertTrue(solo.isCheckBoxChecked(3));

        // Uncheck Requested
        solo.clickOnCheckBox(1);
        solo.clickOnText("Apply");
        assertFalse(solo.searchText("Requested"));
        assertTrue(solo.searchText("Accepted"));
        assertEquals(1, listView.getCount());

        // Check Requested, uncheck Accepted
        solo.clickOnView(filterBtn);
        assertTrue(solo.waitForText("Filter Books"));
        solo.clickOnCheckBox(1);
        solo.clickOnCheckBox(2);
        solo.clickOnText("Apply");
        assertTrue(solo.searchText("Requested"));
        assertFalse(solo.searchText("Accepted"));
        assertEquals(2, listView.getCount());

        // Uncheck both
        solo.clickOnView(filterBtn);
        assertTrue(solo.waitForText("Filter Books"));
        solo.clickOnCheckBox(1);
        solo.clickOnText("Apply");
        assertFalse(solo.searchText("Requested"));
        assertFalse(solo.searchText("Accepted"));
        assertEquals(0, listView.getCount());

        // Check all
        solo.clickOnView(filterBtn);
        assertTrue(solo.waitForText("Filter Books"));
        solo.clickOnCheckBox(1);
        solo.clickOnCheckBox(2);
        solo.clickOnText("Apply");
        assertTrue(solo.searchText("Requested"));
        assertTrue(solo.searchText("Accepted"));
        assertEquals(3, listView.getCount());
    }

    /**
     * Ensures activity title is "Pending Requests". Tests getActivityTitle().
     */
    @Test
    public void checkTitle() {
        assertEquals("Pending Requests", rule.getActivity().getActivityTitle());
        assertTrue(solo.searchText("Pending Requests"));
    }

    /**
     * Ensures "Owner:" of each book is visible.
     */
    @Test
    public void checkBookOwnerVisibility() {
        View searchBtn =
            rule.getActivity().findViewById(R.id.menu_filter_search_search_btn);
        solo.clickOnView(searchBtn);
        TextInputEditText searchEditText =
            rule.getActivity().findViewById(R.id.search_bar_textInput);
        MockModels.getMockBook1().setDescription("Testing visibility of book" +
            " attributes.");
        solo.enterText(searchEditText, "Testing visibility of book attributes.");
        assertTrue(solo.searchText("Owner:"));
    }

    /**
     * Ensures "Status:" of each book is visible.
     */
    @Test
    public void checkBookStatusVisibility() {
        View searchBtn =
            rule.getActivity().findViewById(R.id.menu_filter_search_search_btn);
        solo.clickOnView(searchBtn);
        TextInputEditText searchEditText =
            (TextInputEditText) solo.getView(R.id.search_bar_textInput);
        MockModels.getMockBook1().setDescription("Testing visibility of book" +
            " attributes.");
        solo.enterText(searchEditText, "Testing visibility of book " +
            "attributes.");
        assertTrue(solo.searchText("Status:"));
    }

    /**
     * Check the count of displayed books. In doing so, tests the
     * getRelevantBooks() method of PendingRequestsActivity and the getBooks()
     * and updateBookList() method of ListingBooksActivity.
     */
    @Test
    public void numberOfBooks() {
        ListView listView = (ListView) solo.getView(R.id.books_listview);
        List<Book> validBooks = new ArrayList<>();
        User user = MockModels.getMockRequester();
        StorageServiceProvider.getStorageService().retrieveBooksByRequester(user,
            books -> {
                for (Book book : books) {
                    if ((book.getStatus() == Book.Status.REQUESTED) ||
                        (book.getStatus() == Book.Status.ACCEPTED)) {
                        validBooks.add(book);
                    }
                }
                assertEquals(validBooks.size(), listView.getCount());
            }, e -> fail("An error occurred while retrieving the books by " +
                "requester.")
        );
    }

    /**
     * Ensures that clicking on a REQUESTED book takes the user to the
     * appropriate activity. In doing so, tests getPackageContext(),
     * goToBookDetails() and getIntentDestination().
     */
    @Test
    public void goToBorrowerBookDetailsForRequested() {
        View searchBtn =
            rule.getActivity().findViewById(R.id.menu_filter_search_search_btn);
        solo.clickOnView(searchBtn);
        TextInputEditText searchEditText =
            (TextInputEditText) solo.getView(R.id.search_bar_textInput);

        MockModels.getMockBook1().setDescription("This book is requested.");
        solo.enterText(searchEditText, "This book is requested.");
        solo.clickInList(0, 0);
        solo.assertCurrentActivity("WRONG ACTIVITY", BorrowerBookDetailsActivity.class);
    }

    /**
     * Ensures that clicking on an ACCEPTED book takes the user to the
     * appropriate activity. In doing so, tests getPackageContext(),
     * goToBookDetails() and getIntentDestination().
     */
    @Test
    public void goToBorrowerBookDetailsForAccepted() {
        View searchBtn =
            rule.getActivity().findViewById(R.id.menu_filter_search_search_btn);
        solo.clickOnView(searchBtn);
        TextInputEditText searchEditText =
            (TextInputEditText) solo.getView(R.id.search_bar_textInput);

        MockModels.getMockBook5().setDescription("This book is accepted.");
        solo.enterText(searchEditText, "This book is accepted.");
        solo.clickInList(0, 0);
        solo.assertCurrentActivity("WRONG ACTIVITY", BorrowerBookDetailsActivity.class);
    }

    /**
     * Ensures that the intent destination is set correctly. Tests
     * getIntentDestination().
     */
    @Test
    public void checkGetIntentDestination() {
        assertEquals(BorrowerBookDetailsActivity.class,
            rule.getActivity().getIntentDestination());
    }

    /**
     * Close activity after each test.
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}


