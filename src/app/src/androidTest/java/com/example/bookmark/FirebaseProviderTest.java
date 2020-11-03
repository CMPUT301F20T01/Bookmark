package com.example.bookmark;

import com.example.bookmark.models.Book;
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
        FirebaseProvider.getInstance().saveUser(user, aVoid -> {
            FirebaseProvider.getInstance().getUserByUsername(user.getUsername(), user2 -> {
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
        FirebaseProvider.getInstance().saveBook(book, aVoid -> {
            FirebaseProvider.getInstance().getBookByIsbn(book.getIsbn(), book2 -> {
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
