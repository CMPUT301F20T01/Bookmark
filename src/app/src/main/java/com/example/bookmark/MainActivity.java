package com.example.bookmark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bookmark.models.User;
import com.example.bookmark.server.FirebaseProvider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Starting point of the application.
 * Allows the user to login with their username.
 *
 * @author Konrad Staniszewski
 */
public class MainActivity extends AppCompatActivity {

    FirebaseProvider firebaseProvider = FirebaseProvider.getInstance();

    private EditText userNameEditText;
    private TextInputLayout userNameLayout;
    private Button loginButton;
    private Button signUpButton;

    /**
     * This function creates the main activity view that contains the app's login page
     *
     * @param savedInstanceState an instance state that has the state of the mainactivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fetch relevant views
        userNameEditText = findViewById(R.id.login_username_textInput);
        userNameLayout = findViewById(R.id.login_username_textInputLayout);
        loginButton = findViewById(R.id.login_login_button);
        signUpButton = findViewById(R.id.login_signup_button);

        // pre-fill username if just signed up
        Intent intent = getIntent();
        String signedUpUsername = intent.getStringExtra("SIGNED_UP_USERNAME");
        if (signedUpUsername != null) {
            userNameEditText.setText(signedUpUsername);
        }

        /**
         * Sign in button event listener
         */
        loginButton.setOnClickListener(view -> {
            String username = userNameEditText.getText().toString();
            if (username.length() == 0) {
                userNameLayout.setError("Please enter a username.");
                return;
            }
            // check if username is valid user, if not fire small error notification ect
            firebaseProvider.retrieveUserByUsername(username,
                user -> {
                    if (user == null) {
                        userNameLayout.setError("User not registered!");
                    } else {
                        // store user object in shared preferences
                        SharedPreferences sharedPreferences = getSharedPreferences("LOGGED_IN_USER", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("USER_NAME", user.getUsername());
                        editor.commit();
                        // launch my books activity
                        Intent intent1 = new Intent(getApplicationContext(), MyBooksActivity.class);
                        startActivity(intent1);
                    }
                },
                e -> Toast.makeText(MainActivity.this, "Connection Error. Please Try again.", Toast.LENGTH_LONG).show());
        });

        /**
         * Sign up button event listener
         */
        signUpButton.setOnClickListener(view -> {
            userNameEditText.setText("");
            userNameLayout.setError(null); // hides error message
            Intent intent12 = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent12);
        });
    }
}
