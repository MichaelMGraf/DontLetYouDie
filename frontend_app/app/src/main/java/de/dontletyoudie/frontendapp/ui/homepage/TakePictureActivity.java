package de.dontletyoudie.frontendapp.ui.homepage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.dontletyoudie.frontendapp.R;
import de.dontletyoudie.frontendapp.data.GlobalProperties;
import de.dontletyoudie.frontendapp.data.apiCalls.CallerStatics;
import de.dontletyoudie.frontendapp.data.apiCalls.UploadPictureAPICaller;
import okhttp3.HttpUrl;

public class TakePictureActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    Button takePhotoButton;
    ImageView imageView;
    Spinner categorySpinner;

    String category;
    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);

        imageView = findViewById(R.id.image_view);
        takePhotoButton = findViewById(R.id.capture_image_btn);
        categorySpinner = findViewById(R.id.category_spinner);

        //button click
        takePhotoButton.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                        //checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                        !Environment.isExternalStorageManager() || true
                ) {
                    //permission not enabled, request it
                    String[] permission = {Manifest.permission.CAMERA, Manifest.permission.MANAGE_EXTERNAL_STORAGE/*Manifest.permission.WRITE_EXTERNAL_STORAGE*/};
                    requestPermissions(permission, PERMISSION_CODE);
                    //show popup to request permissions
                } else {
                    //permission already granted
                    openCamera();
                }
            }  // system os < marshmallow

        });

        //for category spinner
        //state wich string array is to be displayed here
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(this);
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);

    }

    //handling permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //this method is called, when user presses Allow or Deny from Permission Request Popup
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission from popup was granted
                openCamera();
            } else {
                //permission from popup was denied
                Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // called when image was captured from camera

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //set the image captured to our Imageview
            imageView.setImageURI(image_uri);
            UploadPictureAPICaller uploadPictureAPICaller = new UploadPictureAPICaller(this);
            String username = GlobalProperties.getInstance().userName;

            ContentResolver cR = getApplicationContext().getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String type = mime.getExtensionFromMimeType(cR.getType(image_uri));
            String[] segments = image_uri.toString().split("/");

            String attemptGetFileName = queryName(cR, image_uri);
            Log.d("maybefilename", attemptGetFileName);

            //TODO do we really need this logs right here?
            Log.d("type at \"image_uri\": ", type);
            Log.d("Path for \"image_uri\": ", image_uri.toString());
            Log.d("Path for \"image_uri\": ", image_uri.getPath());
            Log.d("EnvExternalStorage", Environment.getExternalStorageDirectory().getAbsolutePath());
            Log.d("AttemptToConcat", "content://media/" + image_uri.getPath());
            Log.d("Size: ", Integer.toString(segments.length));
            Log.d("AttemptToConcat2", "/storage/emulated/0/Pictures/" + attemptGetFileName);
            // Bilder sind auf Emulator in:
            // /storage/emulated/0/Pictures

            //create a file to write bitmap data
            File f = new File("/storage/emulated/0/Pictures/" + attemptGetFileName);
//            try {
//                f.createNewFile();
//
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            photo.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
//            byte[] bitmapdata = bos.toByteArray();
//
//            Log.d("tag", "gothere");
//
//            //write the bytes in file
//            FileOutputStream fos = new FileOutputStream(f);
//            fos.write(bitmapdata);
//            fos.flush();
//            fos.close();
//            } catch (IOException e) {
//                //TODO Handle Exception?
//                e.printStackTrace();
//            }


            uploadPictureAPICaller.executePOST(new HttpUrl.Builder()
                            .host(CallerStatics.HOSTIP)
                            .port(8080)
                            .addPathSegment("api")
                            .addPathSegment("proof")
                            .addPathSegment("add")
                            .addQueryParameter("username", username)
                            //TODO Implement querying for comment after taking a picture and replace this or replace comment everywhere
                            .addQueryParameter("comment", "exampleComment")
                            .addQueryParameter("category", category)
                            .scheme("http")
                            .build(),
                    f);
        }
    }

    public void navigateToMainActivity() {
        //TODO lösche Activity Verlauf (back button nicht mehr auf Anmelde-Fenster)
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }

    public void showMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    //because this class is now implementing OnItemSelectedListener
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category = parent.getItemAtPosition(position).toString();
        if(!category.equals("select category")) {
            takePhotoButton.setEnabled(true);
        } else {
            takePhotoButton.setEnabled(false);
        }
    }

    //because this class is now implementing OnItemSelectedListener
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        showMessage("please select the fitting category");
    }
}