package com.example.bookmark;

import com.example.bookmark.models.Book;
import com.example.bookmark.models.Request;
import com.example.bookmark.models.User;
import com.example.bookmark.server.FirebaseStorageService;
import com.example.bookmark.server.FirestoreIndexable;
import com.example.bookmark.server.InMemoryStorageService;
import com.example.bookmark.server.StorageService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.Query;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

import com.example.bookmark.mocks.MockModels;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests the FirebaseStorageService.
 *
 * @author Kyle Hennig.
 */
public class FirebaseStorageServiceTest {
    /**
     * Prevents unit tests from modifying collections used in production.
     */
    private static class MockFirebaseStorageService extends FirebaseStorageService {
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

    private static final boolean INTERACT_WITH_FIREBASE = false;
    private static final StorageService storageService = INTERACT_WITH_FIREBASE ? new MockFirebaseStorageService() : new InMemoryStorageService();

    /**
     * Creates and populates the database before the tests are run.
     */
    @BeforeClass
    public static void createDatabase() {
        Semaphore semaphore = new Semaphore(0);

        User owner = MockModels.getMockOwner();
        storageService.storeUser(owner, aVoid -> semaphore.release(), e -> fail("An error occurred while storing the owner."));
        acquire(semaphore);

        User requester = MockModels.getMockRequester();
        storageService.storeUser(requester, aVoid -> semaphore.release(), e -> fail("An error occurred while storing the requester."));
        acquire(semaphore);

        Book book1 = MockModels.getMockBook1();
        storageService.storeBook(book1, aVoid -> semaphore.release(), e -> fail("An error occurred while storing book 1."));
        acquire(semaphore);

        Book book2 = MockModels.getMockBook2();
        storageService.storeBook(book2, aVoid -> semaphore.release(), e -> fail("An error occurred while storing book 2."));
        acquire(semaphore);

        Request request1 = MockModels.getMockRequest1();
        storageService.storeRequest(request1, aVoid -> semaphore.release(), e -> fail("An error occurred while storing request 1."));
        acquire(semaphore);

        Request request2 = MockModels.getMockRequest2();
        storageService.storeRequest(request2, aVoid -> semaphore.release(), e -> fail("An error occurred while storing request 2."));
        acquire(semaphore);
    }

    /**
     * Tests retrieving a user.
     */
    @Test
    public void testRetrieveUser() {
        Semaphore semaphore = new Semaphore(0);
        User user = MockModels.getMockOwner();
        storageService.retrieveUserByUsername(user.getUsername(), user2 -> {
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
        User owner = MockModels.getMockOwner();
        Book book = MockModels.getMockBook1();
        storageService.retrieveBook(owner, book.getIsbn(), book2 -> {
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
        Book book1 = MockModels.getMockBook1();
        Book book2 = MockModels.getMockBook2();
        storageService.retrieveBooks(books -> {
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
        User owner = MockModels.getMockOwner();
        Book book1 = MockModels.getMockBook1();
        Book book2 = MockModels.getMockBook2();
        storageService.retrieveBooksByOwner(owner, books -> {
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
        Book book1 = MockModels.getMockBook1();
        Book book2 = MockModels.getMockBook2();
        User requester = MockModels.getMockRequester();
        storageService.retrieveBooksByRequester(requester, books -> {
            assertTrue(books.contains(book1));
            assertTrue(books.contains(book2));
            semaphore.release();
        }, e -> fail("An error occurred while retrieving the books by requester."));
        acquire(semaphore);
    }

    /**
     * Tests deleting a book.
     */
    @Test
    public void testDeleteBook() {
        Semaphore semaphore = new Semaphore(0);
        User owner = MockModels.getMockOwner();
        Book book = MockModels.getMockBook1();
        storageService.deleteBook(book, aVoid ->
                storageService.retrieveBook(owner, book.getIsbn(), book2 ->
                        storageService.storeBook(book, aVoid2 -> {
                            assertNull(book2);
                            semaphore.release();
                        }, e -> fail("An error occurred while recreating the deleted book.")),
                    e -> fail("An error occurred while retrieving the deleted book.")),
            e -> fail("An error occurred while deleting the book"));
        acquire(semaphore);
    }

    /**
     * Tests retrieving a request.
     */
    @Test
    public void testRetrieveRequest() {
        Semaphore semaphore = new Semaphore(0);
        Book book = MockModels.getMockBook1();
        User requester = MockModels.getMockRequester();
        Request request = MockModels.getMockRequest1();
        storageService.retrieveRequest(book, requester, request2 -> {
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
        Book book = MockModels.getMockBook1();
        User requester = MockModels.getMockRequester();
        Request request = MockModels.getMockRequest1();
        storageService.deleteRequest(request, aVoid ->
                storageService.retrieveRequest(book, requester, request2 ->
                        storageService.storeRequest(request, aVoid2 -> {
                            assertNull(request2);
                            semaphore.release();
                        }, e -> fail("An error occurred while recreating the deleted request.")),
                    e -> fail("An error occurred while retrieving the deleted request.")),
            e -> fail("An error occurred while deleting the request"));
        acquire(semaphore);
    }

    /**
     * Tests retrieving multiple requests by book.
     */
    @Test
    public void testRetrieveMultipleRequestsByBook() {
        Semaphore semaphore = new Semaphore(0);
        Book book = MockModels.getMockBook1();
        Request request = MockModels.getMockRequest1();
        storageService.retrieveRequestsByBook(book, requests -> {
            assertTrue(requests.contains(request));
            semaphore.release();
        }, e -> fail("An error occurred while retrieving the requests by book."));
        acquire(semaphore);
    }

    /**
     * Tests retrieving multiple requests by requester.
     */
    @Test
    public void testRetrieveMultipleRequestsByRequester() {
        Semaphore semaphore = new Semaphore(0);
        User requester = MockModels.getMockRequester();
        Request request1 = MockModels.getMockRequest1();
        Request request2 = MockModels.getMockRequest2();
        storageService.retrieveRequestsByRequester(requester, requests -> {
            assertTrue(requests.contains(request1));
            assertTrue(requests.contains(request2));
            semaphore.release();
        }, e -> fail("An error occurred while retrieving the requests by requester."));
        acquire(semaphore);
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
