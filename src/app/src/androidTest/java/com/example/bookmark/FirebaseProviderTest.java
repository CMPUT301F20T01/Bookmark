package com.example.bookmark;

import com.example.bookmark.models.Book;
import com.example.bookmark.models.Geolocation;
import com.example.bookmark.models.Request;
import com.example.bookmark.models.User;
import com.example.bookmark.server.FirebaseProvider;
import com.example.bookmark.server.FirestoreIndexable;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.Query;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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
     * Creates and populates the database before the tests are run.
     */
    @BeforeClass
    public static void createDatabase() {
        Semaphore semaphore = new Semaphore(0);

        User owner = mockOwner();
        firebaseProvider.storeUser(owner, aVoid -> semaphore.release(), e -> fail("An error occurred while storing the owner."));
        acquire(semaphore);

        User requester = mockRequester();
        firebaseProvider.storeUser(requester, aVoid -> semaphore.release(), e -> fail("An error occurred while storing the requester."));
        acquire(semaphore);

        Book book1 = mockBook1(owner);
        firebaseProvider.storeBook(book1, aVoid -> semaphore.release(), e -> fail("An error occurred while storing book 1."));
        acquire(semaphore);

        Book book2 = mockBook2(owner);
        firebaseProvider.storeBook(book2, aVoid -> semaphore.release(), e -> fail("An error occurred while storing book 2."));
        acquire(semaphore);

        Request request1 = mockRequest(book1, requester);
        firebaseProvider.storeRequest(request1, aVoid -> semaphore.release(), e -> fail("An error occurred while storing request 1."));
        acquire(semaphore);

        Request request2 = mockRequest(book2, requester);
        firebaseProvider.storeRequest(request2, aVoid -> semaphore.release(), e -> fail("An error occurred while storing request 2."));
        acquire(semaphore);
    }

    /**
     * Tests retrieving a user.
     */
    @Test
    public void testRetrieveUser() {
        Semaphore semaphore = new Semaphore(0);
        User user = mockOwner();
        firebaseProvider.retrieveUserByUsername(user.getUsername(), user2 -> {
            assertEquals(user, user2);
            semaphore.release();
        }, e -> fail("An error occurred while retrieving the user."));
        acquire(semaphore);
    }

    /**
     * Tests retrieving a book.
     */
    @Test
    public void testRetrieveBook() {
        Semaphore semaphore = new Semaphore(0);
        User owner = mockOwner();
        Book book = mockBook1(owner);
        firebaseProvider.retrieveBook(owner, book.getIsbn(), book2 -> {
            assertEquals(book, book2);
            semaphore.release();
        }, e -> fail("An error occurred while retrieving the book."));
        acquire(semaphore);
    }

    /**
     * Tests retrieving multiple books.
     */
    @Test
    public void testRetrieveMultipleBooks() {
        Semaphore semaphore = new Semaphore(0);
        User owner = mockOwner();
        Book book1 = mockBook1(owner);
        Book book2 = mockBook2(owner);
        firebaseProvider.retrieveBooks(books -> {
            assertTrue(books.contains(book1));
            assertTrue(books.contains(book2));
            semaphore.release();
        }, e -> fail("An error occurred while retrieving the books."));
        acquire(semaphore);
    }

    /**
     * Tests retrieving multiple books by owner.
     */
    @Test
    public void testRetrieveMultipleBooksByOwner() {
        Semaphore semaphore = new Semaphore(0);
        User owner = mockOwner();
        Book book1 = mockBook1(owner);
        Book book2 = mockBook2(owner);
        firebaseProvider.retrieveBooksByOwner(owner, books -> {
            assertTrue(books.contains(book1));
            assertTrue(books.contains(book2));
            semaphore.release();
        }, e -> fail("An error occurred while retrieving the books by owner."));
        acquire(semaphore);
    }

    /**
     * Tests retrieving multiple books by requester.
     */
    @Test
    public void testRetrieveMultipleBooksByRequester() {
        Semaphore semaphore = new Semaphore(0);
        User owner = mockOwner();
        Book book1 = mockBook1(owner);
        Book book2 = mockBook2(owner);
        User requester = mockRequester();
        firebaseProvider.retrieveBooksByRequester(requester, books -> {
            assertTrue(books.contains(book1));
            assertTrue(books.contains(book2));
            semaphore.release();
        }, e -> fail("An error occurred while retrieving the books by requester."));
        acquire(semaphore);
    }

    /**
     * Tests retrieving a request.
     */
    @Test
    public void testRetrieveRequest() {
        Semaphore semaphore = new Semaphore(0);
        User owner = mockOwner();
        Book book = mockBook1(owner);
        User requester = mockRequester();
        Request request = mockRequest(book, requester);
        firebaseProvider.retrieveRequest(book, requester, request2 -> {
            assertEquals(request, request2);
            semaphore.release();
        }, e -> fail("An error occurred while retrieving the request."));
        acquire(semaphore);
    }

    /**
     * Tests deleting a request.
     */
    @Test
    public void testDeleteRequest() {
        Semaphore semaphore = new Semaphore(0);
        User owner = mockOwner();
        Book book = mockBook1(owner);
        User requester = mockRequester();
        Request request = mockRequest(book, requester);
        firebaseProvider.deleteRequest(request, aVoid -> {
            firebaseProvider.retrieveRequest(book, requester, request2 -> {
                firebaseProvider.storeRequest(request, aVoid2 -> {
                    assertNull(request2);
                    semaphore.release();
                }, e -> fail("An error occurred while recreating the deleted request."));
            }, e -> fail("An error occurred while retrieving the deleted request."));
        }, e -> fail("An error occurred while deleting the request"));
        acquire(semaphore);
    }

    /**
     * Tests retrieving multiple requests by book.
     */
    @Test
    public void testRetrieveMultipleRequestsByBook() {
        Semaphore semaphore = new Semaphore(0);
        User owner = mockOwner();
        Book book = mockBook1(owner);
        User requester = mockRequester();
        Request request1 = mockRequest(book, requester);
        Request request2 = mockRequest(book, requester);
        firebaseProvider.retrieveRequestsByBook(book, requests -> {
            assertTrue(requests.contains(request1));
            assertTrue(requests.contains(request2));
            semaphore.release();
        }, e -> {
            fail("An error occurred while retrieving the requests by book.");
        });
        acquire(semaphore);
    }

    /**
     * Tests retrieving multiple requests by requester.
     */
    @Test
    public void testRetrieveMultipleRequestsByRequester() {
        Semaphore semaphore = new Semaphore(0);
        User owner = mockOwner();
        Book book1 = mockBook1(owner);
        Book book2 = mockBook2(owner);
        User requester = mockRequester();
        Request request1 = mockRequest(book1, requester);
        Request request2 = mockRequest(book2, requester);
        firebaseProvider.retrieveRequestsByRequester(requester, requests -> {
            assertTrue(requests.contains(request1));
            assertTrue(requests.contains(request2));
            semaphore.release();
        }, e -> {
            fail("An error occurred while retrieving the requests by requester.");
        });
        acquire(semaphore);
    }

    private static User mockOwner() {
        return new User("john.smith42", "John", "Smith", "jsmith@ualberta.ca", "7801234567");
    }

    private static User mockRequester() {
        return new User("mary.jane9", "Mary", "Jane", "mjane@ualberta.ca", "7809999999");
    }

    private static Book mockBook1(User owner) {
        return new Book(owner, "Code Complete 2", "Steve McConnell", "0-7356-1976-0");
    }

    private static Book mockBook2(User owner) {
        return new Book(owner, "Programming Pearls", "Jon Bentley", "978-0-201-65788-3");
    }

    private static Request mockRequest(Book book, User requester) {
        return new Request(book, requester, new Geolocation(53.5461, -113.4938));
    }

    private static void acquire(Semaphore semaphore) {
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
