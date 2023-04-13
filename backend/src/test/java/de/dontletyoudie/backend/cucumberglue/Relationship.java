package de.dontletyoudie.backend.cucumberglue;

import io.cucumber.java.en.And;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class Relationship {
    @And("the source account name passed is {string}")
    public void theSourceAccountNamePassedIs(String arg0) {
    }

    @And("there is a pending friend request in the respective direction")
    public void thereIsAPendingFriendRequestInTheRespectiveDirection() {

    }

    @And("the relationship is saved")
    public void theRelationshipIsSaved() {

    }

    @And("the pending request is removed")
    public void thePendingRequestIsRemoved() {

    }

    @And("all current friends are being shown")
    public void allCurrentFriendsAreBeingShown() {

    }

    @And("all current friend requests are being shown")
    public void allCurrentFriendRequestsAreBeingShown() {

    }
}
