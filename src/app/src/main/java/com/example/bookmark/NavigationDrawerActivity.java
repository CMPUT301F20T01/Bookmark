package com.example.bookmark;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

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
}
