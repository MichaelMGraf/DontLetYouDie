package de.dontletyoudie.frontendapp.data.apiCalls;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;

import de.dontletyoudie.frontendapp.data.dto.AccountAddDto;
import de.dontletyoudie.frontendapp.ui.registration.RegistrationActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
            executePOST(CallerStatics.APIURL+"api/account/add", JSONString);
        }
        catch (IOException e ) {
            // catch various errors
            e.printStackTrace();
            //TODO gib Nutzer rückmeldung ob erfolgreich
        }
    }

    /**
     * builds and executes a POST-Request and handles the responses
     * @param requestURL URL to send the request to
     * @param requestJSON JSON to send in the body
     * @return TODO hier was sinnvolles ausdenken
     */
    public String executePOST(final String requestURL, final String requestJSON) {
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = CallerStatics.getHttpClient();
        Request request = new Request.Builder()
                .url(requestURL)
                .post(RequestBody.create(requestJSON, MEDIA_TYPE_JSON))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //TODO hier mehr machen
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    if(response.code() == HttpsURLConnection.HTTP_CREATED) {
                        sourceActivity.navigateToMainActivity();
                        //TODO andere Codes abfangen
                    } else {
                        Log.d(TAG, "RESPONSE CODE IST NOT CREATED");
                    }
                } else {
                    Log.d(TAG, "RESPONSE WAS NOT SUCCESSFUL");
                }
            }
        });
        return null;
    }
}
