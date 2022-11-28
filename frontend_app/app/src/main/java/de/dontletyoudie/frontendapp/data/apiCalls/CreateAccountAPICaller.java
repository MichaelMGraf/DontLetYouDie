package de.dontletyoudie.frontendapp.data.apiCalls;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.NoSuchElementException;

import de.dontletyoudie.frontendapp.data.dto.AccountAddDto;
import de.dontletyoudie.frontendapp.ui.registration.RegistrationActivity;

public class CreateAccountAPICaller {

    public void createAccount(String username, String email, String password)
            throws CreateAccountFailedException {
        AccountAddDto dto = new AccountAddDto(username, email, password);

        ObjectMapper mapper = new ObjectMapper();
        String JSONString;

        try {
            // convert user object to json string
            JSONString = mapper.writeValueAsString(dto);
            executePOST(Hardcoded.APIURL+"api/account/add/", JSONString);
        }
        catch (IOException e ) {
            // catch various errors
            e.printStackTrace();
            //TODO gib Nutzer rückmeldung ob erfolgreich
        }
    }

    /**
     * Executes an API-Request with the Account information
     * @param requestURL
     * @param requestJSON
     * @return
     * @throws CreateAccountFailedException
     */
    public static String executePOST(final String requestURL, final String requestJSON) throws CreateAccountFailedException {
        try (final CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build()) {
            final HttpPost httpPost = new HttpPost(requestURL);
            StringEntity entity = new StringEntity(requestJSON);
            httpPost.setEntity(entity);

            final HttpResponse response = closeableHttpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 204) {
                return EntityUtils.toString(response.getEntity());
            } else { //TODO handle exceptions properly and give user feedback
                throw new CreateAccountFailedException("Failed with HTTP error code: " + response.getStatusLine().getStatusCode());
            }
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
