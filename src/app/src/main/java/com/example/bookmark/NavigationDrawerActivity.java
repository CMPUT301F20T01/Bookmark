package com.example.bookmark;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.bookmark.util.DialogUtil;
import com.example.bookmark.util.DrawerProvider;
import com.mikepenz.materialdrawer.Drawer;

/**
 * This activity implements the navigation drawer functionality
 * used by activities with a navigation drawer
 *
 * @author Konrad Staniszewski
 */
public class NavigationDrawerActivity extends AppCompatActivity {

    protected ConstraintLayout fullLayout;
    protected FrameLayout contentLayout;
    private Drawer navigationDrawer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.navigationActivity);
    }

    @Override
    public void setContentView(final int layoutResId) {
        fullLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_navigation_drawer, null);
        contentLayout = (FrameLayout) fullLayout.findViewById(R.id.layout_activity_container);

        getLayoutInflater().inflate(layoutResId, contentLayout, true);
        super.setContentView(fullLayout);

        // setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.global_toolbar);
        setSupportActionBar(toolbar);
        navigationDrawer = DrawerProvider.getDrawer(this, toolbar);
        // set selected item
        Intent intent = getIntent();
        long identifier = intent.getLongExtra("SELECTED_IDENTIFIER", -1);
        navigationDrawer.setSelection(identifier);
        // set drawer username text
        View header = navigationDrawer.getHeader();
        TextView usernameTextView = header.findViewById(R.id.navigation_header_username);
        String username = getLoggedUsername();
        usernameTextView.setText(username);
    }

    @Override
    public void onBackPressed() {
        // close the drawer first and if the drawer is closed close the activity
        if (navigationDrawer != null && navigationDrawer.isDrawerOpen()) {
            navigationDrawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private String getLoggedUsername() {
        SharedPreferences sharedPref = this.getSharedPreferences("LOGGED_IN_USER", Context.MODE_PRIVATE);
        String username = sharedPref.getString("USER_NAME", "ERROR_NO_USER");
        if (username.equals("ERROR_NO_USER")) {
            DialogUtil.showErrorDialog(this, new Exception(username));
        } else {
            return username;
        }
        return username;
    }
}
