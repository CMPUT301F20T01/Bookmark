package com.example.bookmark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * This activity opens up a camera view using CameraX and allows a
 * user to scan a barcode using Google's ML kit. Upon success
 * the isbn will be returned.
 *
 * TODO: Add more to these? Classes/Listeners? More about libraries used?
 * @author Mitch Adam.
 */
public class ScanIsbnActivity extends AppCompatActivity {
    // TODO: Figure out the back navigation

    private Executor executor = Executors.newSingleThreadExecutor();
    private static final int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA"};

    PreviewView mPreviewView;
    BarcodeScanner scanner;

    // Used to compare barcode readings to increase accuracy
    int numRequiredSameBarcodes = 5;
    int barcodesIndex = 0;
    String[] barcodes = new String[numRequiredSameBarcodes - 1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_isbn);
        getSupportActionBar().setTitle("Scan ISBN");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPreviewView = findViewById(R.id.viewFinder);

        BarcodeScannerOptions options =
            new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    Barcode.FORMAT_EAN_13, // Format of ISBN barcode's
                    Barcode.FORMAT_EAN_8)
                .build();
        scanner = BarcodeScanning.getClient();

        if(allPermissionsGranted()){
            startCamera(); //start camera if permission has been granted by user
        } else {
            Log.d("PHOTO", "Should request permissions");
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    private void startCamera() {

        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {

                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);

                } catch (ExecutionException | InterruptedException e) {
                    // No errors need to be handled for this Future.
                    // This should never be reached.
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

        cameraProvider.unbindAll();

        Preview preview = new Preview.Builder()
            .setTargetName("viewFinder")
            .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build();

        ImageAnalysis imageAnalysis =
            new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(executor, new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy imageProxy) {
                analyzeBarcode(imageProxy);
            }
        });

        try{
            Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
            preview.setSurfaceProvider(mPreviewView.createSurfaceProvider());
        } catch(Exception e){
            Log.e("error", String.valueOf(e));
        }
    }

    private void analyzeBarcode(ImageProxy imageProxy) {
        @SuppressLint("UnsafeExperimentalUsageError") Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            InputImage image =
                InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());

            // Pass image to an ML Kit Vision API
            Task<List<Barcode>> result = scanner.process(image)
                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                    @Override
                    public void onSuccess(List<Barcode> barcodes) {
                        // Task completed successfully
                        for (Barcode barcode: barcodes) {
                            Rect bounds = barcode.getBoundingBox();
                            Point[] corners = barcode.getCornerPoints();

                            String rawValue = barcode.getRawValue();

                            addBarcode(rawValue);
                        }
                        imageProxy.close();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Task failed with an exception
                        Log.d("Barcode", "The analyzer did not work");
                        Log.d("Barcode", String.valueOf(e));
                        imageProxy.close();
                    }
                });
        }
    }

    private void addBarcode(String barcode) {

        // Check if barcode is the same as all current barcodes
        for (int i = 0; i < barcodesIndex; i++) {
            if (!barcodes[i].equals(barcode)) {
                barcodesIndex = 0;
                barcodes[barcodesIndex] = barcode;
                return;
            }
        }

        // Check if it is that last required
        if (barcodesIndex == (numRequiredSameBarcodes - 1)) {
            // Return barcode
            Intent intent = new Intent();
            intent.putExtra("ISBN",barcode);
            setResult(1,intent);
            finish(); //finishing activity

        } else {
            // Add to list
            barcodes[barcodesIndex] = barcode;
            barcodesIndex += 1;
        }
    }

    private boolean allPermissionsGranted(){

        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(allPermissionsGranted()){
                startCamera();
            } else{
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }
}
