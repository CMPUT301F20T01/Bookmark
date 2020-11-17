package com.example.bookmark;

import android.content.Intent;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.bookmark.mocks.MockStorageService;
import com.example.bookmark.server.StorageServiceProvider;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;


/**
 * Perform intent testing on ViewProfileActivity
 * <p>
 * Outstanding Issues/TODOs
 *
 * @author Konrad Staniszewski.
 */
public class ViewProfileActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<ViewProfileActivity> rule =
        new ActivityTestRule<>(ViewProfileActivity.class, true, false);


    @BeforeClass
    public static void setUpOnce() {
        StorageServiceProvider.setStorageService(MockStorageService.getMockStorageService());
    }

    /**
     * Runs before all tests and create the solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        Intent intent = new Intent();
        intent.putExtra("USERNAME", "mary.jane9");
        rule.launchActivity(intent);

        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        solo.assertCurrentActivity("WRONG ACTIVITY", ViewProfileActivity.class);
    }


    /**
     * Close the activity after each test
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
