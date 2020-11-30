package com.example.bookmark.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.bookmark.R;
import com.example.bookmark.models.Book;

import java.util.List;

/**
 * This is a custom adapter to show books in a list view. The layout used
 * is BookPreview
 *
 * @author Mitch Adam.
 */
public class BookList extends ArrayAdapter<Book> {

    private final List<Book> booksList;
    private final Context context;

    private final boolean showOwner;
    private final boolean showStatus;
    private String activityTitle;

    private TextView owner;
    private TextView status;
    private TextView description;

    public BookList(Context context, List<Book> books, boolean showOwner,
                    boolean showStatus, String activityTitle) {
        super(context, 0, books);
        this.showOwner = showOwner;
        this.showStatus = showStatus;
        this.activityTitle = activityTitle;
        this.booksList = books;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.book_preview, parent, false);
        }

        Book book = booksList.get(position);

        ImageView image = view.findViewById(R.id.book_preview_image);
        TextView title = view.findViewById(R.id.bok_preview_title_text);
        TextView author = view.findViewById(R.id.book_preview_author_text);
        ImageView notificationIcon = view.findViewById(R.id.book_preview_notification_icon);
        description = view.findViewById(R.id.book_preview_description_text);
        owner = view.findViewById(R.id.book_preview_owner_text);
        status = view.findViewById(R.id.book_preview_status_text);

        //image.setImageBitmap(book.getPhotograph());

        String bookStatus = ("Status: "
            + book.getStatus().toString().charAt(0)
            + book.getStatus().toString().substring(1).toLowerCase());
        String bookOwner = "Owner: " + book.getOwnerId().toString();

        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        description.setText(book.getDescription());
        owner.setText(bookOwner);
        status.setText(bookStatus);

        switch(this.activityTitle) {
            case "My Books":
                if (book.getStatus().toString().equals("REQUESTED")) {
                    notificationIcon.setVisibility(View.VISIBLE);
                } break;
            case "Pending Requests":
                if (book.getStatus().toString().equals("ACCEPTED")) {
                    notificationIcon.setVisibility(view.VISIBLE);
                } break;
        }

        if (!showOwner) {
            hideBookOwner(view);
        }
        if (!showStatus) {
            status.setVisibility(TextView.GONE);
        }

        return view;
    }

    private void hideBookOwner(View view) {
        // Hides book owner and constrains the top of book status to the
        // bottom of book description
        owner.setVisibility(TextView.INVISIBLE);
        ConstraintLayout constraintLayout =
            view.findViewById(R.id.book_preview_layout);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(status.getId(), ConstraintSet.TOP,
            description.getId(), ConstraintSet.BOTTOM);
        constraintSet.applyTo(constraintLayout);
    }
}
