package de.dontletyoudie.frontendapp.data.apiCalls;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import de.dontletyoudie.frontendapp.data.apiCalls.core.ActionAfterCall;
import de.dontletyoudie.frontendapp.data.apiCalls.core.Caller;
import de.dontletyoudie.frontendapp.data.apiCalls.core.CallerFactory;
import de.dontletyoudie.frontendapp.data.dto.FriendDto;
import de.dontletyoudie.frontendapp.data.dto.FriendListDto;
import de.dontletyoudie.frontendapp.ui.homepage.AdapterFriends;
import de.dontletyoudie.frontendapp.ui.homepage.AddFriendsActivity;
import de.dontletyoudie.frontendapp.ui.homepage.fragments.FriendsFragment;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FetchFriendsAPICaller {

    private final FriendsFragment sourceFragment;

    public FetchFriendsAPICaller(FriendsFragment sourceFragment) {
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
                    FriendListDto friendList = new ObjectMapper().readValue(responseBody,
                            FriendListDto.class);
                    sourceFragment.fillAdapterFriendsWithList(friendList);
                } catch (IOException e) {
                    //TODO leere Liste zurückgeben, besser ein TextView mit Text
                }
            }
        });
        actionAfterCall.put(HttpsURLConnection.HTTP_NO_CONTENT, new ActionAfterCall() {
            @Override
            public void onSuccessfulCall(String responseBody, Headers headers, Context appContext) {
                sourceFragment.noFriendsYet();
            }
        });

        caller.executeCallWithAuthZ(request, actionAfterCall);
    }
}
