package com.example.bookmark.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bookmark.R;
import com.example.bookmark.models.Photograph;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static android.os.Environment.getExternalStoragePublicDirectory;

/**
 * Dialog fragment that allows the user to select between choosing an existing image and a taking
 * a new photo with the camera. Redirects the user to the appropriate system utility on selection,
 * and on successful retrieval of an image, the image is passed back through
 * ImageSelectListener.onImageSelect()
 * <p>
 * Any class which shows this dialog fragment should implement ImageSelectListener to receive the
 * resulting image.
 *
 * @author Eric Claerhout.
 */
public class ImageSelectDialogFragment extends DialogFragment {
    private static final int PICK_IMAGE_REQUEST_CODE = 200;
    private static final int PICK_IMAGE_PERMISSIONS_REQUEST_CODE = 201;
    private static final String[] PICK_IMAGE_PERMISSIONS = new String[]{
        Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static final int TAKE_PHOTO_REQUEST_CODE = 300;
    private static final int TAKE_PHOTO_PERMISSIONS_REQUEST_CODE = 301;
    private static final String[] TAKE_PHOTO_PERMISSIONS = new String[]{
        Manifest.permission.CAMERA
    };

    private ImageSelectListener listener;
    private Context context;
    private String TAG;
    private Uri takePhotoUri;


    /**
     * Listener interface for when an image is chosen
     */
    public interface ImageSelectListener {

        /**
         * Fired when an image is selected.
         *
         * @param uri The URI of the selected imaged
         */
        void onImageSelect(Uri uri);

        /**
         * Utility function for converting an image URI to a Photograph object. Note that this
         * function is relatively expensive, so extraneous calls should be avoided.
         *
         * @param uri The URI of the selected imaged
         * @return Photograph object with data from URI
         */
        default Photograph uriToPhotograph(ImageView uri) {
            // TODO: Convert Uri to Bitmap and create Photograph
            // Use https://developer.android.com/reference/android/graphics/BitmapFactory or
            // https://developer.android.com/reference/android/graphics/BitmapRegionDecoder if image
            // size is known. Be wary of out-of-memory when converting.
            return null;
        }
    }


    /**
     * Use this factory method to create a new instance of this fragment.
     *
     * @return A new instance of fragment ImageSelectDialogFragment.
     */
    public static ImageSelectDialogFragment newInstance() {
        return new ImageSelectDialogFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ImageSelectListener) {
            listener = (ImageSelectListener) context;
        } else {
            throw new RuntimeException(context.toString()
                + " must implement ImageSelectListener");
        }
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        TAG = getTag();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View imageSelectView = LayoutInflater.from(context).inflate(R.layout.fragment_image_select_dialog, null);
        Button galleryPhotoButton = imageSelectView.findViewById(R.id.button_gallery_photo);
        Button takePhotoButton = imageSelectView.findViewById(R.id.button_take_photo);

        galleryPhotoButton.setOnClickListener(v -> {
            if (permissionsGranted(PICK_IMAGE_PERMISSIONS)) {
                chooseFromGallery();
            } else {
                requestPermissions(PICK_IMAGE_PERMISSIONS, PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            }
        });

        takePhotoButton.setOnClickListener(v -> {
            if (permissionsGranted(TAKE_PHOTO_PERMISSIONS)) {
                takePhoto();
            } else {
                requestPermissions(TAKE_PHOTO_PERMISSIONS, TAKE_PHOTO_PERMISSIONS_REQUEST_CODE);
            }
        });

        return builder
            .setView(imageSelectView)
            .setTitle(getString(R.string.choose_book_image))
            .setNegativeButton(getString(R.string.cancel), null)
            .create();
    }

    /**
     * Opens the system photo picker
     */
    private void chooseFromGallery() {
        Intent chooseFromGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        try {
            startActivityForResult(chooseFromGallery, PICK_IMAGE_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "No activity found when starting image gallery select activity");
        }
    }

    /**
     * Creates a new file to save a photo into and launches the system camera
     */
    private void takePhoto() {
        File imageFile;
        try {
            imageFile = createImageFile();
        } catch (IOException e) {
            Log.e(TAG, "Error creating image file: " + e);
            return;
        }

        Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoUri = FileProvider.getUriForFile(context,
            "com.example.bookmark.fileprovider", imageFile);
        takePhoto.putExtra(MediaStore.EXTRA_OUTPUT, takePhotoUri);
        try {
            startActivityForResult(takePhoto, TAKE_PHOTO_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "No activity found when starting image capture activity");
        }
    }

    /**
     * Creates a new image file for future saving into.
     *
     * @return A new image file
     */
    private File createImageFile() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String filename = sdf.format(new Date());

        // Use getExternalFilesDir for API<=28 support
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(filename, ".jpg", storageDir);
    }

    /**
     * Checks if the given permissions are granted
     *
     * @param permissions List of permission strings
     * @return True if all permissions are granted, false otherwise
     */
    private boolean permissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (permissionsGranted(PICK_IMAGE_PERMISSIONS)) {
                chooseFromGallery();
            } else {
                Toast.makeText(context, "Permissions not granted, cannot proceed.", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "PICK_IMAGE_PERMISSIONS not granted, not calling chooseFromGallery()");
            }
        } else if (requestCode == TAKE_PHOTO_PERMISSIONS_REQUEST_CODE) {
            if (permissionsGranted(TAKE_PHOTO_PERMISSIONS)) {
                takePhoto();
            } else {
                Toast.makeText(context, "Permissions not granted, cannot proceed.", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "TAKE_PHOTO_PERMISSIONS not granted, not calling takePhoto()");
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == PICK_IMAGE_REQUEST_CODE) {
            listener.onImageSelect(data.getData());
        } else if (requestCode == TAKE_PHOTO_REQUEST_CODE) {
            listener.onImageSelect(takePhotoUri);
        }
        dismiss();
    }
}
