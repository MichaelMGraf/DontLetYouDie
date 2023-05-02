package de.dontletyoudie.backend.cucumberglue;

import io.cucumber.java.en.And;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class MiniMe extends CucumberRunnerTest {
    @And("the reponse contains all necessary data")
    public void theReponseContainsAllNecessaryData() {
    }
}
