package com.example.bookmark;

import com.example.bookmark.models.User;
import com.example.bookmark.server.FirebaseProvider;

import org.junit.Test;

import java.util.concurrent.Semaphore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class FirebaseProviderTest {
    @Test
    public void testCreateAndGetUser() {
        Semaphore semaphore = new Semaphore(0);
        User user = new User("john.smith42", "John", "Smith", "jsmith@ualberta.ca", "7801234567");
        FirebaseProvider.getInstance().saveUser(user, aVoid -> {
            FirebaseProvider.getInstance().getUserByUsername(user.getUsername(), user2 -> {
                assertEquals(user, user2);
                semaphore.release();
            }, e -> {
                fail("An error occured while getting the user");
            });
        }, e -> {
            fail("An error occured while creating the user");
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
