package de.dontletyoudie.backend.cucumberglue;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class Judgement {
    @When("the client calls endpoint {string}")
    public void whenClientCalls(String route) {
    }

    @Then("response contains info that judgement has been saved")
    public void thenResponseContainsJudgemendSaved() {

    }

    @Then("the effects of the judgement have been applied")
    public void theEffectsOfTheJudgementHaveBeenApplied() {

    }
}
