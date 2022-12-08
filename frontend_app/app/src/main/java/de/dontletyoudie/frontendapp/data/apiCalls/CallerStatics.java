package de.dontletyoudie.frontendapp.data.apiCalls;

import okhttp3.OkHttpClient;

public class CallerStatics {
    public final static String HOSTIP = "10.0.2.2";
    public final static String APIURL = "http://" + HOSTIP + ":8080/";

    public static OkHttpClient getHttpClient() {
        OkHttpClient client = new OkHttpClient();
        return client;
    }
}
