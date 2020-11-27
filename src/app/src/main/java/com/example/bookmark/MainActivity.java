package com.example.bookmark;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookmark.server.StorageServiceProvider;
import com.example.bookmark.util.DialogUtil;
import com.example.bookmark.util.EmptyTextFocusListener;
import com.example.bookmark.util.UserUtil;
import com.google.android.material.textfield.TextInputLayout;

import static com.example.bookmark.util.UserInfoFormValidator.validateEditTextEmpty;

/**
 * Starting point of the application.
 * Allows the user to login with their username.
 *
 * @author Konrad Staniszewski
 */
public class MainActivity extends AppCompatActivity {

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

        // make sure shared preferences have user logged out
        logOut();

        // Fetch relevant views
        userNameEditText = findViewById(R.id.login_username_textInput);
        userNameLayout = findViewById(R.id.login_username_textInputLayout);
        loginButton = findViewById(R.id.login_login_button);
        signUpButton = findViewById(R.id.login_signup_button);

        loginButton.setOnClickListener(v -> logIn());
        signUpButton.setOnClickListener(v -> signUp());
        userNameEditText.setOnFocusChangeListener(new EmptyTextFocusListener(userNameEditText, userNameLayout));

        // pre-fill username if just signed up
        Intent intent = getIntent();
        String signedUpUsername = intent.getStringExtra("SIGNED_UP_USERNAME");
        if (signedUpUsername != null) {
            userNameEditText.setText(signedUpUsername);
        }

        /**
         * username EditText textChangeListener
         */
        userNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                userNameLayout.setError(null);
            }
        });
    }

    /**
     * Sign in button event listener function
     */
    private void logIn() {
        if (!validateEditTextEmpty(userNameEditText, userNameLayout)) {
            return;
        }

        // check if username is valid user, if not fire small error notification ect
        String username = userNameEditText.getText().toString();
        StorageServiceProvider.getStorageService().retrieveUserByUsername(username,
            user -> {
                if (user == null) {
                    userNameLayout.setError("User not registered!");
                } else {
                    // store user object in shared preferences
                    UserUtil.setLoggedInUser(this, user.getUsername());
                    // launch my books activity
                    Intent intent = new Intent(getApplicationContext(), MyBooksActivity.class);
                    startActivity(intent);
                }
            },
            e -> DialogUtil.showErrorDialog(MainActivity.this,
                new Exception("Connection Error. Please Try again."))
        );
    }

    /**
     * Sign up button event listener function
     */
    private void signUp() {
        userNameEditText.setText("");
        userNameLayout.setError(null); // hides error message
        Intent signUpIntent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(signUpIntent);
    }

    /**
     * Make sure any logged in user is signed out before continuing
     */
    private void logOut() {
        SharedPreferences sharedPreferences = getSharedPreferences("LOGGED_IN_USER", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().commit();
    }
}
