package de.dontletyoudie.frontendapp.data.apiCalls.core;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.dontletyoudie.frontendapp.data.apiCalls.CallerStatics;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class DefaultCaller implements Caller {

    private Request.Builder request;
    private Map<Integer, ActionAfterCall> afterCallHandlers;
    private OkHttpClient httpClient;
    private Context appContext;
    private boolean withAuthZ = false;

    public DefaultCaller(Context appContext) {
        httpClient = CallerStatics.getHttpClient();
        this.appContext = appContext;
    }


    @Override
    public void executeCall(Request.Builder request, Map<Integer, ActionAfterCall> handler) {
        this.request = request;
        this.afterCallHandlers = handler;

        withAuthZ = false;

        executeCall();
    }

    @Override
    public void executeCallWithAuthZ(Request.Builder request, Map<Integer, ActionAfterCall> handler) {
        this.request = request;
        this.afterCallHandlers = handler;

        withAuthZ = true;

        executeWithToken();
    }

    protected void executeWithToken() {
        request.header("Authorization", "Bearer " + TokenHolder.getAccessToken());
        executeCall();
        request.removeHeader("Authorization");
    }

    private void executeCall() {
        httpClient.newCall(request.build()).enqueue(new Callback() {
            //TODO mit defaultCallback ersetzen
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "request failed");

                ActionAfterCall actionAfterCall = afterCallHandlers.get(ActionAfterCall.FAIL_CODE);
                if (actionAfterCall == null)
                    new Handler(Looper.getMainLooper()).post(() ->
                            new AlertDialog.Builder(appContext)
                                    .setMessage("Something went wrong\n\n" +
                                            "Reasons could be:\n" +
                                            "Your connection does not work.\n" +
                                            "Or our Servers are down :(")
                                    .setPositiveButton("Ok", null)
                                    .show());

                else
                    new Handler(Looper.getMainLooper()).post(() ->
                            actionAfterCall.onFail(e, appContext));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                int responseCode = response.code();

                if (withAuthZ && responseCode == HttpsURLConnection.HTTP_FORBIDDEN) {
                    ErrorMessage errorMessage = new ObjectMapper().readValue(response.body().string(),
                            ErrorMessage.class);
                    if ("Token expired".equals(errorMessage.errorMessage)) {
                        refreshTokens();
                        return;
                    } else {
                        //TODO ExceptionHandling
                    }
                }

                if (afterCallHandlers.containsKey(responseCode)) {
                    ActionAfterCall successfulAPICall = afterCallHandlers.get(responseCode);

                    if (successfulAPICall == null) return;
                    successfulAPICall.onSuccessfulCall(response);

                } else {
                    //TODO ExceptionHandling
                    //handler.get(1000).onSuccessfulCall();
                }
            }
        });
    }

    void refreshTokens() {
        Log.d(TAG, "refreshing token");

        httpClient.newCall(new Request.Builder()
                .url(CallerStatics.APIURL + "login/token/refresh")
                .addHeader("Authorization", "Bearer " + TokenHolder.getRefreshToken())
                .build()).enqueue(new Callback() {
                    //TODO mit refrshCallback ersetzen
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //TODO
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(TAG, response.code() + "");
                if (response.isSuccessful() && response.code() == HttpsURLConnection.HTTP_OK) {
                    TokenEntity entity = new ObjectMapper().readValue(response.body().string(), TokenEntity.class);
                    TokenHolder.setAccessToken(entity.access_token);
                    TokenHolder.setRefreshToken(entity.refresh_token);
                    executeWithToken();
                }
            }
        });
    }
}

