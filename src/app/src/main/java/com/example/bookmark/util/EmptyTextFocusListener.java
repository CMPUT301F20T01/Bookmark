package com.example.bookmark.util;

import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import static com.example.bookmark.util.UserInfoFormValidator.validateEditTextEmpty;

/**
 * Custom OnFocusChangeListener to eliminate super repetitive listener
 * declarations for all "empty text" checking situations.
 */
public class EmptyTextFocusListener implements View.OnFocusChangeListener {
    EditText text;
    TextInputLayout layout;

    public EmptyTextFocusListener(EditText text, TextInputLayout layout) {
        this.text = text;
        this.layout = layout;
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            this.layout.setError(null);
        } else {
            validateEditTextEmpty(this.text, this.layout);
        }
    }
}
