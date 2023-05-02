package de.dontletyoudie.backend.cucumberglue;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import de.dontletyoudie.backend.CucumberRunnerTest;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class AccountTest extends CucumberRunnerTest {

    @LocalServerPort
    String port;

    ResponseEntity<String> lastResponse;

    @When("the client calls endpoints {string}")
    public void whenClientCalls(String url) {
        /*
        lastResponse = new RestTemplate().exchange("http://localhost:" + port + url,
                HttpMethod.GET,
                null,
                String.class);

         */
    }

    @Then("response status code is {int}")
    public void thenStatusCode(int expected) {
        /*
        Assertions.assertThat(lastResponse.getStatusCodeValue())
                .isEqualTo(expected);

         */
    }

    //TODO Hieraus nen Test machen
    @Then("one of the returned Accounts should have an Account-Name {string}")
    public void thenAccountExists(String expectedAccountname) {
        /*
        Assertions.assertThat(expectedAccountname)
                .isEqualTo("passi0305");
                
         */
    }

    @And("the account name passed is {string}")
    public void theAccountNamePassedIs(String arg0) {
        // add code here
    }

    @Then("response status code is not {int}")
    public void responseStatusCodeIsNot(int arg0) {
        // add code here
    }

    @And("the email passed is {string}")
    public void theEmailPassedIs(String arg0) {
        // add code here
    }

    @And("the account name passed doesn't exist yet")
    public void theAccountNamePassedDoesnTExistYet() {
    }

    @And("the email passed doesn't exist yet")
    public void theEmailPassedDoesnTExistYet() {

    }

    @And("the related name passed is {string}")
    public void theRelatedNamePassedIs(String arg0) {
        
    }

    @And("the source name passed is {string}")
    public void theSourceNamePassedIs(String arg0) {
        
    }

    @And("the source name passed is not {string}")
    public void theSourceNamePassedIsNot(String arg0) {
        
    }

    @And("the account was deleted")
    public void theAccountWasDeleted() {
    }
}
