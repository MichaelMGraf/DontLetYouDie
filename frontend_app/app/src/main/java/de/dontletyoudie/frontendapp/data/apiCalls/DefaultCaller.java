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

    private final Request.Builder request;
    private final Map<Integer, CallSuccessfulHandler> handler;
    private final boolean withAuthZ;

    public DefaultCaller(Request.Builder request, Map<Integer, CallSuccessfulHandler> handler, boolean withAuthZ) {
        this.request = request;
        this.handler = handler;
        this.withAuthZ = withAuthZ;
    }

    public void executeCall() {
        if (withAuthZ) request.header("Authorization", "Bearer "
                + TokenHolder.getAccessToken());

        OkHttpClient httpClient = CallerStatics.getHttpClient();
        httpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "request failed");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                int responseCode = response.code();

                if (withAuthZ && responseCode == 403) {
                    ErrorMessage errorMessage = new ObjectMapper().readValue(response.body().string(),
                            ErrorMessage.class);
                    if ("Token expired".equals(errorMessage.errorMessage)) {
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
                                    executeCall();
                                }
                            }
                        });
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
}

class ErrorMessage {
    @JsonProperty("error_message")
    String errorMessage;
}
