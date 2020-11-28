package com.example.bookmark.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.bookmark.R;
import com.google.android.material.textfield.TextInputEditText;

/**
 * Creates a fragment that allows the user to enter keywords and perform a
 * search - note any class that uses this fragment must implement the
 * OnFragmentInteractionListener interface (i.e. implement the
 * executeSearch(..) function which should pass the searched keywords as a
 * string in an intent using the key "com.example.bookmark.SEARCH" to the
 * ExploreActivity. The user is required to enter something in the search bar
 * editText if they want to perform a search (if they have not entered
 * anything and they try to press confirm the fragment will prompt them to do
 * so).
 *
 * @author Ryan Kortbeek.
 */
public class SearchDialogFragment extends DialogFragment {
    private TextInputEditText searchTextInput;
    private OnFragmentInteractionListener listener;
    private Context context;

    public interface OnFragmentInteractionListener {
        void sendSearchedKeywords(String searchString);
    }

    /**
     * Use this factory method to create a new instance of this fragment.
     *
     * @return A new instance of fragment SearchDialogFragment.
     */
    public static SearchDialogFragment newInstance() {
        return new SearchDialogFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement " +
                "OnFragmentInteractionListener");
        }
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view =
            LayoutInflater.from(context).inflate(R.layout.fragment_search_dialog,
                null);
        searchTextInput = view.findViewById(R.id.search_bar_textInput);

        // Available books being all books that are not currently accepted or
        // borrowed
        final AlertDialog dialog = builder
            .setView(view)
            .setTitle("Search Available Books")
            .setNegativeButton(getString(R.string.cancel), null)
            .setPositiveButton(getString(R.string.confirm), null).create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button posButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                posButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean proceed = true;
                        // There must be some text in the search bar to execute a search
                        if (TextUtils.isEmpty(searchTextInput.getText())) {
                            searchTextInput.setError("Please enter the keywords that " +
                                "you would like to search.");
                            proceed = false;
                        }
                        if (proceed) {
                            listener.sendSearchedKeywords(
                                searchTextInput.getText().toString()
                            );
                            dialog.dismiss();
                        }
                    }
                });
                posButton.setTextColor(ContextCompat.getColor(context,
                    R.color.colorPrimary));
                ((AlertDialog) dialog)
                    .getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat
                        .getColor(context, R.color.colorPrimary));
            }
        });
        return dialog;
    }
}
