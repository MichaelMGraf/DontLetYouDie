package de.dontletyoudie.frontendapp.data.apiCalls;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;

import de.dontletyoudie.frontendapp.ui.login.LoginActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.FormBody;

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
        OkHttpClient client = CallerStatics.getHttpClient();
        Request request = new Request.Builder()
                .url(requestURL)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                throw new LoginFailedException("Login failed, try again later");
                //todo exception fangen und handlen
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    if(response.code() == HttpsURLConnection.HTTP_OK) {
                        sourceActivity.navigateToMainActivity();
                        Log.d(TAG, "LOGIN SUCCESSFUL");
                        Log.d(TAG, response.body().string());
                        //TODO Tokens speichern
                        //TODO andere Codes abfangen
                    } else {
                        Log.d(TAG, "RESPONSE CODE IST NOT CREATED");
                    }
                } else {
                    Log.d(TAG, "RESPONSE WAS NOT SUCCESSFUL");
                }
            }
        });
    }


}
