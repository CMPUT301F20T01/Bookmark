package com.example.bookmark;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * Starting point of the application.
 * Allows the user to login with their username.
 * @author Konrad Staniszewski
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
