package com.example.bookmark.util;

import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

/**
 * This class contains static methods used to validate user
 * info fields in SignUpActivity and EditProfileActivity
 *
 * @author Konrad Staniszewski.
 */
public class UserInfoFormValidator {
    /**
     * Function to check if a EditText field is empty and float an error if it is.
     *
     * @args EditText editText: the text field to check
     * TextInputLayout layout: layout containing textfield
     * @returns boolean: whether or not the field is empty
     */
    public static boolean validateEditTextEmpty(EditText editText, TextInputLayout layout) {
        if (editText.getText().toString().equals("")) {
            layout.setError("This field cannot be left empty.");
            return false;
        } else {
            layout.setError(null);
            return true;
        }
    }

    /**
     * Function to check if a EditText field is a valid email and float an error if it is
     *
     * @args EditText editText: the text field to check
     * TextInputLayout layout: layout containing textfield
     * @returns boolean: whether or not the editText contains a valid email
     */
    public static boolean checkIfEditTextValidEmail(EditText editText, TextInputLayout layout) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String string = editText.getText().toString();
        if (string.equals("")) {
            layout.setError("This field cannot be left empty");
            return false;
        } else if (!string.matches(emailPattern)) {
            layout.setError("Please enter a valid email address.");
            return false;
        } else {
            layout.setError(null);
            return true;
        }
    }

    /**
     * Function to check if a EditText field is a valid phone number and float an error if it is
     *
     * @args EditText editText: the text field to check
     * TextInputLayout layout: layout containing textfield
     * @returns boolean: whether or not the editText contains a phone number
     */
    public static boolean checkIfEditTextValidPhoneNumber(EditText editText, TextInputLayout layout) {
        String phonePattern = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
        String string = editText.getText().toString();
        if (string.equals("")) {
            layout.setError("This field cannot be left empty");
            return false;
        } else if (!string.matches(phonePattern)) {
            layout.setError("Please enter a valid phone number.");
            return false;
        } else {
            layout.setError(null);
            return true;
        }
    }
}
