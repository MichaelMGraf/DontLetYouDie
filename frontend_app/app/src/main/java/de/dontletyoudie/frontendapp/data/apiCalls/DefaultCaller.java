package de.dontletyoudie.frontendapp.data.apiCalls;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.dontletyoudie.frontendapp.data.apiCalls.callback.CallSuccessfulHandler;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DefaultCaller {

    private Request.Builder request;
    private Map<Integer, CallSuccessfulHandler> handler;
    private boolean withAuthZ;
    private OkHttpClient httpClient;

    public DefaultCaller() {
        httpClient = CallerStatics.getHttpClient();
    }

    public void executeCall(Request.Builder request, Map<Integer, CallSuccessfulHandler> handler) {
        this.request = request;
        this.handler = handler;

        executeCall();

    }

    public void executeCallWithAuthZ(Request.Builder request, Map<Integer, CallSuccessfulHandler> handler) {
        this.request = request;
        this.handler = handler;

        executeWithToken();
    }

    private void executeWithToken() {
        request.header("Authorization", "Bearer " + TokenHolder.getAccessToken());
        executeCall();
        request.removeHeader("Authorization");
    }

    private void executeCall() {
        httpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "request failed");
                //TODO Exception handling
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

                if (handler.containsKey(responseCode)) {
                    CallSuccessfulHandler successfulAPICall = handler.get(responseCode);

                    if (successfulAPICall == null) return;
                    successfulAPICall.onSuccessfulCall(response);

                } else {
                    //TODO ExceptionHandling
                }
            }
        });
    }

    private void refreshTokens() {
        Log.d(TAG, "refreshing token");
        httpClient.newCall(new Request.Builder()
                .url(CallerStatics.APIURL + "login/token/refresh")
                .addHeader("Authorization", "Bearer " + TokenHolder.getRefreshToken())
                .build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //TODO
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(TAG, response.code()+"");
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

class ErrorMessage {
    @JsonProperty("error_message")
    String errorMessage;
}
