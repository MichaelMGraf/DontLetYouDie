Feature: Add Friend
  Scenario: Call /api/relationship/add with already existing username
    When the client calls endpoints "/api/relationship/add"
    And the source name passed is "passi0305"
    And the related name passed is "michael0305"
    Then response status code is not 201


  Scenario: Call /api/relationship/add with already existing relationship pair
    When the client calls endpoints "/api/relationship/add"
    And the source name passed is "passi0305"
    And the related name passed is "gloria0305"
    Then response status code is not 201