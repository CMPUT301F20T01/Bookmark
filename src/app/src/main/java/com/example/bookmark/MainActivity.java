package com.example.bookmark;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.bookmark.fragments.SignupDialogFragment;
import com.example.bookmark.models.User;

/**
 * Starting point of the application.
 * Allows the user to login with their username.
 * @author Konrad Staniszewski
 */
public class MainActivity extends AppCompatActivity implements SignupDialogFragment.OnFragmentInteractionListener {

    private EditText userNameEditText;
    private Button signInButton;
    private Button signUpButton;

    /**
     * This function creates the main activity view that contains the app's login page
     * @param savedInstanceState an instance state that has the state of the mainactivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fetch relevant views
        userNameEditText = findViewById(R.id.login_username_edit_text);
        signInButton = findViewById(R.id.login_sign_in_button);
        signUpButton = findViewById(R.id.login_sign_up_button);

        /**
         * Sign in button event listener
         */
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = userNameEditText.getText().toString();
                // check if username is valid user, if not fire small error notification ect
                // if it is valid, store username and user object into a shared preference
                // fire off the "my books" activity
            }
        });

        /**
         * Sign up button event listener
         */
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SignupDialogFragment().show(getSupportFragmentManager(), "SIGN_UP");
            }
        });
    }

    /**
     * Handles the logic for when signup is pressed in the SignupDialog
     * @param  user: user object that was just added to the system
     */
    @Override
    public void onSignUpPressed(User user) {
        // add user to database

        userNameEditText = findViewById(R.id.login_username_edit_text);
        userNameEditText.setText(user.getUsername());
    }

}
