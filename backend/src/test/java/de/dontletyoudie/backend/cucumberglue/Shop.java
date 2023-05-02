package de.dontletyoudie.backend.cucumberglue;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import io.cucumber.java.en.And;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class Shop extends CucumberRunnerTest {
    @And("the user owns the respective goodie to be redeemed")
    public void theUserOwnsTheRespectiveGoodieToBeRedeemed() {

    }

    @And("goodie has been detracted in database")
    public void goodieHasBeenDetractedInDatabase() {

    }

    @And("the effect of the goodie has been applied")
    public void theEffectOfTheGoodieHasBeenApplied() {

    }

    @And("the user doesn't own the respective goodie to be redeemed")
    public void theUserDoesnTOwnTheRespectiveGoodieToBeRedeemed() {
    }

    @And("the goodie to buy exists")
    public void theGoodieToBuyExists() {
        
    }

    @And("goodie has been added to user's inventory")
    public void goodieHasBeenAddedToUserSInventory() {
        
    }

    @And("the price has been detracted from user's purse")
    public void thePriceHasBeenDetractedFromUserSPurse() {
        
    }

    @And("the item to be bought doesn't exist")
    public void theItemToBeBoughtDoesnTExist() {
        
    }

    @And("the user has enough currency in purse")
    public void theUserHasEnoughCurrencyInPurse() {
        
    }

    @And("the user doesn't have enough currency in purse")
    public void theUserDoesnTHaveEnoughCurrencyInPurse() {
    }
}
