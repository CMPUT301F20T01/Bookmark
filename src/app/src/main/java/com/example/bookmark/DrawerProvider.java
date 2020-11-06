package com.example.bookmark;


import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

public class DrawerProvider {
    public static Drawer getDrawer(final Activity activity, Toolbar toolbar) {
        Drawer result = new DrawerBuilder()
            .withActivity(activity)
            .withToolbar(toolbar)
            .inflateMenu(R.menu.drawer_menu)
            .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
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
            }).build();
        return result;
    }
}
