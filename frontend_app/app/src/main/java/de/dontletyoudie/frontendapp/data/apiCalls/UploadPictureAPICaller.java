package de.dontletyoudie.frontendapp.data.apiCalls;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.net.Uri;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import de.dontletyoudie.frontendapp.data.apiCalls.callback.CallSuccessfulHandler;
import de.dontletyoudie.frontendapp.ui.homepage.TakePicture;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadPictureAPICaller {
    private final TakePicture sourceActivity;

    public UploadPictureAPICaller(TakePicture refToThis) {
        this.sourceActivity = refToThis;
    }

    public void logIn(String usernameOrEmail, String password, File file) {
        executePOST(CallerStatics.APIURL+"login", usernameOrEmail, password, file);
    }

    public void executePOST(String requestURL, String username, String password, File file) {

        try {

            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

            RequestBody req = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("uploaded_file", "filename", RequestBody.create(MEDIA_TYPE_PNG, file))
                    .build();

            Request request = new Request.Builder()
                    .url("url")
                    .post(req)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();

            Log.d("response", "uploadImage:" + response.body().string());

            //return new JSONObject(response.body().string());

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e(TAG, "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e(TAG, "Other Error: " + e.getLocalizedMessage());
        }

        Map<Integer, CallSuccessfulHandler> handlerMap = new HashMap<>();
        handlerMap.put(200, response -> {
            TokenEntity entity;
            try {
                entity = new ObjectMapper().readValue(response.body().string(), TokenEntity.class);
            } catch (IOException e) {
                e.printStackTrace();
                //TODO handel Exception
                return;
            }

            TokenHolder.setAccessToken(entity.access_token);
            TokenHolder.setRefreshToken(entity.refresh_token);

            sourceActivity.navigateToMainActivity();
            Log.d(TAG, "LOGIN SUCCESSFUL");
        });

    }
}
