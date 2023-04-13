Feature: Delete Friend
  Scenario: Call /api/relationship/delete with existing relationship
    When the client calls endpoints "/api/relationship/add"
    And the source name passed is "passi0305"
    And the related name passed is "gloria0305"
    Then response status code is 200


  Scenario: Call /api/relationship/add with not yet existing relationship pair
    When the client calls endpoints "/api/relationship/delete"
    And the source name passed is "passi0305"
    And the related name passed is "gloria0305"
    Then response status code is 400