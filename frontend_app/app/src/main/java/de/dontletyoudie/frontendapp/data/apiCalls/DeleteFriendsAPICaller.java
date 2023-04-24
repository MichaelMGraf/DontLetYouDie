package de.dontletyoudie.frontendapp.data.apiCalls;

import java.util.Map;
import java.util.Objects;

import de.dontletyoudie.frontendapp.data.apiCalls.core.ActionAfterCall;
import de.dontletyoudie.frontendapp.data.apiCalls.core.Caller;
import de.dontletyoudie.frontendapp.data.apiCalls.core.CallerFactory;
import de.dontletyoudie.frontendapp.ui.homepage.AdapterFriends;
import okhttp3.HttpUrl;
import okhttp3.Request;

public class DeleteFriendsAPICaller {

    private final AdapterFriends sourceAdapter;

    public DeleteFriendsAPICaller(AdapterFriends sourceAdapter) {
        this.sourceAdapter = sourceAdapter;
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
        Caller caller = CallerFactory.getCaller(sourceAdapter.getFriendsFragment().getContext());

        caller.executeCallWithAuthZ(request, actionAfterCall);
    }
}
