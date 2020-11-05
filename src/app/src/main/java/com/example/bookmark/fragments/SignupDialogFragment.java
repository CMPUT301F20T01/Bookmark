package com.example.bookmark.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.bookmark.R;
import com.example.bookmark.models.User;
import com.example.bookmark.server.FirebaseProvider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

/**
 * Creates a fragment that allows the user to create a new account
 * This fragment performs a check against the database to ensure that the entered
 * username is unique.
 *
 * @author Konrad Staniszewski.
 */
public class SignupDialogFragment extends DialogFragment {
    private EditText userNameEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailAddressEditText;
    private EditText phoneNumberEditText;
    private Button signUpButton;
    private Button cancelButton;

    private OnFragmentInteractionListener listener;

    /**
     * this interface specifies what functions must be implemented by mainActivity to
     * work with this dialog
     */
    public interface OnFragmentInteractionListener {
        void onSignUpPressed(User user);
    }

    /**
     * This function runs once the dialog fragment is attached and sets up the
     * fragment interaction listener so that the main activity knows then the dialog
     * buttons are pressed
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                +"must implement OnFragmentInteractionListener");
        }
    }

    /**
     * This functin creates the dialog fragment and takes care of all of the form
     * editing logic
     * @param savedInstanceState
     * @return Dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // fetch FirebaseProvider
        FirebaseProvider firebaseProvider = FirebaseProvider.getInstance();

        // Fetch relevant views
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_signup_dialog, null);
        userNameEditText = view.findViewById(R.id.sign_up_username_edit_text);
        firstNameEditText = view.findViewById(R.id.sign_up_firstname_edit_text);
        lastNameEditText = view.findViewById(R.id.sign_up_lastname_edit_text);
        emailAddressEditText = view.findViewById(R.id.sign_up_email_edit_text);
        phoneNumberEditText = view.findViewById(R.id.sign_up_phonenumber_edit_text);
        signUpButton = view.findViewById(R.id.sign_up_sign_up_button);
        cancelButton = view.findViewById(R.id.sign_up_cancel_button);

        // Build dialog fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.setTitle("Sign Up");
        final Dialog dialog = builder.create();

        /**
         * Sets behaviour for when signup button is pressed
         * This includes checking hat all text boxes are filled, validating input
         * and creates a user object to send back to the main activity.
         */
        signUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ArrayList<EditText> formFields= new ArrayList<>();
                formFields.add(userNameEditText);
                formFields.add(firstNameEditText);
                formFields.add(lastNameEditText);
                formFields.add(emailAddressEditText);
                formFields.add(phoneNumberEditText);

                // double check all form fields filled
                for (int i = 0; i < formFields.size(); i++) {
                    checkEditTextEmpty(formFields.get(i), false);
                    if (formFields.get(i).getError() != null) {
                        return;
                    }
                }

                // check email and phone validation
                if (!checkIfEditTextValidEmail(emailAddressEditText, false)
                    || !checkIfEditTextValidPhoneNumber(phoneNumberEditText, false)) {
                    return;
                }

                // check username is not in db already
                String username = userNameEditText.getText().toString();
                firebaseProvider.getUserByUsername(username,
                    new OnSuccessListener<User>() {
                        @Override
                        public void onSuccess(User user) {
                            if (user == null) {
                                // Get values from fields and create user object
                                String firstName = firstNameEditText.getText().toString();
                                String lastName = lastNameEditText.getText().toString();
                                String emailAddress = emailAddressEditText.getText().toString();
                                String phoneNumber = phoneNumberEditText.getText().toString();

                                User newUser = new User(username, firstName, lastName, emailAddress, phoneNumber);
                                listener.onSignUpPressed(newUser);
                                dialog.dismiss();
                            }
                            else {
                                userNameEditText.setError("Username already registered. Please choose another");
                                return;
                            }
                        }
                    },
                    new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Connection Error. Please Try again.", Toast.LENGTH_LONG).show();
                        }
                    });
            }
        });

        /**
         * Listener
         */
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        /**
         * Listener to validate and remind user to fill text view
         */
        userNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkEditTextEmpty(userNameEditText, hasFocus);
            }
        });

        /**
         * Listener to validate and remind user to fill text view
         */
        firstNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkEditTextEmpty(firstNameEditText, hasFocus);
            }
        });

        /**
         * Listener to validate and remind user to fill text view
         */
        lastNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkEditTextEmpty(lastNameEditText, hasFocus);
            }
        });

        /**
         * Listener to validate and remind user to fill text view
         */
        emailAddressEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkEditTextEmpty(emailAddressEditText, hasFocus);
                checkIfEditTextValidEmail(emailAddressEditText, hasFocus);
            }
        });

        /**
         * Listener to validate and remind user to fill text view
         */
        phoneNumberEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkEditTextEmpty(phoneNumberEditText, hasFocus);
                checkIfEditTextValidPhoneNumber(phoneNumberEditText, hasFocus);
            }
        });

        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkIfEditTextValidPhoneNumber(phoneNumberEditText, false);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        emailAddressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkIfEditTextValidEmail(emailAddressEditText, false);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        return dialog;
    }

    /**
     * Function to check if a EditText field is empty and float an error if it is.
     * @args
     * EditText editText: the text field to check
     * boolean hasFocus: whether or not the text field has focus
     * @returns
     * boolean: whether or not the field is empty
     * */
    private boolean checkEditTextEmpty(EditText editText, boolean hasFocus) {
        if(!hasFocus) {
            String string = editText.getText().toString();
            if (string.equals("")) {
                editText.setError("This field cannot be left empty.");
                return true;
            }
            return false;
        }
        return true;
    }

    /**
     * Function to check if a EditText field is a valid email and float an error if it is
     * @args
     * EditText editText: the text field to check
     * boolean hasFocus: whether or not the text field has focus
     * @returns
     * boolean: whether or not the editText contains a valid email
     * */
    private boolean checkIfEditTextValidEmail(EditText editText, boolean hasFocus) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!hasFocus) {
            String string = editText.getText().toString();
            if (!string.matches(emailPattern) && string.length() > 0) {
                editText.setError("Please enter a valid email address.");
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Function to check if a EditText field is a valid phone number and float an error if it is
     * @args
     * EditText editText: the text field to check
     * boolean hasFocus: whether or not the text field has focus
     * @returns
     * boolean: whether or not the editText contains a phone number
     * */
    private boolean checkIfEditTextValidPhoneNumber(EditText editText, boolean hasFocus) {
        String phonePattern = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
        if (!hasFocus) {
            String string = editText.getText().toString();
            if (!string.matches(phonePattern) && string.length() > 0) {
                editText.setError("Please enter a valid phone number.");
                return false;
            }
            return true;
        }
        return false;
    }
}
