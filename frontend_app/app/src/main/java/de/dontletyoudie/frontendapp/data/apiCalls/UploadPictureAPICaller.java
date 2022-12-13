package de.dontletyoudie.frontendapp.data.apiCalls;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.dontletyoudie.frontendapp.data.apiCalls.core.ActionAfterCall;
import de.dontletyoudie.frontendapp.data.apiCalls.core.Caller;
import de.dontletyoudie.frontendapp.data.apiCalls.core.CallerFactory;
import de.dontletyoudie.frontendapp.ui.homepage.TakePictureActivity;
import okhttp3.Headers;
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

            RequestBody req = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("uploaded_file", file.getName(), RequestBody.create(file, MEDIA_TYPE_PNG))
                    .build();

            Request.Builder request = new Request.Builder()
                    .url(requestURL)
                    .post(req);

            Map<Integer, ActionAfterCall> handlerMap = new HashMap<>();
            handlerMap.put(HttpsURLConnection.HTTP_OK, new ActionAfterCall() {
                @Override
                public void onSuccessfulCall(String responseBody, Headers headers, Context appContext) {
                    sourceActivity.navigateToMainActivity();
                }
            });

            Caller caller = CallerFactory.getCaller(sourceActivity);
            caller.executeCall(request, handlerMap);

        } catch (Exception e) {
            Log.e(TAG, "Other Error: " + e.getLocalizedMessage());
        }
    }
}
