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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;

class DefaultCallback implements Callback {

    private final Map<Integer, ActionAfterCall> afertCallHandlers;
    protected Context appContext;
    private final boolean withAuthZ;
    protected DefaultCaller caller;

    public DefaultCallback(Map<Integer, ActionAfterCall> afterCallHandlers, Context appContext,
                           boolean withAuthZ, DefaultCaller caller) {
        Log.d("bra", "DeafultCallback erstellt");
        if (withAuthZ && caller == null)
            throw new NullPointerException("Caller should not be null if the call is with authZ");
        this.afertCallHandlers = afterCallHandlers == null ? new HashMap<>() : afterCallHandlers;
        this.appContext = appContext;
        this.withAuthZ = withAuthZ;
        this.caller = caller;
    }


    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        Log.d(TAG, "request failed");

        ActionAfterCall actionAfterCall = afertCallHandlers.get(ActionAfterCall.FAIL_CODE);
        if (actionAfterCall == null) alert(
                "Something went wrong\n\n" +
                        "Reasons could be:\n" +
                        "Your connection does not work.\n" +
                        "Or our Servers are down :(", "Retry");
        else
            new Handler(Looper.getMainLooper()).post(() ->
                    actionAfterCall.onFail(e, appContext, caller));
    }

    protected void alert(String message) {
        alert(message, "ok");
    }

    protected void alert(String message, String buttonText) {
        new Handler(Looper.getMainLooper()).post(() ->
                new AlertDialog.Builder(appContext)
                        .setMessage(message)
                        .setPositiveButton(buttonText, (dialogInterface, i) -> caller.executeCall())
                        .show());
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        Log.d("response", response.code() + " " /*+ response.body().string()*/);
        Log.d("withAuthZ", withAuthZ + "");
        Headers headers = response.headers();
        String responseBody = Objects.requireNonNull(response.body()).string();

        int responseCode = response.code();
        Log.d("if", (withAuthZ && responseCode == HttpsURLConnection.HTTP_FORBIDDEN) + "");

        if (withAuthZ && responseCode == HttpsURLConnection.HTTP_FORBIDDEN) {
            ErrorMessage errorMessage = new ObjectMapper().readValue(
                    responseBody, ErrorMessage.class);
            Log.d("errorMessage", errorMessage.errorMessage);
            if ("Token expired".equals(errorMessage.errorMessage)) {
                caller.refreshTokens();
                return;
            }
        }
        if (afertCallHandlers.containsKey(responseCode)) {
            ActionAfterCall afterCall = afertCallHandlers.get(responseCode);

            if (afterCall == null) return;
            new Handler(Looper.getMainLooper()).post(() ->
                    afterCall.onSuccessfulCall(responseBody, headers, appContext));

        } else {
            alert("Something went wrong\n\n You got a " + response + " Response");

        }
    }
}

