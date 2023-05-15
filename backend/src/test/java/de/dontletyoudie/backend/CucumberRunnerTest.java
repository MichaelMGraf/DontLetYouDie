package de.dontletyoudie.backend;

import de.dontletyoudie.backend.cucumberglue.*;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@RunWith(Cucumber.class)
@ContextConfiguration //@SpringBootTest
        (classes = {
        AccountTest.class,
        JudgementTest.class,
        MiniMeTest.class,
        ProofTest.class,
        RelationshipTest.class,
        ShopTest.class
})
@CucumberOptions(
        features = {"./src/test/resources/features"},
        glue = {"de.dontletyoudie.backend.cucumberglue"},
        plugin = {"pretty", "html:target/cucumber-reports/report.html"},
        dryRun = true,
        monochrome = true)
public class CucumberRunnerTest {
}
