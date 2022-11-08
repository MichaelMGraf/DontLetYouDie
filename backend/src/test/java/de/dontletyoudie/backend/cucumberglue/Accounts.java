package de.dontletyoudie.backend.cucumberglue;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class Accounts {
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
}
