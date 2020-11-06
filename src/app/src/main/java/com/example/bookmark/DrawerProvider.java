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
                    String itemName= ((Nameable) drawerItem).getName().getText(activity);
                    Toast.makeText(activity, itemName, Toast.LENGTH_SHORT).show();
                    if (drawerItem != null) {
                        Intent intent = null;
                        switch (itemName) {
                            case "My Books":
                                if (!(activity instanceof MyBooksActivity)) {
                                    intent = new Intent(activity, MyBooksActivity.class);
                                }
                            case "Borrowed":
                                if (!(activity instanceof BorrowedActivity)) {
                                    intent = new Intent(activity, BorrowedActivity.class);
                                }
//                            case "Pending Requests":
//                                if (!(activity instanceof PendingRequestsActivity)) {
//                                    intent = new Intent(activity, PendingRequestsActivity.class);
//                                }
//                            case "Explore":
//                                if (!(activity instanceof ExploreActivity)) {
//                                    intent = new Intent(activity, ExploreActivity.class);
//                                }
                            case "Profile":
                                if (!(activity instanceof MyProfileActivity)) {
                                    intent = new Intent(activity, MyProfileActivity.class);
                                }
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
