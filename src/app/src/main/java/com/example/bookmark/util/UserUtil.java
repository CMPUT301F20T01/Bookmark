package com.example.bookmark.util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class UserUtil {
    public static String getLoggedInUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LOGGED_IN_USER", MODE_PRIVATE);
        String username = sharedPreferences.getString("USER_NAME", "ERROR_NO_USER");
        if (username.equals("ERROR_NO_USER")) {
            DialogUtil.showErrorDialog(context, new Exception(username));
            return null;
        } else {
            return username;
        }
    }

    public static void setLoggedInUser(Context context, String username) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LOGGED_IN_USER", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USER_NAME", username);
        editor.commit();
    }
}
