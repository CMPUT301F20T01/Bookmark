package com.example.bookmark.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookmark.AcceptRequestsActivity;
import com.example.bookmark.ManageRequestsActivity;
import com.example.bookmark.R;
import com.example.bookmark.models.Request;
import com.example.bookmark.server.FirebaseProvider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
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

        borrowerName.setText(request.getRequester());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        requestDate.setText(sdf.format(request.getCreatedDate()));

        Button acceptButton = (Button) view.findViewById(R.id.accept_button);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AcceptRequestsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Request", requests.get(position));
                i.putExtras(bundle);
                Activity origin = (Activity) context;
                origin.startActivityForResult(i, ManageRequestsActivity.GET_MEETING_LOCATION);
            }
        });

        Button rejectButton = (Button) view.findViewById(R.id.reject_button);

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request r = requests.get(position);
                FirebaseProvider.getInstance().deleteRequest(r, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        requests.remove(position);
                        notifyDataSetChanged();
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });

        return view;
    }

}
