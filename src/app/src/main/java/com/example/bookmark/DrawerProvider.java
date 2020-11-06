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
        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem drawerEmptyItem= new PrimaryDrawerItem().withIdentifier(0).withName("");
        drawerEmptyItem.withEnabled(false);

        PrimaryDrawerItem drawerItem1 = new PrimaryDrawerItem().withIdentifier(1).withName("TEST").withIcon(R.drawable.ic_bookmark);
        PrimaryDrawerItem drawerItem2 = new PrimaryDrawerItem().withIdentifier(2).withName("TEST").withIcon(R.drawable.ic_bookmark);

        //create the drawer and remember the `Drawer` result object
//        Drawer result = new DrawerBuilder()
//            .withActivity(activity)
//            .withToolbar(toolbar)
//            .withActionBarDrawerToggle(true)
//            .withActionBarDrawerToggleAnimated(true)
//            .withCloseOnClick(true)
//            .withSelectedItem(-1)
//            .addDrawerItems(
//                drawerEmptyItem,drawerEmptyItem,drawerEmptyItem,
//                drawerItem1,
//                drawerItem2
//            )
//            .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//                @Override
//                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//                    if (drawerItem.getIdentifier() == 2 && !(activity instanceof TestActivity2)) {
//                        // load tournament screen
//                        Intent intent = new Intent(activity, TestActivity1.class);
//                        view.getContext().startActivity(intent);
//                    }
//                    return true;
//                }
//            })
//            .build();


        Drawer result = new DrawerBuilder()
            .withActivity(activity)
            .withToolbar(toolbar)
            .inflateMenu(R.menu.drawer_menu)
            .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                    if (drawerItem instanceof Nameable) {
                        Toast.makeText(activity, ((Nameable) drawerItem).getName().getText(activity), Toast.LENGTH_SHORT).show();
                    }

                    return false;
                }
            }).build();
        return result;
    }
}
