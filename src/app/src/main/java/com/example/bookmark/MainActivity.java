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

import com.example.bookmark.fragments.SignupDialogFragment;
import com.example.bookmark.models.User;
import com.example.bookmark.server.FirebaseProvider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * Starting point of the application.
 * Allows the user to login with their username.
 * @author Konrad Staniszewski
 */
public class MainActivity extends AppCompatActivity implements SignupDialogFragment.OnFragmentInteractionListener {

    FirebaseProvider firebaseProvider = FirebaseProvider.getInstance();

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
                String username = userNameEditText.getText().toString();
                if (username.length() == 0) {
                    userNameEditText.setError("Please enter a username.");
                    return;
                }
                // check if username is valid user, if not fire small error notification ect
                firebaseProvider.getUserByUsername(username,
                    new OnSuccessListener<User>() {
                        @Override
                        public void onSuccess(User user) {
                            if (user == null) {
                                userNameEditText.setError("Username not registered!\nPlease enter existing username.");
                            }
                            else {
                                // store user object in shared preferences
                                SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("USER_NAME", user.getFirstName());
                                editor.commit();
                                // launch my books activity
                                Intent intent = new Intent(getApplicationContext(), MyBooksActivity.class);
                                startActivity(intent);
                            }
                        }
                    },
                    new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            userNameEditText.setError("Connection Error. Please Try again.");
                        }
                    });
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
        firebaseProvider.saveUser(user,
            new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    ;
                }
            },
            new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplication().getBaseContext(), "Connection Error. Please Try again.", Toast.LENGTH_LONG).show();
                }
            });

        // fill out field for easier sign in
        userNameEditText = findViewById(R.id.login_username_edit_text);
        userNameEditText.setText(user.getUsername());
    }
}
