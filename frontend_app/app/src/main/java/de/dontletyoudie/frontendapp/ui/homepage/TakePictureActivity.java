package de.dontletyoudie.frontendapp.ui.homepage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;

import de.dontletyoudie.frontendapp.R;
import de.dontletyoudie.frontendapp.data.GlobalProperties;
import de.dontletyoudie.frontendapp.data.apiCalls.CallerStatics;
import de.dontletyoudie.frontendapp.data.apiCalls.UploadPictureAPICaller;
import okhttp3.HttpUrl;

public class TakePictureActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    Button uploadPictureButton;
    ImageView imageView;
    Spinner categorySpinner;
    EditText commentEditText;

    String category;
    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);

        imageView = findViewById(R.id.image_view);
        uploadPictureButton = findViewById(R.id.upload_image_btn);
        categorySpinner = findViewById(R.id.category_spinner);
        commentEditText = findViewById(R.id.eT_comment);

        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.MANAGE_EXTERNAL_STORAGE/*Manifest.permission.WRITE_EXTERNAL_STORAGE*/};
        requestPermissions(permission, PERMISSION_CODE);

        //button click upload
         uploadPictureButton.setOnClickListener(view -> {
             onClickUploadButton();
        });

        //for category spinner
        //state which string array is to be displayed here
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(this);
    }



    //handling permission result:
    //if permission granted: open camera
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

    // called when image was captured from camera. We just want to set the imageview
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //set the image captured to our Imageview
            imageView.setImageURI(image_uri);
        }
    }



    //because this class is now implementing OnItemSelectedListener
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category = parent.getItemAtPosition(position).toString();
        if(!category.equals("select category")) {
            uploadPictureButton.setEnabled(true);
        } else {
            uploadPictureButton.setEnabled(false);
        }
    }

    //because this class is now implementing OnItemSelectedListener
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        showMessage("please select the fitting category");
    }

    //opens camera. This happens in a new activity, and onActivityResult gets called automatically when done
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

    public void onClickUploadButton(){
        UploadPictureAPICaller uploadPictureAPICaller = new UploadPictureAPICaller(this);
        String username = GlobalProperties.getInstance().userName;
        ContentResolver cR = getApplicationContext().getContentResolver();
        String attemptGetFileName = queryName(cR, image_uri);
        String comment = commentEditText.getText().toString();

        //create a file to write bitmap data
        File f = new File("/storage/emulated/0/Pictures/" + attemptGetFileName);

        uploadPictureAPICaller.executePOST(new HttpUrl.Builder()
                        .host(CallerStatics.HOSTIP)
                        .port(8080)
                        .addPathSegment("api")
                        .addPathSegment("proof")
                        .addPathSegment("add")
                        .addQueryParameter("username", username)
                        .addQueryParameter("comment", comment)
                        .addQueryParameter("category", category)
                        .scheme("http")
                        .build(),
                f);
    }

    public void showMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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
}