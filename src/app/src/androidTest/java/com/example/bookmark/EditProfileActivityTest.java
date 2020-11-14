package com.example.bookmark;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

import static android.content.Context.MODE_PRIVATE;


/**
 * Perform intent testing on EditProfileActivity
 * <p>
 * Outstanding Issues/TODOs
 * Test that user details are actually updated?
 * @author Konrad Staniszewski.
 */
public class EditProfileActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<EditProfileActivity> rule =
        new ActivityTestRule<>(EditProfileActivity.class, true, false);

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
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("LOGGED_IN_USER", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USER_NAME", "john.smith42").commit();
        rule.launchActivity(new Intent());

        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        solo.assertCurrentActivity("WRONG ACTIVITY", EditProfileActivity.class);
    }

    /**
     * Test clicking done button with no changes
     */
    @Test
    public void doneEditing() {
        View doneEditingBtn = rule.getActivity().findViewById(R.id.edit_profile_done_button);
        solo.clickOnView(doneEditingBtn);
        solo.assertCurrentActivity("WRONG ACTIVITY", MyProfileActivity.class);
    }

    /**
     * Test making an illegal email
     */
    @Test
    public void testEmailValidation() {
        View doneEditingBtn = rule.getActivity().findViewById(R.id.edit_profile_done_button);
        EditText emailEditText = rule.getActivity().findViewById(R.id.edit_profile_email_edit_text);
        solo.clearEditText(emailEditText);
        solo.clickOnView(doneEditingBtn);
        solo.assertCurrentActivity("WRONG ACTIVITY", EditProfileActivity.class);
    }

    /**
     * Close the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
