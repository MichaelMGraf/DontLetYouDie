package de.dontletyoudie.frontendapp.data.apiCalls;

import android.app.AlertDialog;
import android.content.Context;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.dontletyoudie.frontendapp.data.GlobalProperties;
import de.dontletyoudie.frontendapp.data.apiCalls.core.ActionAfterCall;
import de.dontletyoudie.frontendapp.data.apiCalls.core.Caller;
import de.dontletyoudie.frontendapp.data.apiCalls.core.CallerFactory;
import de.dontletyoudie.frontendapp.data.apiCalls.core.TokenEntity;
import de.dontletyoudie.frontendapp.ui.login.LoginActivity;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

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
                GlobalProperties.getInstance().userName = username;
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

