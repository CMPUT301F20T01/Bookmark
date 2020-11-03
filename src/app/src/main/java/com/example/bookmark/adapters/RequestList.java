package com.example.bookmark.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookmark.R;
import com.example.bookmark.models.Request;
import com.example.bookmark.server.FirebaseProvider;

import java.util.ArrayList;

public class RequestList extends ArrayAdapter<Request> {

    private ArrayList<Request> requests;
    private Context context;

    public RequestList(Context context, ArrayList<Request> requests) {
        super(context, 0, requests);
        this.requests = requests;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.request_content, parent, false);
        }

        Request request = requests.get(position);

        TextView borrowerName = view.findViewById(R.id.borrower_text);
        TextView requestDate = view.findViewById(R.id.request_date_text);

        borrowerName.setText(request.getRequester().getUsername());
        requestDate.setText(request.getRequestDate());

        Button acceptButton = view.findViewById(R.id.accept_button);
        Button rejectButton = view.findViewById(R.id.reject_button);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseProvider.getInstance().acceptRequest(request);
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseProvider.getInstance().rejectRequest(request);
            }
        });

        return view;
    }

}
