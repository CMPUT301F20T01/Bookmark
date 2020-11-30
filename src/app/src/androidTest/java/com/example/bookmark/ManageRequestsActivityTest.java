package com.example.bookmark;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bookmark.mocks.MockModels;
import com.example.bookmark.mocks.MockStorageService;
import com.example.bookmark.models.Book;
import com.example.bookmark.models.User;
import com.example.bookmark.server.StorageServiceProvider;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Perform intent testing on the ManageRequestsActivity
 *
 * @author Nayan Prakash
 */
public class ManageRequestsActivityTest {
    private Solo solo;
    private Book book;
    private User owner;

    @Rule
    public ActivityTestRule<ManageRequestsActivity> rule =
        new ActivityTestRule<ManageRequestsActivity>(ManageRequestsActivity.class, true, true) {
            @Override
            protected Intent getActivityIntent() {
                book = MockModels.getMockBook1();
                owner = MockModels.getMockOwner();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("Book", book);
                bundle.putSerializable("User", owner);
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
        solo.assertCurrentActivity("WRONG ACTIVITY", ManageRequestsActivity.class);
    }

    /**
     * Test accepting a request
     */
    @Test
    public void acceptRequest() {
        ListView requestList = rule.getActivity().findViewById(R.id.request_list);
        View view = requestList.getAdapter().getView(0, null, null);
        solo.clickOnView(view.findViewById(R.id.accept_button));
        solo.waitForActivity(AcceptRequestsActivity.class);
        solo.assertCurrentActivity("WRONG ACTIVITY", AcceptRequestsActivity.class);
    }

    /**
     * Test rejecting a request
     */
    @Test
    public void rejectRequest() {
        ListView requestList = rule.getActivity().findViewById(R.id.request_list);
        assertTrue(solo.searchText("mary.jane9"));
        View view = requestList.getAdapter().getView(0, null, null);
        solo.clickOnView(view.findViewById(R.id.reject_button));
        solo.waitForActivity(ManageRequestsActivity.class);
        solo.assertCurrentActivity("WRONG ACTIVITY", ManageRequestsActivity.class);
        assertFalse(solo.searchText("mary.jane9"));
    }
}
