package com.example.bookmark;

import com.example.bookmark.models.Book;
import com.example.bookmark.models.Request;
import com.example.bookmark.models.User;
import com.example.bookmark.server.FirebaseProvider;
import com.example.bookmark.server.FirestoreIndexable;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.Query;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class FirebaseProviderTest {
    /**
     * Prevents unit tests from modifying collections used in production.
     */
    private static class MockFirebaseProvider extends FirebaseProvider {
        private static final MockFirebaseProvider instance = new MockFirebaseProvider();

        public static MockFirebaseProvider getInstance() {
            return instance;
        }

        @Override
        protected void storeEntity(String collection, FirestoreIndexable entity, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
            super.storeEntity(mock(collection), entity, onSuccessListener, onFailureListener);
        }

        @Override
        protected <T> void retrieveEntity(String collection, String id, Function<Map<String, Object>, T> fromFirestoreDocument, OnSuccessListener<T> onSuccessListener, OnFailureListener onFailureListener) {
            super.retrieveEntity(mock(collection), id, fromFirestoreDocument, onSuccessListener, onFailureListener);
        }

        @Override
        protected <T> void retrieveEntities(String collection, Function<Map<String, Object>, T> fromFirestoreDocument, OnSuccessListener<List<T>> onSuccessListener, OnFailureListener onFailureListener) {
            super.retrieveEntities(mock(collection), fromFirestoreDocument, onSuccessListener, onFailureListener);
        }

        @Override
        protected <T> void retrieveEntitiesMatching(String collection, Function<Query, Query> conditions, Function<Map<String, Object>, T> fromFirestoreDocument, OnSuccessListener<List<T>> onSuccessListener, OnFailureListener onFailureListener) {
            super.retrieveEntitiesMatching(mock(collection), conditions, fromFirestoreDocument, onSuccessListener, onFailureListener);
        }

        @Override
        protected void deleteEntity(String collection, String id, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
            super.deleteEntity(mock(collection), id, onSuccessListener, onFailureListener);
        }

        private String mock(String collection) {
            return String.format("mock-%s", collection);
        }
    }

    private static final FirebaseProvider firebaseProvider = MockFirebaseProvider.getInstance();

    /**
     * Tests the creation of a user.
     */
    @Test
    public void testCreateAndGetUser() {
        Semaphore semaphore = new Semaphore(0);
        User user = mockUser();
        firebaseProvider.storeUser(user, aVoid -> {
            firebaseProvider.retrieveUserByUsername(user.getUsername(), user2 -> {
                assertEquals(user, user2);
                semaphore.release();
            }, e -> {
                fail("An error occurred while getting the user.");
            });
        }, e -> {
            fail("An error occurred while creating the user.");
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
        User owner = mockUser();
        Book book = new Book(owner, "Code Complete 2", "Steve McConnell", "0-7356-1976-0");
        firebaseProvider.storeBook(book, aVoid -> {
            firebaseProvider.retrieveBook(owner, book.getIsbn(), book2 -> {
                assertEquals(book, book2);
                semaphore.release();
            }, e -> {
                fail("An error occurred while getting the book.");
            });
        }, e -> {
            fail("An error occurred while creating the book.");
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
     * Tests the creation and deletion of a request.
     */
    @Test
    public void testCreateAndGetAndDeleteRequest() {
        Semaphore semaphore = new Semaphore(0);
        User owner = mockUser();
        User borrower = mockBorrower();
        Book book = new Book(owner, "Programming Pearls", "Jon Bentley", "978-0-201-65788-3");
        Request request = new Request(book, borrower, null);
        firebaseProvider.storeRequest(request, aVoid -> {
            firebaseProvider.retrieveRequest(book, borrower, request2 -> {
                assertEquals(request, request2);
                firebaseProvider.deleteRequest(request, aVoid2 ->
                    semaphore.release(), e -> {
                    fail("An error occurred while deleting the request");
                });
            }, e -> {
                fail("An error occurred while getting the request.");
            });
        }, e -> {
            fail("An error occurred while creating the request.");
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

    private User mockUser() {
        return new User("john.smith42", "John", "Smith", "jsmith@ualberta.ca", "7801234567");
    }

    private User mockBorrower() {
        return new User("mary.jane9", "Mary", "Jane", "mjane@ualberta.ca", "7809999999");
    }
}
