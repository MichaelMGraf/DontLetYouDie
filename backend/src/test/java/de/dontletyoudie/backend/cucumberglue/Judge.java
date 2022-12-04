package de.dontletyoudie.backend.cucumberglue;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

public class Judge {
    @When("the client calls endpoint {string}")
    public void whenClientCalls(String route) {
    }

    @Then("response contains info that judgement has been saved")
    public void thenResponseContainsJudgemendSaved() {

    }
}
