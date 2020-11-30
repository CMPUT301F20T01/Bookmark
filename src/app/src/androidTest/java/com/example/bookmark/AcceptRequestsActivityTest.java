package com.example.bookmark;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bookmark.mocks.MockStorageService;
import com.example.bookmark.server.StorageServiceProvider;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static android.content.Context.MODE_PRIVATE;
import static org.junit.Assert.assertTrue;

/**
 * Perform intent testing on the AcceptRequestsActivity
 *
 * @author Nayan Prakash
 */
public class AcceptRequestsActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<AcceptRequestsActivity> rule =
        new ActivityTestRule<AcceptRequestsActivity>(AcceptRequestsActivity.class, true, true) {
            @Override
            protected Intent getActivityIntent() {
                Intent intent = new Intent();
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
        solo.assertCurrentActivity("WRONG ACTIVITY", AcceptRequestsActivity.class);
    }

    /**
     * Test clicking done button after setting marker
     */
    @Test
    public void clickDoneButton() {
        Activity activity = solo.getCurrentActivity();
        View buttonView = rule.getActivity().findViewById(R.id.done_button);
        solo.clickOnView(buttonView);
        solo.waitForLogMessage("Finishing Accept Requests Activity on done button press");
    }
}
