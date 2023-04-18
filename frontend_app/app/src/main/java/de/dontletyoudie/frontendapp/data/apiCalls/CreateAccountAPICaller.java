package de.dontletyoudie.frontendapp.data.apiCalls;

import android.app.AlertDialog;
import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.dontletyoudie.frontendapp.data.apiCalls.core.ActionAfterCall;
import de.dontletyoudie.frontendapp.data.apiCalls.core.Caller;
import de.dontletyoudie.frontendapp.data.apiCalls.core.CallerFactory;
import de.dontletyoudie.frontendapp.data.dto.AccountAddDto;
import de.dontletyoudie.frontendapp.ui.registration.RegistrationActivity;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class CreateAccountAPICaller {

    public static boolean callSuccessful = false;
    private final RegistrationActivity sourceActivity;

    public CreateAccountAPICaller(RegistrationActivity sourceActivity) {
        this.sourceActivity = sourceActivity;
    }

    public void createAccount(String username, String email, String password) {
        AccountAddDto dto = new AccountAddDto(username, email, password);

        ObjectMapper mapper = new ObjectMapper();
        String JSONString;

        try {
            // convert user object to json string
            JSONString = mapper.writeValueAsString(dto);
        }
        catch (IOException e ) {
            new AlertDialog.Builder(sourceActivity)
                    .setMessage("Something went wrong (⊙_⊙)？")
                    .setPositiveButton("Ok", null)
                    .show();
            return;
        }
        executePOST(CallerStatics.APIURL+"api/account/add", JSONString);
    }

    /**
     * builds and executes a POST-Request and handles the responses
     * @param requestURL URL to send the request to
     * @param requestJSON JSON to send in the body
     * @return TODO hier was sinnvolles ausdenken
     */
    public void executePOST(final String requestURL, final String requestJSON) {
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
        Request.Builder request = new Request.Builder()
                .url(requestURL)
                .post(RequestBody.create(requestJSON, MEDIA_TYPE_JSON));
        Caller caller = CallerFactory.getCaller(sourceActivity);
        Map<Integer, ActionAfterCall> actionAfterCall = new HashMap<>();
        actionAfterCall.put(HttpsURLConnection.HTTP_CREATED, new ActionAfterCall() {
            @Override
            public void onSuccessfulCall(String responseBody, Headers headers, Context appContext) {
                sourceActivity.navigateToMainActivity();
            }
        });
        //TODO Feherl behandlung wenn Account mit dem Username schon exisitiert und so

        caller.executeCall(request, actionAfterCall);
    }
}
