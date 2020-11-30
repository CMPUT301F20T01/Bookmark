package com.example.bookmark.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.example.bookmark.R;
import com.example.bookmark.models.Book;

/**
 * Dialog fragment that allows the user to select checkboxes for each Book state (available,
 * requested, accepted, borrowed). Any class which shows this dialog fragment should implement
 * FilterDialogListener to receive the boolean values of the checkboxes for future filtering.
 *
 * @author Eric Claerhout.
 */
public class FilterDialogFragment extends DialogFragment {
    private static final String FILTER_CHECK_ENABLED = "CHECK_STATUS";

    private FilterDialogFragment.FilterDialogListener listener;
    private Context context;
    private String TAG;
    private final CheckBox[] checkboxes = new CheckBox[Book.Status.values().length];
    private final int[] checkboxIds = new int[] {
        R.id.filter_available_checkbox,
        R.id.filter_requested_checkbox,
        R.id.filter_accepted_checkbox,
        R.id.filter_borrowed_checkbox
    };

    /**
     * Listener interface for when filter checkboxes are updated
     */
    public interface FilterDialogListener {
        /**
         * Fires when the filter checkboxes have been updated by the user.
         * @param statusFilterEnabled list of checkbox states (true = enabled)
         */
        void onFilterUpdate(boolean[] statusFilterEnabled);
    }


    /**
     * Use this factory method to create a new instance of this fragment.
     *
     * @param checkboxEnabled list of states to initialize checkboxes to
     * @return A new instance of fragment FilterDialogFragment.
     */
    public static FilterDialogFragment newInstance(boolean[] checkboxEnabled) {
        FilterDialogFragment fragment = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putBooleanArray(FILTER_CHECK_ENABLED, checkboxEnabled);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FilterDialogFragment.FilterDialogListener) {
            listener = (FilterDialogFragment.FilterDialogListener) context;
        } else {
            throw new RuntimeException(context.toString()
                + " must implement FilterDialogListener");
        }
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        TAG = getTag();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View filterView = LayoutInflater.from(context).inflate(R.layout.fragment_filter_dialog, null);

        Bundle bundle = getArguments();
        boolean[] checkboxEnabled = bundle.getBooleanArray(FILTER_CHECK_ENABLED);
        for (int i=0; i < checkboxes.length; i++) {
            checkboxes[i] = filterView.findViewById(checkboxIds[i]);
            checkboxes[i].setChecked(checkboxEnabled[i]);
        }

        AlertDialog dialog = builder
            .setView(filterView)
            .setTitle(getString(R.string.filter_book))
            .setPositiveButton(getString(R.string.apply), (d,w) -> updateFilters())
            .setNegativeButton(getString(R.string.cancel), null)
            .create();

        dialog.setOnShowListener(shownDialog -> {
            ((AlertDialog) shownDialog)
                .getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            ((AlertDialog) shownDialog)
                .getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        });

        return dialog;
    }

    /**
     * Returns an updated list of checkbox states via the FilterDialogListener.
     */
    private void updateFilters() {
        boolean[] checkboxEnabled = new boolean[checkboxes.length];
        for (int i=0; i < checkboxes.length; i++) {
            checkboxEnabled[i] = checkboxes[i].isChecked();
        }
        listener.onFilterUpdate(checkboxEnabled);
    }
}
