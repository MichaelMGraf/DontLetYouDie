package de.dontletyoudie.frontendapp.data.apiCalls.core;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import java.util.Map;

import de.dontletyoudie.frontendapp.data.apiCalls.CallerStatics;
import okhttp3.OkHttpClient;
import okhttp3.Request;

class DefaultCaller implements Caller {

    private Request.Builder request;
    private Map<Integer, ActionAfterCall> afterCallHandlers;
    private final OkHttpClient httpClient;
    private final Context appContext;
    private boolean withAuthZ = false;

    public DefaultCaller(Context appContext) {
        httpClient = CallerStatics.getHttpClient();
        this.appContext = appContext;
    }


    @Override
    public void executeCall(Request.Builder request, Map<Integer, ActionAfterCall> handler) {
        this.request = request;
        this.afterCallHandlers = handler;

        request.removeHeader("Authorization");
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
    }

    protected void executeCall() {
        httpClient.newCall(request.build())
                .enqueue(new DefaultCallback(afterCallHandlers, appContext, withAuthZ, this));
    }

    protected void refreshTokens() {
        Log.d(TAG, "refreshing token");

        httpClient.newCall(new Request.Builder()
                .url(CallerStatics.APIURL + "login/token/refresh")
                .addHeader("Authorization", "Bearer " + TokenHolder.getRefreshToken())
                .build())
                .enqueue(new RefreshTokenCallback(appContext, this));
    }

    @Override
    public void retryCall() {
        executeCall();
    }
}

