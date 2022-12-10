package de.dontletyoudie.frontendapp.data.apiCalls;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.dontletyoudie.frontendapp.data.apiCalls.core.ActionAfterCall;
import de.dontletyoudie.frontendapp.data.apiCalls.core.Caller;
import de.dontletyoudie.frontendapp.data.apiCalls.core.CallerFactory;
import de.dontletyoudie.frontendapp.data.apiCalls.core.TokenEntity;
import de.dontletyoudie.frontendapp.ui.login.LoginActivity;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginAPICaller {
    private final LoginActivity sourceActivity;

    public LoginAPICaller(LoginActivity refToThis) {
        this.sourceActivity = refToThis;
    }

    public void logIn(String usernameOrEmail, String password) {
        executePOST(CallerStatics.APIURL+"login", usernameOrEmail, password);
    }

    public void executePOST(String requestURL, String username, String password) {
        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Request.Builder request = new Request.Builder()
                .url(requestURL)
                .post(requestBody);

        Map<Integer, ActionAfterCall> handlerMap = new HashMap<>();
        handlerMap.put(HttpsURLConnection.HTTP_OK, new ActionAfterCall() {
            @Override
            public void onSuccessfulCall(String responseBody, Headers headers, Context appContext) {
                try {
                    TokenEntity.getTokenFromResponse(responseBody).saveTokens();
                } catch (IOException e) {
                    sourceActivity.showMessage("Something went wrong (+_+)?");
                    return;
                }
                sourceActivity.navigateToMainActivity();
            }
        });
        handlerMap.put(HttpsURLConnection.HTTP_FORBIDDEN, new ActionAfterCall() {
            @Override
            public void onSuccessfulCall(String responseBody, Headers headers, Context appContext) {
                new AlertDialog.Builder(appContext)
                        .setMessage("Password and Username does not match")
                        .setPositiveButton("Ok", null)
                        .show();
            }
        });
        Caller caller = CallerFactory.getCaller(sourceActivity);
        caller.executeCall(request, handlerMap);
    }
}

