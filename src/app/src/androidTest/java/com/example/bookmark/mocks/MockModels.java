package com.example.bookmark.mocks;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;

import com.example.bookmark.models.Book;
import com.example.bookmark.models.Geolocation;
import com.example.bookmark.models.Photograph;
import com.example.bookmark.models.Request;
import com.example.bookmark.models.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Mocks users, books, and requests.
 *
 * @author Kyle Hennig.
 */
public class MockModels {
    private static final User mockOwner = new User("john.smith42", "John", "Smith", "jsmith@ualberta.ca", "7801234567");
    private static final User mockRequester = new User("mary.jane9", "Mary", "Jane", "mjane@ualberta.ca", "7809999999");
    private static final Book mockBook1 = new Book(mockOwner, "Code Complete 2", "Steve McConnell", "0-7356-1976-0");
    private static final Book mockBook2 = new Book(mockOwner, "Programming Pearls", "Jon Bentley", "978-0-201-65788-3");
    private static final Book mockBook3 = new Book(mockOwner, "Unedited Title", "Unedited Author", "000000000");
    private static final Geolocation mockLocation = new Geolocation(53.5461, -113.4938);
    private static final Request request1 = new Request(mockBook1, mockRequester, mockLocation);
    private static final Request request2 = new Request(mockBook2, mockRequester, mockLocation);
    private static final Photograph mockPhotograph;

    static {
        // Creates a blue square.
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(50, 50, config);
        for (int x = 0; x < bitmap.getWidth(); ++x) {
            for (int y = 0; y < bitmap.getHeight(); ++y) {
                bitmap.setPixel(x, y, Color.BLUE);
            }
        }
        try {
            // Saves the bitmap to a file.
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, bytes);
            File file = File.createTempFile("blue-square", "jpg");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes.toByteArray());
            fos.flush();
            fos.close();
            // Creates the mock photograph.
            mockPhotograph = new Photograph(Uri.fromFile(file));
            mockBook1.setPhotograph(mockPhotograph);
            mockBook2.setPhotograph(mockPhotograph);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static User getMockOwner() {
        return mockOwner;
    }

    public static User getMockRequester() {
        return mockRequester;
    }

    public static Book getMockBook1() {
        return mockBook1;
    }

    public static Book getMockBook2() {
        return mockBook2;
    }

    public static Book getMockBook3() {
        return mockBook3;
    }

    public static Request getMockRequest1() {
        return request1;
    }

    public static Request getMockRequest2() {
        return request2;
    }

    public static Photograph getMockPhotograph() {
        return mockPhotograph;
    }
}
