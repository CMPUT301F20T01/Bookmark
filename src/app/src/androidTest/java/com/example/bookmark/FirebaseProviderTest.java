package com.example.bookmark;

import com.example.bookmark.models.Book;
import com.example.bookmark.models.Borrower;
import com.example.bookmark.models.User;
import com.example.bookmark.server.FirebaseProvider;

import org.junit.Test;

import java.util.concurrent.Semaphore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class FirebaseProviderTest {
    /**
     * Tests the creation of a user.
     */
    @Test
    public void testCreateAndGetUser() {
        Semaphore semaphore = new Semaphore(0);
        User user = new User("john.smith42", "John", "Smith", "jsmith@ualberta.ca", "7801234567");
        FirebaseProvider.getInstance().storeUser(user, aVoid -> {
            FirebaseProvider.getInstance().retrieveUserByUsername(user.getUsername(), user2 -> {
                assertEquals(user, user2);
                semaphore.release();
            }, e -> {
                fail("An error occured while getting the user.");
            });
        }, e -> {
            fail("An error occured while creating the user.");
        });
        while (true) {
            try {
                semaphore.acquire();
                break;
            } catch (InterruptedException e) {
                // Ignores interruptions.
            }
        }
    }

    /**
     * Tests the creation of a book.
     */
    @Test
    public void testCreateAndGetBook() {
        Semaphore semaphore = new Semaphore(0);
        Book book = new Book("Code Complete 2", "Steve McConnell", "0-7356-1976-0", "john.smith42");
        FirebaseProvider.getInstance().storeBook(book, aVoid -> {
            FirebaseProvider.getInstance().retrieveBookByIsbn(book.getIsbn(), book2 -> {
                assertEquals(book, book2);
                semaphore.release();
            }, e -> {
                fail("An error occured while getting the book.");
            });
        }, e -> {
            fail("An error occured while creating the book.");
        });
        while (true) {
            try {
                semaphore.acquire();
                break;
            } catch (InterruptedException e) {
                // Ignores interruptions.
            }
        }
    }

    /**
     * Tests the creation of a book with a request.
     */
    @Test
    public void testCreateAndGetBookWithRequest() {
        Semaphore semaphore = new Semaphore(0);
        Borrower borrower = new Borrower("mary.jane9", "Mary", "Jane", "mjane@ualberta.ca", "7809999999");
        Book book = new Book("Programming Pearls", "Jon Bentley", "978-0-201-65788-3", "john.smith42");
        book.requestBook(borrower);
        FirebaseProvider.getInstance().storeBook(book, aVoid -> {
            FirebaseProvider.getInstance().retrieveBookByIsbn(book.getIsbn(), book2 -> {
                assertEquals(book, book2);
                semaphore.release();
            }, e -> {
                fail("An error occured while getting the book.");
            });
        }, e -> {
            fail("An error occured while creating the book.");
        });
        while (true) {
            try {
                semaphore.acquire();
                break;
            } catch (InterruptedException e) {
                // Ignores interruptions.
            }
        }
    }
}
