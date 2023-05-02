package de.dontletyoudie.backend;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import cucumberglue.AccountTest;
import cucumberglue.Judgement;
import cucumberglue.MiniMe;
import cucumberglue.Proof;
import cucumberglue.Relationship;
import cucumberglue.Shop;
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
