package de.dontletyoudie.frontendapp.data.apiCalls.core;

import android.content.Context;

import org.intellij.lang.annotations.MagicConstant;

import java.io.IOException;

import okhttp3.Headers;

public interface ActionAfterCall {

    @MagicConstant
    int FAIL_CODE = 1000;

    default void onSuccessfulCall(String responseBody, Headers headers, Context appContext){}

    default void onFail(IOException e, Context appontext, RetryCaller retryCaller){}
}
