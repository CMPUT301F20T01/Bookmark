package com.example.bookmark.util;


import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.example.bookmark.BorrowedActivity;
import com.example.bookmark.ExploreActivity;
import com.example.bookmark.MyBooksActivity;
import com.example.bookmark.MyProfileActivity;
import com.example.bookmark.PendingRequestsActivity;
import com.example.bookmark.R;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


/**
 * This class implement a navigation drawer that is passed to
 */
public class DrawerProvider {
    public static Drawer getDrawer(final Activity activity, Toolbar toolbar) {
        DrawerBuilder drawerBuilder = new DrawerBuilder();
        drawerBuilder.withActivity(activity);
        drawerBuilder.withToolbar(toolbar);
        drawerBuilder.inflateMenu(R.menu.drawer_menu);
        drawerBuilder.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                if (drawerItem != null) {
                    Intent intent = null;
                    if (position == 0 && !(activity instanceof MyBooksActivity)) {
                        intent = new Intent(activity, MyBooksActivity.class);
                    } else if (position == 1 && !(activity instanceof BorrowedActivity)) {
                        intent = new Intent(activity, BorrowedActivity.class);
                    } else if (position == 2 && !(activity instanceof PendingRequestsActivity)) {
                        intent = new Intent(activity, PendingRequestsActivity.class);
                    } else if (position == 3 && !(activity instanceof ExploreActivity)) {
                        intent = new Intent(activity, ExploreActivity.class);
                    } else if (position == 4 && !(activity instanceof MyProfileActivity)) {
                        intent = new Intent(activity, MyProfileActivity.class);
                    }
                    if (intent != null) {
                        activity.startActivity(intent);
                    }
                }
                return false;
            }
        });
        Drawer result = drawerBuilder.build();
        return result;
    }
}
