package de.dontletyoudie.frontendapp.data.apiCalls.core;

import android.content.Context;

public final class CallerFactory {

    private CallerFactory() {
    }

    public static Caller getCaller(Context appContext) {
        return new DefaultCaller(appContext);
    }
}
