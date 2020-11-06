package com.example.bookmark;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This activity implements the backwards navigation functionality used by
 * activities with a back button.
 * TODO: verify back button functionality
 *
 * @author Ryan Kortbeek.
 */
public class BackButtonActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = getIntent();
        setResult(AppCompatActivity.RESULT_CANCELED, intent);
        finish();
        return true;
    }
}
