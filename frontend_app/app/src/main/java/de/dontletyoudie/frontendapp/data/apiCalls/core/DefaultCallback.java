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
import okhttp3.Response;

class DefaultCallback implements Callback {

    private Map<Integer, ActionAfterCall> afertCallHandlers;
    private Context appContext;
    private boolean withAuthZ = false;
    protected DefaultCaller caller;

    public DefaultCallback(Map<Integer, ActionAfterCall> afterCallHandlers, Context appContext,
                           boolean withAuthZ, DefaultCaller caller) {
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
                        "Or our Servers are down :(");
        else
            new Handler(Looper.getMainLooper()).post(() -> actionAfterCall.onFail(e, appContext));
    }

    private void alert(String message) {
        new Handler(Looper.getMainLooper()).post(() ->
                new AlertDialog.Builder(appContext)
                        .setMessage(message)
                        .setPositiveButton("Ok", null)
                        .show());
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        if (!response.isSuccessful()) {
            alert("Something went wrong\n\n" +
                    "The Response was not successful but did not fail ¯\\(°_o)/¯");
            return;
        }

        int responseCode = response.code();

        if (withAuthZ && responseCode == HttpsURLConnection.HTTP_FORBIDDEN) {
            ErrorMessage errorMessage = new ObjectMapper().readValue(
                    Objects.requireNonNull(response.body()).string(), ErrorMessage.class);
            if ("Token expired".equals(errorMessage.errorMessage)) {
                caller.refreshTokens();
                return;
            }

            if (afertCallHandlers.containsKey(responseCode)) {
                ActionAfterCall successfulAPICall = afertCallHandlers.get(responseCode);

                if (successfulAPICall == null) return;
                successfulAPICall.onSuccessfulCall(response);

            } else {
                alert("Something went wrong\n\n You got a " + response + " Response");
            }
        }
    }
}

