package de.dontletyoudie.frontendapp.data.apiCalls.callback;

import okhttp3.Response;

public interface CallSuccessfulHandler {

    void onSuccessfulCall(Response response);
}
