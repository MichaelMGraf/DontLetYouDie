package de.dontletyoudie.frontendapp.data.apiCalls;

import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import de.dontletyoudie.frontendapp.data.apiCalls.core.ActionAfterCall;
import de.dontletyoudie.frontendapp.data.apiCalls.core.Caller;
import de.dontletyoudie.frontendapp.data.apiCalls.core.CallerFactory;
import de.dontletyoudie.frontendapp.ui.homepage.fragments.SettingsFragment;
import de.dontletyoudie.frontendapp.ui.login.LoginActivity;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;

public class DeleteAccountAPICaller {
    private final SettingsFragment settingsFragment;

    public DeleteAccountAPICaller(SettingsFragment settingsFragment) {
        this.settingsFragment = settingsFragment;
    }

    public void executeDELETE(final String requestURL, final String username, final Map<Integer,ActionAfterCall> actionAfterCall) {
        HttpUrl URL = Objects.requireNonNull(HttpUrl.parse(requestURL))
                .newBuilder()
                .addQueryParameter("username", username)
                .build();
        Request.Builder request = new Request.Builder()
                .url(URL)
                .delete();
        Caller caller = CallerFactory.getCaller(settingsFragment.getContext());

        caller.executeCallWithAuthZ(request, actionAfterCall);
    }
}
