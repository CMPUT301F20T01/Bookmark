package com.example.bookmark;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;


import com.example.bookmark.models.User;
import com.example.bookmark.server.FirebaseProvider;
import com.example.bookmark.util.DialogUtil;
import com.example.bookmark.util.EmptyTextFocusListener;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import static com.example.bookmark.util.UserInfoFormValidator.checkIfEditTextValidEmail;
import static com.example.bookmark.util.UserInfoFormValidator.checkIfEditTextValidPhoneNumber;
import static com.example.bookmark.util.UserInfoFormValidator.validateEditTextEmpty;

public class SignUpActivity extends BackButtonActivity {
    private EditText userNameEditText, firstNameEditText, lastNameEditText, emailAddressEditText, phoneNumberEditText;
    private TextInputLayout userNameLayout, firstNameLayout, lastNameLayout, emailAddressLayout, phoneNumberLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setTitle("Sign Up");

        // get relevant views
        userNameEditText = findViewById(R.id.signup_username_edit_text);
        firstNameEditText = findViewById(R.id.signup_firstname_edit_text);
        lastNameEditText = findViewById(R.id.signup_lastname_edit_text);
        emailAddressEditText = findViewById(R.id.signup_email_edit_text);
        phoneNumberEditText = findViewById(R.id.signup_phonenumber_edit_text);
        Button signUpButton = findViewById(R.id.signup_signup_button);

        userNameLayout = findViewById(R.id.signup_username_textInputLayout);
        firstNameLayout = findViewById(R.id.signup_firstname_textInputLayout);
        lastNameLayout = findViewById(R.id.signup_lastname_textInputLayout);
        emailAddressLayout = findViewById(R.id.signup_email_textInputLayout);
        phoneNumberLayout = findViewById(R.id.signup_phonenumber_textInputLayout);

        signUpButton.setOnClickListener(v -> validateAndAddUser());

        // allows user to see if their email address is valid as they type
        phoneNumberEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkIfEditTextValidPhoneNumber(phoneNumberEditText, phoneNumberLayout);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        // allows user to see if their phone number is valid as they type
        emailAddressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkIfEditTextValidEmail(emailAddressEditText, emailAddressLayout);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // focus change listeners
        userNameEditText.setOnFocusChangeListener(new EmptyTextFocusListener(userNameEditText, userNameLayout));
        firstNameEditText.setOnFocusChangeListener(new EmptyTextFocusListener(firstNameEditText, firstNameLayout));
        lastNameEditText.setOnFocusChangeListener(new EmptyTextFocusListener(lastNameEditText, lastNameLayout));

        emailAddressEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                emailAddressLayout.setError(null);
            } else {
                if (emailAddressEditText.getText().toString().equals("")) {
                    validateEditTextEmpty(emailAddressEditText, emailAddressLayout);
                } else {
                    checkIfEditTextValidEmail(emailAddressEditText, emailAddressLayout);
                }
            }
        });

        phoneNumberEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                phoneNumberLayout.setError(null);
            } else {
                if (phoneNumberEditText.getText().toString().equals("")) {
                    validateEditTextEmpty(phoneNumberEditText, phoneNumberLayout);
                } else {
                    checkIfEditTextValidPhoneNumber(phoneNumberEditText, phoneNumberLayout);
                }
            }
        });
    }

    private void validateAndAddUser() {
        boolean invalidField = false;
        // double check all form fields filled
        if (!validateEditTextEmpty(userNameEditText, userNameLayout)) {
            invalidField = true;
        }
        if (!validateEditTextEmpty(firstNameEditText, firstNameLayout)) {
            invalidField = true;
        }
        if (!validateEditTextEmpty(lastNameEditText, lastNameLayout)) {
            invalidField = true;
        }
        if (!checkIfEditTextValidEmail(emailAddressEditText, emailAddressLayout)) {
            invalidField = true;
        }
        if (!checkIfEditTextValidPhoneNumber(phoneNumberEditText, phoneNumberLayout)) {
            invalidField = true;
        }
        if (invalidField) {
            return;
        }

        // check username is not in db already
        String username = userNameEditText.getText().toString();
        FirebaseProvider.getInstance().retrieveUserByUsername(username,
            user -> {
                if (user == null) {
                    // Get values from fields and create user object
                    String firstName = firstNameEditText.getText().toString();
                    String lastName = lastNameEditText.getText().toString();
                    String emailAddress = emailAddressEditText.getText().toString();
                    String phoneNumber = phoneNumberEditText.getText().toString();

                    // add user and go back to main activity
                    User newUser = new User(username, firstName, lastName, emailAddress, phoneNumber);
                    FirebaseProvider.getInstance().storeUser(newUser,
                        aVoid -> {
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            intent.putExtra("SIGNED_UP_USERNAME", username);
                            startActivity(intent);
                        },
                        e -> DialogUtil.showErrorDialog(this, e)
                    );
                } else {
                    userNameLayout.setError("Username already registered. Please choose another");
                }
            },
            e -> DialogUtil.showErrorDialog(this, e)
        );
    }
}
