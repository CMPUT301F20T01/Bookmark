package com.example.bookmark;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bookmark.mocks.MockModels;
import com.example.bookmark.mocks.MockStorageService;
import com.example.bookmark.models.Book;
import com.example.bookmark.server.StorageServiceProvider;
import com.google.android.material.textfield.TextInputEditText;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


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
     * Tests search functionality.
     */
    // @Test
    // public void search() {
    //     View searchBtn =
    //         rule.getActivity().findViewById(R.id.menu_filter_search_search_btn);
    //     TextInputLayout searchBarLayout =
    //         rule.getActivity().findViewById(R.id.search_bar_textInputLayout);
    //     assertEquals(View.GONE, searchBarLayout.getVisibility());
    //     solo.clickOnView(searchBtn);
    //     solo.waitForText("Search");
    //     assertEquals(View.VISIBLE, searchBarLayout.getVisibility());
    //     TextInputEditText searchEditText =
    //         rule.getActivity().findViewById(R.id.search_bar_textInput);
    //     ListView listView =
    //         rule.getActivity().findViewById(R.id.visible_books_listview);
    //     Book mock1 = MockModels.getMockBook1();
    //     mock1.setStatus(Book.Status.REQUESTED);
    //     mock1.setDescription("Learning to code!");
    //     Book mock2 = MockModels.getMockBook2();
    //     mock2.setStatus(Book.Status.REQUESTED);
    //     mock2.setDescription("LEARNT BEHAVIOUR...");
    //     // TODO call listView.getAdapter().notifyDataSetChanged() here
    //     solo.enterText(searchEditText, "lEaRn");
    //     assertTrue(solo.searchText("Code Complete 2"));
    //     assertTrue(solo.searchText("Programming Pearls"));
    //     assertEquals(2, listView.getCount());
    //     solo.enterText(searchEditText, " behaviour...");
    //     assertTrue(solo.searchText("Programming Pearls"));
    //     assertEquals(1, listView.getCount());
    // }

    /**
     * Ensures activity title is "Pending Requests". Tests getActivityTitle().
     */
    @Test
    public void checkTitle() {
        assertEquals("Pending Requests", rule.getActivity().getActivityTitle());
        assertTrue(solo.searchText("Pending Requests"));
    }

    /**
     * Ensures "Owner:" of each book is visible. Tests getBookOwnerVisibility().
     */
    @Test
    public void checkBookOwnerVisibility() {
        assertTrue(rule.getActivity().getBookOwnerVisibility());
        View searchBtn =
            rule.getActivity().findViewById(R.id.menu_filter_search_search_btn);
        solo.clickOnView(searchBtn);
        TextInputEditText searchEditText =
            rule.getActivity().findViewById(R.id.search_bar_textInput);
        Book mock1 = MockModels.getMockBook1();
        mock1.setDescription("Testing visibility of book attributes.");
        solo.enterText(searchEditText, "Testing visibility of book attributes.");
        assertTrue(solo.searchText("Owner:"));
    }

    /**
     * Ensures "Status:" of each book is visible. Tests
     * getBookStatusVisibility().
     */
    @Test
    public void checkBookStatusVisibility() {
        assertTrue(rule.getActivity().getBookStatusVisibility());
        View searchBtn =
            rule.getActivity().findViewById(R.id.menu_filter_search_search_btn);
        solo.clickOnView(searchBtn);
        TextInputEditText searchEditText =
            (TextInputEditText) solo.getView(R.id.search_bar_textInput);
        Book mock1 = MockModels.getMockBook1();
        mock1.setDescription("Testing visibility of book attributes.");
        solo.enterText(searchEditText, "Testing visibility of book attributes.");
        assertTrue(solo.searchText("Status:"));
    }

    /**
     * Check the count of displayed books. In doing so, tests the
     * getRelevantBooks() method of PendingRequestsActivity and the getBooks()
     * method of ListingBooksActivity.
     */
    // @Test
    // public void numberOfBooks() {
    //     ListView listView = (ListView) solo.getView(R.id.visible_books_listview);
    //     List<Book> validBooks = new ArrayList<>();
    //     MockModels.getMockBook1().setStatus(Book.Status.ACCEPTED);
    //     MockModels.getMockBook2().setStatus(Book.Status.REQUESTED);
    //     // TODO call listView.getAdapter().notifyDataSetChanged() here
    //     User user = MockModels.getMockRequester();
    //     StorageServiceProvider.getStorageService().retrieveBooksByRequester(user,
    //         books -> {
    //             for (Book book : books) {
    //                 if ((book.getStatus() == Book.Status.REQUESTED) ||
    //                     (book.getStatus() == Book.Status.ACCEPTED)) {
    //                     validBooks.add(book);
    //                 }
    //             }
    //             assertEquals(validBooks.size(), listView.getCount());
    //         }, e -> fail("An error occurred while retrieving the books by " +
    //             "requester.")
    //     );
    // }

    /**
     * Ensures that clicking on a REQUESTED book takes the user to the
     * appropriate activity. In doing so, tests getPackageContext() and
     * getIntentDestination().
     */
    @Test
    public void goToRequestedBookDetails() {
        View searchBtn =
            rule.getActivity().findViewById(R.id.menu_filter_search_search_btn);
        solo.clickOnView(searchBtn);
        TextInputEditText searchEditText =
            (TextInputEditText) solo.getView(R.id.search_bar_textInput);
        Book mock1 = MockModels.getMockBook1();
        mock1.setDescription("This book is requested.");
        mock1.setStatus(Book.Status.REQUESTED);
        solo.enterText(searchEditText, "This book is requested.");
        solo.clickInList(0, 0);
        solo.assertCurrentActivity("WRONG ACTIVITY", RequestedBookDetailsActivity.class);
    }

    /**
     * Ensures that clicking on a ACCEPTED book takes the user to the
     * appropriate activity. In doing so, tests getPackageContext() and
     * getIntentDestination().
     */
    // @Test
    // public void goToAcceptedBookDetails() {
    //     View searchBtn =
    //         rule.getActivity().findViewById(R.id.menu_filter_search_search_btn);
    //     solo.clickOnView(searchBtn);
    //     TextInputEditText searchEditText =
    //         (TextInputEditText) solo.getView(R.id.search_bar_textInput);
    //     Book mock1 = MockModels.getMockBook1();
    //     mock1.setDescription("This book is accepted.");
    //     mock1.setStatus(Book.Status.ACCEPTED);
    //     // TODO call listView.getAdapter().notifyDataSetChanged() here
    //     solo.enterText(searchEditText, "This book is accepted.");
    //     solo.clickInList(0, 0);
    //     solo.assertCurrentActivity("WRONG ACTIVITY", AcceptedBookDetailsActivity.class);
    // }

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


