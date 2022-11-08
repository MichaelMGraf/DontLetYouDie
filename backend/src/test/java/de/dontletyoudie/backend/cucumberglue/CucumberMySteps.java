package de.dontletyoudie.backend.cucumberglue;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.assertj.core.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


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
        Assertions.assertThat(lastResponse.getStatusCodeValue())
                .isEqualTo(expected);
    }

    //TODO Hieraus nen Test machen
    @Then("one of the returned Accounts should have an Account-Name {string}")
    public void thenAccountExists(String expectedAccountname) {
        Assertions.assertThat(expectedAccountname)
                .isEqualTo("passi0305");
    }
}
