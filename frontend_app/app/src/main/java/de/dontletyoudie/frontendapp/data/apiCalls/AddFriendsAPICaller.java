package de.dontletyoudie.frontendapp.data.apiCalls;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.dontletyoudie.frontendapp.data.apiCalls.core.ActionAfterCall;
import de.dontletyoudie.frontendapp.data.apiCalls.core.Caller;
import de.dontletyoudie.frontendapp.data.apiCalls.core.CallerFactory;
import de.dontletyoudie.frontendapp.data.dto.AccountAddDto;
import de.dontletyoudie.frontendapp.data.dto.RelationshipAddDto;
import de.dontletyoudie.frontendapp.ui.homepage.AddFriendsActivity;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class AddFriendsAPICaller {
    private final AddFriendsActivity sourceActivity;

    public AddFriendsAPICaller(AddFriendsActivity sourceActivity) {
        this.sourceActivity = sourceActivity;
    }

    public void createRelationship(String srcUsername, String relUsername) {
        Log.d(null, "srcUsername: " + srcUsername);
        Log.d(null, "relUsername: " + relUsername);
        RelationshipAddDto dto = new RelationshipAddDto(srcUsername, relUsername);

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
        executePOST(CallerStatics.APIURL+"api/relationship/add", JSONString);
    }

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
                sourceActivity.showMessage("Successfully added Friend");
            }
        });
        actionAfterCall.put(HttpsURLConnection.HTTP_BAD_REQUEST, new ActionAfterCall() {
            @Override
            public void onSuccessfulCall(String responseBody, Headers headers, Context appContext) {
                sourceActivity.showMessage("Dieser Account konnte leider nicht gefunden werden.");
            }
        });
        //TODO Fehlerbehandlung wenn Account mit dem Username schon exisitiert und sowas

        caller.executeCallWithAuthZ(request, actionAfterCall);
    }
}
