package de.dontletyoudie.frontendapp.data.apiCalls;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import de.dontletyoudie.frontendapp.data.apiCalls.core.ActionAfterCall;
import de.dontletyoudie.frontendapp.data.apiCalls.core.Caller;
import de.dontletyoudie.frontendapp.data.apiCalls.core.CallerFactory;
import de.dontletyoudie.frontendapp.data.dto.FriendListDto;
import de.dontletyoudie.frontendapp.ui.homepage.fragments.HomeFragment;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;

public class FetchStatsAPICaller {

    private final HomeFragment sourceFragment;

    public FetchStatsAPICaller(HomeFragment sourceFragment) {
        this.sourceFragment = sourceFragment;
    }

    public void executeGet(final String requestURL, final String userName) {
        HttpUrl URL = Objects.requireNonNull(HttpUrl.parse(requestURL))
                .newBuilder()
                .addQueryParameter("username", userName)
                .build();
        Request.Builder request = new Request.Builder()
                .url(URL)
                .get();
        Caller caller = CallerFactory.getCaller(sourceFragment.getContext());
        Map<Integer, ActionAfterCall> actionAfterCall = new HashMap<>();
        actionAfterCall.put(HttpsURLConnection.HTTP_OK, new ActionAfterCall() {
            @Override
            public void onSuccessfulCall(String responseBody, Headers headers, Context appContext) {
                try {
                    //converting JSON to Map
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Integer> stats = mapper.readValue(responseBody, Map.class);
                    Log.d("stat", "Stats are: " + stats);
                    sourceFragment.fillAdapterStatsWithData(stats);
                    sourceFragment.changeMiniMeVersion(stats);
                } catch (Exception e) {
                    Log.e("stats", e.getMessage());
                }
            }
        });
        actionAfterCall.put(HttpsURLConnection.HTTP_NO_CONTENT, new ActionAfterCall() {
            @Override
            public void onSuccessfulCall(String responseBody, Headers headers, Context appContext) {
                //TODO does this even occur? I don't think so...
            }
        });

        caller.executeCallWithAuthZ(request, actionAfterCall);
    }
}
