package de.dontletyoudie.backend.cucumberglue;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class Proof {
    @When("the user sends a request to {string}")
    public void theUserSendsARequestTo(String arg0) {
        // add code here
    }

    @Then("a picture with a unique name was saved to the filesystem")
    public void aPictureWithAUniqueNameWasSavedToTheFilesystem() {
        // add code here
    }

    @And("there exists a record with the corresponding filename in the database")
    public void thereExistsARecordWithTheCorrespondingFilenameInTheDatabase() {
        // add code here
    }
}
