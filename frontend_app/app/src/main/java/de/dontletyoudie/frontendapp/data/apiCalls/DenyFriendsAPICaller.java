package de.dontletyoudie.frontendapp.data.apiCalls;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import de.dontletyoudie.frontendapp.data.apiCalls.core.ActionAfterCall;
import de.dontletyoudie.frontendapp.data.apiCalls.core.Caller;
import de.dontletyoudie.frontendapp.data.apiCalls.core.CallerFactory;
import de.dontletyoudie.frontendapp.ui.homepage.AdapterFriendRequests;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class DenyFriendsAPICaller {

    private final AdapterFriendRequests sourceAdapter;

    public DenyFriendsAPICaller(AdapterFriendRequests sourceActivity) {
        this.sourceAdapter = sourceActivity;
    }

    public void executeDELETE(final String requestURL, final String srcName, final String relName, final Map<Integer,ActionAfterCall> actionAfterCall) {
        HttpUrl URL = Objects.requireNonNull(HttpUrl.parse(requestURL))
                .newBuilder()
                .addQueryParameter("srcAccount", srcName)
                .addQueryParameter("relAccount", relName)
                .build();
        Request.Builder request = new Request.Builder()
                .url(URL)
                .delete();
        Caller caller = CallerFactory.getCaller(sourceAdapter.getContext());

        caller.executeCallWithAuthZ(request, actionAfterCall);
    }
}
