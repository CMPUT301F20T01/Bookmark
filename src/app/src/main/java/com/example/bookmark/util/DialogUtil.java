package com.example.bookmark.util;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

/**
 * @author Kyle Hennig
 */
public class DialogUtil {
    public static void showErrorDialog(Context context, Exception e) {
        new AlertDialog.Builder(context)
            .setTitle("Error")
            .setMessage(String.format("An error occurred: %s", e))
            .setPositiveButton(android.R.string.ok, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }
}
