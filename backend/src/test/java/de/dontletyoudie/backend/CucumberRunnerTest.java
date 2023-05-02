package de.dontletyoudie.backend;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Cucumber.class)
@SpringBootTest
@CucumberOptions(
        features = {"./src/test/resources/features"},
        plugin = {"pretty"},
        glue = {"de.dontletyoudie.backend.cucumberglue"},
        dryRun = true,
        monochrome = true)
public class CucumberRunnerTest {
}
