package de.dontletyoudie.frontendapp.data.apiCalls.core;

import android.content.Context;
import android.os.Handler;

import org.intellij.lang.annotations.MagicConstant;

import java.io.IOException;

import okhttp3.Response;

public interface ActionAfterCall {

    @MagicConstant
    int FAIL_CODE = 1000;

    default void onSuccessfulCall(Response response){}

    default void onFail(IOException e, Context appontext){}
}
