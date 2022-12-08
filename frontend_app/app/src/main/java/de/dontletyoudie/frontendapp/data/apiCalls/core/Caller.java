package de.dontletyoudie.frontendapp.data.apiCalls.core;

import java.util.Map;

import okhttp3.Request;

public interface Caller {
    void executeCall(Request.Builder request, Map<Integer, ActionAfterCall> handler);

    void executeCallWithAuthZ(Request.Builder request, Map<Integer, ActionAfterCall> handler);
}
