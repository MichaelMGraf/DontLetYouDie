package de.dontletyoudie.frontendapp.data.apiCalls;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.dontletyoudie.frontendapp.data.apiCalls.callback.CallSuccessfulHandler;
import de.dontletyoudie.frontendapp.ui.homepage.TakePictureActivity;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class UploadPictureAPICaller {
    private final TakePictureActivity sourceActivity;

    public UploadPictureAPICaller(TakePictureActivity refToThis) {
        this.sourceActivity = refToThis;
    }

    public void executePOST(HttpUrl requestURL, File file) {

        Log.d("url: ", requestURL.toString());

        try {

            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

            RequestBody req = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("uploaded_file", "filename", RequestBody.create(MEDIA_TYPE_PNG, file))
                    .build();

            Request.Builder request = new Request.Builder()
                    .url(requestURL)
                    .post(req);

            Map<Integer, CallSuccessfulHandler> handlerMap = new HashMap<>();
            handlerMap.put(200, response -> {
                TokenEntity entity;

                try {
                    entity = new ObjectMapper().readValue(response.body().string(), TokenEntity.class);
                } catch (IOException e) {
                    e.printStackTrace();
                    //TODO handle Exception
                    return;
                }

                TokenHolder.setAccessToken(entity.access_token);
                TokenHolder.setRefreshToken(entity.refresh_token);

                sourceActivity.navigateToMainActivity();
                Log.d(TAG, "LOGIN SUCCESSFUL");
            });

            DefaultCaller caller = new DefaultCaller();
            caller.executeCall(request, handlerMap);

        } catch (Exception e) {
            Log.e(TAG, "Other Error: " + e.getLocalizedMessage());
        }
    }
}
