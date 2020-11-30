package com.example.bookmark.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.bookmark.BorrowedActivity;
import com.example.bookmark.MyBooksActivity;
import com.example.bookmark.PendingRequestsActivity;
import com.example.bookmark.R;
import com.example.bookmark.models.Book;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is a custom adapter to show books in a list view. The layout used
 * is BookPreview
 *
 * @author Mitch Adam.
 * @author Eric Claerhout.
 */
public class BookList extends ArrayAdapter<Book> implements Filterable {
    public static final String STATUS_FILTER_OP = "status:";
    public static final String FILTER_OP_DELIM = ",";

    private final List<Book> bookList;
    private List<Book> filteredBookList;

    private final Context context;

    private TextView owner;
    private TextView status;
    private TextView description;

    public BookList(Context context, List<Book> books) {
        super(context, 0, books);
        this.bookList = books;
        this.filteredBookList = books;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.book_preview, parent, false);
        }

        Book book = getItem(position);

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

        notificationIcon.setVisibility(View.INVISIBLE);
        if (context instanceof MyBooksActivity) {
            hideBookOwner(view);
            if (book.getStatus() == Book.Status.REQUESTED) {
                notificationIcon.setVisibility(View.VISIBLE);
            }
        } else if (context instanceof PendingRequestsActivity) {
            if (book.getStatus() == Book.Status.ACCEPTED) {
                notificationIcon.setVisibility(View.VISIBLE);
            }
        } else if (context instanceof BorrowedActivity) {
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

    @Override
    public int getCount() {
        return filteredBookList.size();
    }

    @Override
    public Book getItem(int i) {
        return filteredBookList.get(i);
    }

    public Filter getFilter() {
        return new BookFilter();
    }

    /**
     * Interface to hold lambda filtering functions for future evaluation
     */
    private interface FilterFunction {
        boolean eval(Book book);
    }

    private class BookFilter extends Filter {

        /**
         * Generates a list of filters based on the constraint string
         *
         * @param constraint
         * @return list of filter functions
         */
        private List<FilterFunction> getFilters(String constraint) {
            ArrayList<FilterFunction> filters = new ArrayList<>();

            for (String tok: constraint.split("\\s+")) {
                if (tok.startsWith(STATUS_FILTER_OP)) {
                    List<String> statuses = Arrays.asList(tok.replace(STATUS_FILTER_OP, "").split(FILTER_OP_DELIM));
                    filters.add((book) -> statuses.contains(book.getStatus().toString()));
                } else if (!TextUtils.isEmpty(tok)) {  // Regular search terms
                    filters.add((book) ->
                        book.getDescription().toLowerCase().contains(tok.toLowerCase()) ||
                            book.getTitle().toLowerCase().contains(tok.toLowerCase()) ||
                            book.getAuthor().toLowerCase().contains(tok.toLowerCase())
                    );
                }
            }
            return filters;
        }

        /**
         * Generates a FilterResults object based on a given list
         *
         * @param bookList List of books
         * @return FilterResults object
         */
        private FilterResults getFilterResults(List<Book> bookList) {
            FilterResults results = new FilterResults();
            results.count = bookList.size();
            results.values = bookList;
            return results;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint == null) {
                return getFilterResults(bookList);
            }

            ArrayList<Book> resultsList = new ArrayList<>();
            List<FilterFunction> filters = getFilters(constraint.toString());
            if (filters.size() == 0) {
                return getFilterResults(bookList);
            }

            // Evaluate filters to build resultsList
            for (Book book: bookList) {
                boolean match = true;
                for (FilterFunction filter: filters) {
                    if (!filter.eval(book)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    resultsList.add(book);
                }
            }

            return getFilterResults(resultsList);
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredBookList = (List<Book>) results.values;
            notifyDataSetChanged();
        }
    }
}
