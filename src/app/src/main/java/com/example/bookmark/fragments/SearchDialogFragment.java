package com.example.bookmark.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.bookmark.R;

/**
 * Creates a fragment that allows the user to enter keywords and perform a
 * search - note any class that uses this fragment must implement the
 * OnFragmentInteractionListener interface (i.e. implement the
 * executeSearch(..) function which should perform a search of the database in
 * the desired way. The user is required to enter something in the search bar
 * editText if they want to perform a search (if they have not entered
 * anything and they try to press confirm the fragment will prompt them to do
 * so.
 *
 * @author Ryan Kortbeek.
 */
public class SearchDialogFragment extends DialogFragment {
    private EditText searchEditText;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void executeSearch(String searchString);
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
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view =
            LayoutInflater.from(getActivity()).inflate(R.layout.fragment_search_dialog, null);
        searchEditText = view.findViewById(R.id.search_bar_editText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // Available books being all books that are not currently accepted or
        // borrowed
        builder.setView(view).setTitle("Search Available Books").setNegativeButton(
            "CANCEL", null).setPositiveButton("CONFIRM", null);
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean proceed = Boolean.TRUE;
                // There must be some text in the search bar to execute a search
                if (TextUtils.isEmpty(searchEditText.getText())) {
                    searchEditText.setError("Please enter the keywords that " +
                        "you would like to search.");
                    proceed = Boolean.FALSE;
                }
                if (proceed) {
                    listener.executeSearch(searchEditText.getText().toString());
                    dialog.dismiss();
                }
            }
        });
        return dialog;
    }
}
