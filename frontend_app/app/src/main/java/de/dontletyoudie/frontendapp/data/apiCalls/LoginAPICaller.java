package de.dontletyoudie.frontendapp.data.apiCalls;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.dontletyoudie.frontendapp.data.apiCalls.callback.CallSuccessfulHandler;
import de.dontletyoudie.frontendapp.ui.login.LoginActivity;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginAPICaller {
    private final LoginActivity sourceActivity;

    public LoginAPICaller(LoginActivity refToThis) {
        this.sourceActivity = refToThis;
    }

    public void logIn(String usernameOrEmail, String password) {
        executePOST(CallerStatics.APIURL+"login", usernameOrEmail, password);
    }

    public void executePOST(String requestURL, String username, String password) {
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        //TODO Do we need this? Isn't used anywhere after, I suspect it's just leftover
        //OkHttpClient client = CallerStatics.getHttpClient();
        Request.Builder request = new Request.Builder()
                .url(requestURL)
                .post(requestBody);

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
    }
}

