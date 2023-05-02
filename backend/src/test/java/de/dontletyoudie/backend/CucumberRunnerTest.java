package de.dontletyoudie.backend;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import de.dontletyoudie.backend.cucumberglue.AccountTest;
import de.dontletyoudie.backend.cucumberglue.Judgement;
import de.dontletyoudie.backend.cucumberglue.MiniMe;
import de.dontletyoudie.backend.cucumberglue.Proof;
import de.dontletyoudie.backend.cucumberglue.Relationship;
import de.dontletyoudie.backend.cucumberglue.Shop;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Cucumber.class)
@SpringBootTest(classes = {
        AccountTest.class,
        Judgement.class,
        MiniMe.class,
        Proof.class,
        Relationship.class,
        Shop.class
})
@CucumberOptions(
        features = {"./src/test/resources/features"},
        plugin = {"pretty"},
        glue = {"de.dontletyoudie.backend.cucumberglue"},
        dryRun = true,
        monochrome = true)
@CucumberContextConfiguration
public class CucumberRunnerTest {
}
