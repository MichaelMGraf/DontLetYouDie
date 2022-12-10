package de.dontletyoudie.frontendapp.data.apiCalls.core;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import de.dontletyoudie.frontendapp.data.GloablStuff;
import de.dontletyoudie.frontendapp.data.apiCalls.CallerStatics;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RefreshTokenCallback extends DefaultCallback {

    public RefreshTokenCallback(Context appContext, DefaultCaller caller) {
        super(null, appContext, false, caller);
        Log.d("bra", "REfesh crerated");
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        if (response.code() == HttpsURLConnection.HTTP_OK) {
            TokenEntity.getTokenFromResponse(Objects.requireNonNull(response.body()).string()).saveTokens();
            caller.executeWithToken();
            return;
        }

        if (response.code() == HttpsURLConnection.HTTP_FORBIDDEN) {
            ErrorMessage message = new ObjectMapper().readValue(
                    Objects.requireNonNull(response.body()).string(), ErrorMessage.class);
            if ("Token expired".equals(message.errorMessage)) {
                new Handler(Looper.getMainLooper()).post(this::alertReenterPassword);
                return;
            }
        }

        alert("Something went wrong\n\n You got a " + response + " Response");
    }

    private void alertReenterPassword() {
        EditText textfield = new EditText(appContext);
        textfield.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        new AlertDialog.Builder(appContext)
                .setMessage("Please login again\n\nUsername:" + GloablStuff.username)
                .setView(textfield)
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("username", GloablStuff.username)
                            .add("password", textfield.getText().toString())
                            .build();

                    Request.Builder request = new Request.Builder()
                            .url(CallerStatics.APIURL + "login")
                            .post(requestBody);

                    Map<Integer, ActionAfterCall> actionAfterCall = new HashMap<>();
                    actionAfterCall.put(HttpsURLConnection.HTTP_OK, new ActionAfterCall() {
                        @Override
                        public void onSuccessfulCall(String responseBody, Headers headers, Context appContext) {
                            try {
                                TokenEntity.getTokenFromResponse(responseBody).saveTokens();
                            } catch (IOException e) {
                                Toast.makeText(appContext,
                                        "Something went wrong (+_+)?",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                            caller.retryCall();
                        }
                    });
                    actionAfterCall.put(HttpsURLConnection.HTTP_FORBIDDEN, new ActionAfterCall() {
                        @Override
                        public void onSuccessfulCall(String responseBody, Headers headers, Context appContext) {
                            new AlertDialog.Builder(appContext)
                                    .setMessage("Password and Username does not match")
                                    .setPositiveButton("Ok", (dialogInterface1, i1) -> alertReenterPassword())
                                    .show();
                        }
                    });

                    Caller c = CallerFactory.getCaller(appContext);
                    c.executeCall(request, actionAfterCall);
                })
                .show();
    }
}
