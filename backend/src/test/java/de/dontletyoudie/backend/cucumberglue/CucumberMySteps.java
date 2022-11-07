package de.dontletyoudie.backend.cucumberglue;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class CucumberMySteps {

    @LocalServerPort
    String port;

    ResponseEntity<String> lastResponse;

    @When("the client calls endpoint {string}")
    public void whenClientCalls(String url) {
        lastResponse = new RestTemplate().exchange("http://localhost:" + port + url,
                HttpMethod.GET,
                null,
                String.class);
    }


    @Then("response status code is {int}")
    public void thenStatusCode(int expected) {
        assertThat("status code is " + expected,
        lastResponse.getStatusCodeValue() == expected);
    }

    //TODO Hieraus nen Test machen
    @Then("one of the returned Accounts should have an Account-Name {string}")
    public void thenAccountExists(String expectedAccountname) {
        Assertions.assertEquals(expectedAccountname, "passi0305");
    }
}
