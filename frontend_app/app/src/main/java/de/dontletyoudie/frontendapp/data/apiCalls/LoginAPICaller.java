package de.dontletyoudie.frontendapp.data.apiCalls;

import java.util.HashMap;
import java.util.Map;

import de.dontletyoudie.frontendapp.data.apiCalls.core.ActionAfterCall;
import de.dontletyoudie.frontendapp.ui.login.LoginActivity;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class LoginAPICaller {
    private final LoginActivity sourceActivity;

    public LoginAPICaller(LoginActivity refToThis) {
        this.sourceActivity = refToThis;
    }

    public void logIn(String usernameOrEmail, String password) {
        executePOST(CallerStatics.APIURL+"login", usernameOrEmail, password);
    }

    public void executePOST(String requestURL, String username, String password) {
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Request.Builder request = new Request.Builder()
                .url(requestURL)
                .post(requestBody);

        Map<Integer, ActionAfterCall> handlerMap = new HashMap<>();
        //TODO gscheid machen hier so
        /*handlerMap.put(200, new CallSuccessfulHandler() {
            @Override
            public void onSuccessfulCall(Response response) {
                TokenEntity entity = null;
                try {
                    entity = new ObjectMapper().readValue(response.body().string(), TokenEntity.class);
                } catch (IOException e) {
                    e.printStackTrace();
                    //TODO handel Exception
                    return;
                }

                TokenHolder.setAccessToken(entity.access_token);
                TokenHolder.setRefreshToken(entity.refresh_token);

                sourceActivity.navigateToMainActivity();
                Log.d(TAG, "LOGIN SUCCESSFUL");
            }

            @Override
            public void onFail(IOException e) {

            }
        });
        DefaultCaller caller = new DefaultCaller();
        caller.executeCall(request, handlerMap);*/
    }
}

