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
import de.dontletyoudie.frontendapp.data.dto.ProofGetDto;
import de.dontletyoudie.frontendapp.ui.homepage.fragments.FriendsFragment;
import de.dontletyoudie.frontendapp.ui.homepage.fragments.JudgeFragment;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;

public class GetProofAPICaller {
    private final JudgeFragment sourceFragment;

    public GetProofAPICaller(JudgeFragment sourceFragment) {
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
                    Log.d("proof", responseBody);
                    ProofGetDto proof = new ObjectMapper().readValue(responseBody,
                            ProofGetDto.class);
                    sourceFragment.displayProof(proof);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        actionAfterCall.put(HttpsURLConnection.HTTP_NO_CONTENT, new ActionAfterCall() {
            @Override
            public void onSuccessfulCall(String responseBody, Headers headers, Context appContext) {
                sourceFragment.noProofFound();
            }
        });

        caller.executeCallWithAuthZ(request, actionAfterCall);
    }
}
