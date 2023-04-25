Feature: Display MiniMe
  Scenario: user calls /api/minime/show
    When the client calls endpoint "/api/minime/show"
    Then response status code is 200
    And the reponse contains all necessary data