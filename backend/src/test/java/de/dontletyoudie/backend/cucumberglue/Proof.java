package de.dontletyoudie.backend.cucumberglue;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class Proof extends CucumberRunnerTest {
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
