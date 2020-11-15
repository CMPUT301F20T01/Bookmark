package com.example.bookmark;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.bookmark.server.StorageServiceProvider;
import com.example.bookmark.util.DialogUtil;

/**
 * This activity shows the details of another user
 *
 * @author Konrad Staniszewski
 */
public class ViewProfileActivity extends BackButtonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        // retrieve username from intent
        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");
        populateUserInfo(username);
    }

    private void populateUserInfo(String username) {
        StorageServiceProvider.getStorageService().retrieveUserByUsername(username, user -> {
            ((TextView) findViewById(R.id.view_profile_username_textView)).setText(user.getUsername());
            ((TextView) findViewById(R.id.view_profile_firstName_lastName_textView))
                .setText("Name: " + user.getFirstName() + " " + user.getLastName());
            ((TextView) findViewById(R.id.view_profile_emailAddress_textView))
                .setText("Email: " + user.getEmailAddress());
            ((TextView) findViewById(R.id.view_profile_phoneNumber_textView))
                .setText("Phone: " + user.getPhoneNumber());
        }, e -> DialogUtil.showErrorDialog(this, e));
    }
}
