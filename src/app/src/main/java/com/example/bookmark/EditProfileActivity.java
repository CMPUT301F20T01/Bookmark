package com.example.bookmark;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.example.bookmark.server.FirebaseProvider;
import com.example.bookmark.util.DialogUtil;
import com.example.bookmark.util.EmptyTextFocusListener;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import static com.example.bookmark.util.UserInfoFormValidator.checkIfEditTextValidEmail;
import static com.example.bookmark.util.UserInfoFormValidator.checkIfEditTextValidPhoneNumber;
import static com.example.bookmark.util.UserInfoFormValidator.validateEditTextEmpty;

/**
 * This activity allows a user to edit their profile information.
 * It provides editable fields pre-populated with their information.
 * When the done button is hit, their profile information is updated in the database
 * and they are returned to their MyProfileActivity.
 *
 * @author Konrad Staniszewski
 */
public class EditProfileActivity extends BackButtonActivity {

    private EditText firstNameEditText, lastNameEditText, emailAddressEditText, phoneNumberEditText;
    private TextInputLayout firstNameLayout, lastNameLayout, emailAddressLayout, phoneNumberLayout;
    private Button doneButton;
    private String loggedUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setTitle("Edit Profile");

        firstNameEditText = findViewById(R.id.edit_profile_firstname_edit_text);
        lastNameEditText = findViewById(R.id.edit_profile_lastname_edit_text);
        emailAddressEditText = findViewById(R.id.edit_profile_email_edit_text);
        phoneNumberEditText = findViewById(R.id.edit_profile_phonenumber_edit_text);

        firstNameLayout = findViewById(R.id.edit_profile_firstname_textInputLayout);
        lastNameLayout = findViewById(R.id.edit_profile_lastname_textInputLayout);
        emailAddressLayout = findViewById(R.id.edit_profile_email_textInputLayout);
        phoneNumberLayout = findViewById(R.id.edit_profile_phonenumber_textInputLayout);

        doneButton = findViewById(R.id.edit_profile_done_button);
        doneButton.setOnClickListener(v -> editUserProfile());

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

        firstNameEditText.setOnFocusChangeListener(new EmptyTextFocusListener(firstNameEditText, firstNameLayout));
        lastNameEditText.setOnFocusChangeListener(new EmptyTextFocusListener(lastNameEditText, lastNameLayout));

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

        // pre populate fields with user current user info
        SharedPreferences sharedPref = this.getSharedPreferences("LOGGED_IN_USER", Context.MODE_PRIVATE);
        loggedUsername = sharedPref.getString("USER_NAME", "ERROR_NO_USER");
        if (loggedUsername.equals("ERROR_NO_USER")) {
            DialogUtil.showErrorDialog(this, new Exception(loggedUsername));
        } else {
            prepopulateTextFields(loggedUsername);
        }
    }

    private void prepopulateTextFields(String username) {
        FirebaseProvider.getInstance().retrieveUserByUsername(username, user -> {
            firstNameEditText.setText(user.getFirstName());
            lastNameEditText.setText(user.getLastName());
            emailAddressEditText.setText(user.getEmailAddress());
            phoneNumberEditText.setText(user.getPhoneNumber());
        }, e -> DialogUtil.showErrorDialog(this, e));
    }

    private void editUserProfile() {

    }

    private void goToMyProfile() {
        Intent intent = new Intent(EditProfileActivity.this, MyProfileActivity.class);
        startActivity(intent);
    }
}
