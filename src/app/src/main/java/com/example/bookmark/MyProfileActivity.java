package com.example.bookmark;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bookmark.models.MenuOptions;
import com.example.bookmark.server.FirebaseProvider;
import com.example.bookmark.util.DialogUtil;
import com.mikepenz.materialdrawer.Drawer;

/**
 * This activity shows a user all of their profile details.
 * It also allows them to access a page where they can edit all of their details
 * by going to the edit profile activity where they can modify their details
 *
 * @author Konrad Staniszewski
 */
public class MyProfileActivity extends AppCompatActivity implements MenuOptions {

    private Drawer navigationDrawer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Profile");
        navigationDrawer = DrawerProvider.getDrawer(this, toolbar);

        SharedPreferences sharedPref = this.getSharedPreferences("LOGGED_IN_USER", Context.MODE_PRIVATE);
        String username = sharedPref.getString("USER_NAME", "ERROR_NO_USER");

        FirebaseProvider.getInstance().retrieveUserByUsername(username, user -> {
            ((TextView) findViewById(R.id.username_textView)).setText(user.getUsername());
            ((TextView) findViewById(R.id.firstName_textView)).setText(user.getFirstName());
            ((TextView) findViewById(R.id.lastName_textView)).setText(user.getLastName());
            ((TextView) findViewById(R.id.emailAddress_textView)).setText(user.getEmailAddress());
            ((TextView) findViewById(R.id.phoneNumber_textView)).setText(user.getPhoneNumber());
        }, e -> DialogUtil.showErrorDialog(this, e));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (navigationDrawer != null && navigationDrawer.isDrawerOpen()) {
            navigationDrawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private void goToEditProfile() {
        Intent intent = new Intent(MyProfileActivity.this, EditProfileActivity.class);
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_filter_search_search_btn) {
            goToEditProfile();
        }
        return (super.onOptionsItemSelected(item));
    }
}
