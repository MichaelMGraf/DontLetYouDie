package de.dontletyoudie.frontendapp.data.apiCalls.core;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.dontletyoudie.frontendapp.ui.homepage.AdapterFriends;
import de.dontletyoudie.frontendapp.ui.homepage.AddFriendsActivity;
import de.dontletyoudie.frontendapp.ui.homepage.fragments.FriendsFragment;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class FetchFriendsAPICaller {

    private final FriendsFragment sourceFragment;

    public FetchFriendsAPICaller(FriendsFragment sourceFragment) {
        this.sourceFragment = sourceFragment;
    }

    public void executeGET(final String requestURL, final String userName) {
        Request.Builder request = new Request.Builder()
                .url(requestURL)
                .get();
        Caller caller = CallerFactory.getCaller(sourceFragment.getContext());
        Map<Integer, ActionAfterCall> actionAfterCall = new HashMap<>();
        actionAfterCall.put(HttpsURLConnection.HTTP_CREATED, new ActionAfterCall() {
        });
        //TODO Fehlerbehandlung wenn Account mit dem Username schon exisitiert und sowas

        caller.executeCallWithAuthZ(request, actionAfterCall);
    }
}
