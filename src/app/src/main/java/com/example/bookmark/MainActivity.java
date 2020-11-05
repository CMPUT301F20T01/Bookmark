package com.example.bookmark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.bookmark.models.Book;
import com.example.bookmark.models.Borrower;
import com.example.bookmark.models.Request;
import com.example.bookmark.server.FirebaseProvider;

/**
 * Starting point of the application.
 * Allows the user to login with their username.
 * @author Konrad Staniszewski
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(this, ManageRequestsActivity.class);
        Book b = new Book("Code Complete 2", "Steve McConnell", "0-7356-1976-0", "jane.doe98");
        Borrower borrower = new Borrower("jonathan.doe", "jonathan", "doe", "jonathadoe@ualberta.ca", "222 222 2222");
        Bundle bundle = new Bundle();
        bundle.putSerializable("Book", b);
        i.putExtras(bundle);
        startActivity(i);
    }
}
