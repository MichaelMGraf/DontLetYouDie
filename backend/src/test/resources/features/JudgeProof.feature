Feature: Judge proof
  Scenario: Call /api/judgement/add with judgement
    When the client calls endpoint "/api/judgement/add"
    Then response status code is 201
    And response contains info that judgement has been saved

  Scenario: Call /api/judgement/add with judgement
    When the client calls endpoint "/api/judgement/add"
    Then the effects of the judgement have been applied