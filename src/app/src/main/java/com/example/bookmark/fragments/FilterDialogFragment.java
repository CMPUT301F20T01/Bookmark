package com.example.bookmark.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bookmark.R;

/**
 * TODO: Description of class.
 * @author Eric Claerhout.
 */
public class FilterDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context context;
    private String TAG;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilterDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    // public static FilterDialogFragment newInstance(String param1, String param2) {
    //     FilterDialogFragment fragment = new FilterDialogFragment();
    //     Bundle args = new Bundle();
    //     args.putString(ARG_PARAM1, param1);
    //     args.putString(ARG_PARAM2, param2);
    //     fragment.setArguments(args);
    //     return fragment;
    // }
    public static FilterDialogFragment newInstance() {
        return new FilterDialogFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        TAG = getTag();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View filterView = LayoutInflater.from(context).inflate(R.layout.fragment_filter_dialog, null);

        return builder
            .setView(filterView)
            .setTitle(getString(R.string.filter_book))
            .setPositiveButton(getString(R.string.apply), null)
            .setNegativeButton(getString(R.string.cancel), null)
            .create();
    }
}
