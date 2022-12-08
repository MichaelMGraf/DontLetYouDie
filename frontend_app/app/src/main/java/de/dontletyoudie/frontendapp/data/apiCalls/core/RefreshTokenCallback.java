package de.dontletyoudie.frontendapp.data.apiCalls.core;

import android.content.Context;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RefreshTokenCallback extends DefaultCallback {

    public RefreshTokenCallback(Context appContext, boolean withAuthZ, DefaultCaller caller) {
        super(null, appContext, false, caller);
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {

    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        if (response.isSuccessful() && response.code() == HttpsURLConnection.HTTP_OK) {
            TokenEntity entity = new ObjectMapper().readValue(
                    Objects.requireNonNull(response.body()).string(), TokenEntity.class);
            TokenHolder.setAccessToken(entity.access_token);
            TokenHolder.setRefreshToken(entity.refresh_token);
            caller.executeWithToken();
            return;
        }

        //TODO wenn refreshen nicht funktioniert hat und wenn refresh token auch abgelaufen
    }
}
