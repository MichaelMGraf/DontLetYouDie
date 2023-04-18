package de.dontletyoudie.frontendapp.data.apiCalls.core;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import de.dontletyoudie.frontendapp.data.dto.FriendListDto;
import de.dontletyoudie.frontendapp.ui.homepage.fragments.FriendsFragment;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;

public class FetchFriendRequestsAPICaller {


    private final FriendsFragment sourceFragment;

    public FetchFriendRequestsAPICaller(FriendsFragment sourceFragment) {
        this.sourceFragment = sourceFragment;
    }

    public void executeGET(final String requestURL, final String userName) {
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
                    FriendListDto requestList = new ObjectMapper().readValue(responseBody,
                            FriendListDto.class);
                    sourceFragment.fillAdapterFriendRequestsWithList(requestList);
                } catch (IOException e) {
                    //TODO leere Liste zurückgeben, besser ein TextView mit Text
                }
            }
        });
        actionAfterCall.put(HttpsURLConnection.HTTP_NO_CONTENT, new ActionAfterCall() {
            @Override
            public void onSuccessfulCall(String responseBody, Headers headers, Context appContext) {
                sourceFragment.noFriendRequestsYet();
            }
        });

        caller.executeCallWithAuthZ(request, actionAfterCall);
    }
}
