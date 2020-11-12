package com.example.bookmark;

import android.app.Activity;
import android.view.View;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


/**
 * Perform intent testing on MyProfileActivity
 * <p>
 * Outstanding Issues/TODOs
 *
 * @author Konrad Staniszewski.
 */
public class MyProfileActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MyProfileActivity> rule =
        new ActivityTestRule<>(MyProfileActivity.class, true, true);

    /**
     * Runs before all tests and create the solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Test clicking the edit profile button
     */
    @Test
    public void editProfile() {
        View editProfileBtn = rule.getActivity().findViewById(R.id.menu_edit_edit_btn);
        solo.clickOnView(editProfileBtn);
        solo.assertCurrentActivity("WRONG ACTIVITY", EditProfileActivity.class);
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
