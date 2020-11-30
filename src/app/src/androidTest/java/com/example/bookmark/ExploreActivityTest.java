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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * Perform intent testing on ExploreActivity.
 *
 * @author Ryan Kortbeek.
 */
@RunWith(AndroidJUnit4.class)
public class ExploreActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<ExploreActivity> rule =
        new ActivityTestRule<>(ExploreActivity.class, true, false);

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
        solo.assertCurrentActivity("WRONG ACTIVITY", ExploreActivity.class);
    }

    /**
     * Tests search functionality.
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
            rule.getActivity().findViewById(R.id.visible_books_listview);
        MockModels.getMockBook1().setDescription("Learning to code!");
        MockModels.getMockBook2().setDescription("LEARNT BEHAVIOUR...");
        MockModels.getMockBook3().setDescription("Trying to LeArN.");
        solo.enterText(searchEditText, "lEaRn");
        assertTrue(solo.searchText("Code Complete 2"));
        assertTrue(solo.searchText("Programming Pearls"));
        assertTrue(solo.searchText("Unedited Title"));
        assertEquals(3, listView.getCount());
        solo.enterText(searchEditText, " behaviour...");
        assertTrue(solo.searchText("Programming Pearls"));
        assertEquals(1, listView.getCount());
    }

    /**
     * Ensures activity title is "Explore". Tests getActivityTitle().
     */
    @Test
    public void checkTitle() {
        assertEquals(rule.getActivity().getActivityTitle(), "Explore");
        assertTrue(solo.searchText("Explore"));
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
     * getRelevantBooks() method of ExploreActivity and the getBooks() method
     * of ListingBooksActivity.
     */
    @Test
    public void numberOfBooks() {
        ListView listView =
            rule.getActivity().findViewById(R.id.visible_books_listview);
        List<Book> validBooks = new ArrayList<>();
        User user = MockModels.getMockRequester();
        StorageServiceProvider.getStorageService().retrieveBooks(books -> {
                for (Book book : books) {
                    if (!book.getOwnerId().equals(user.getId()) &&
                        (book.getStatus() != Book.Status.BORROWED) &&
                        (book.getStatus() != Book.Status.ACCEPTED)) {
                        validBooks.add(book);
                    }
                }
                assertEquals(validBooks.size(), listView.getCount());
            }, e -> fail("An error occurred while retrieving the books by " +
                "requester.")
        );
    }

    /**
     * Ensures that clicking on a book takes the user to the appropriate
     * activity. In doing so, tests getPackageContext() and
     * getIntentDestination().
     */
    @Test
    public void goToBookDetails() {
        solo.clickInList(0, 0);
        solo.assertCurrentActivity("WRONG ACTIVITY", ExploreBookDetailsActivity.class);
    }

    /**
     * Ensures that the intent destination is set correctly. Tests
     * getIntentDestination().
     */
    @Test
    public void checkGetIntentDestination() {
        assertEquals(ExploreBookDetailsActivity.class,
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

