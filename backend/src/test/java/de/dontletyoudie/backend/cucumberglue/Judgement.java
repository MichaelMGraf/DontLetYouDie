package de.dontletyoudie.backend.cucumberglue;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

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
