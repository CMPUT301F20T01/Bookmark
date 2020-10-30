package com.example.bookmark.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookmark.R;
import com.example.bookmark.models.Book;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * This is a custom adapter to show books in a list view. The layout used
 * is BookPreview
 *
 * @author Mitch Adam.
 */
public class BookList extends ArrayAdapter<Book> {

    private ArrayList<Book> booksList;
    private Context context;

    public BookList(Context context, ArrayList<Book> books) {
        super(context, 0, books);
        this.booksList = books;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.book_preview, parent,false);
        }

        Book book = booksList.get(position);

        ImageView image = view.findViewById(R.id.book_preview_image);
        TextView title = view.findViewById(R.id.bok_preview_title_text);
        TextView author = view.findViewById(R.id.book_preview_author_text);
        TextView description = view.findViewById(R.id.book_preview_description_text);
        TextView status = view.findViewById(R.id.book_preview_status_text);

        //image.setImageBitmap(book.getPhotograph());

        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        description.setText(book.getDescription());
        status.setText("Status: " + book.getStatus().toString());

        return view;
    }
}
