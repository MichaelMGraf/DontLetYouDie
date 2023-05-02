package de.dontletyoudie.backend;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"./src/test/resources/features"},
        plugin = {"pretty"},
        glue = {"de.dontletyoudie.backend.cucumberglue"})
public class CucumberRunnerTest {
}
