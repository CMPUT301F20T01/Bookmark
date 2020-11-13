package com.example.bookmark;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.bookmark.server.StorageServiceProvider;
import com.example.bookmark.util.DialogUtil;

/**
 * This activity shows a user all of their profile details.
 * It also allows them to access a page where they can edit all of their details
 * by going to the edit profile activity where they can modify their details
 *
 * @author Konrad Staniszewski
 */
public class MyProfileActivity extends NavigationDrawerActivity
    implements MenuOptions {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        getSupportActionBar().setTitle("My Profile");

        SharedPreferences sharedPref = this.getSharedPreferences("LOGGED_IN_USER", Context.MODE_PRIVATE);
        String username = sharedPref.getString("USER_NAME", "ERROR_NO_USER");
        if (username.equals("ERROR_NO_USER")) {
            DialogUtil.showErrorDialog(this, new Exception(username));
        } else {
            populateUserInto(username);
        }
    }

    private void populateUserInto(String username) {
        StorageServiceProvider.getStorageService().retrieveUserByUsername(username, user -> {
            ((TextView) findViewById(R.id.my_profile_username_textView)).setText(user.getUsername());
            ((TextView) findViewById(R.id.my_profile_firstName_lastName_textView))
                .setText("Name: " + user.getFirstName() + " " + user.getLastName());
            ((TextView) findViewById(R.id.my_profile_emailAddress_textView))
                .setText("Email: " + user.getEmailAddress());
            ((TextView) findViewById(R.id.my_profile_phoneNumber_textView))
                .setText("Phone: " + user.getPhoneNumber());
        }, e -> DialogUtil.showErrorDialog(this, e));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    private void goToEditProfile() {
        Intent intent = new Intent(MyProfileActivity.this, EditProfileActivity.class);
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_edit_edit_btn) {
            goToEditProfile();
        }
        return (super.onOptionsItemSelected(item));
    }
}
