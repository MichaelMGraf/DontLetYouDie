package de.dontletyoudie.frontendapp.data.apiCalls;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import de.dontletyoudie.frontendapp.data.GlobalProperties;
import de.dontletyoudie.frontendapp.data.apiCalls.core.ActionAfterCall;
import de.dontletyoudie.frontendapp.data.apiCalls.core.Caller;
import de.dontletyoudie.frontendapp.data.apiCalls.core.CallerFactory;
import de.dontletyoudie.frontendapp.ui.homepage.fragments.JudgeFragment;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class JudgeProofAPICaller {

    private final JudgeFragment sourceFragment;

    public JudgeProofAPICaller(JudgeFragment sourceFragment) {
        this.sourceFragment = sourceFragment;
    }

    public void executePOST(final String requestURL, final String judgeName, final Long proofId, final Boolean approved) {
        HttpUrl URL = Objects.requireNonNull(HttpUrl.parse(requestURL))
                .newBuilder()
                .addQueryParameter("judgeName", judgeName)
                .addQueryParameter("proofId", String.valueOf(proofId))
                .addQueryParameter("approved", String.valueOf(approved))
                .build();
        Request.Builder request = new Request.Builder()
                .url(URL)
                .post(RequestBody.create("", MediaType.parse("application/json; charset=utf-8")));
        Caller caller = CallerFactory.getCaller(sourceFragment.getContext());
        Map<Integer, ActionAfterCall> actionAfterCall = new HashMap<>();
        actionAfterCall.put(HttpsURLConnection.HTTP_CREATED, new ActionAfterCall() {
            @Override
            public void onSuccessfulCall(String responseBody, Headers headers, Context appContext) {
                sourceFragment.showMessage("Successfully judged proof");
                sourceFragment.getGetProofAPICaller().executeGET(CallerStatics.APIURL+"api/proof/getPending", GlobalProperties.getInstance().userName);
            }
        });

        caller.executeCallWithAuthZ(request, actionAfterCall);
    }
}
