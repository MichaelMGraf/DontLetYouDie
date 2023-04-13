Feature: Delete Account
  Scenario: Call /api/relationship/delete with existing relationship
    When the client calls endpoints "/api/account/delete"
    And the account name passed is "passi0305"
    Then response status code is 200
    And the account was deleted


  Scenario: Call /api/relationship/add with not yet existing relationship pair
    When the client calls endpoints "/api/relationship/delete"
    And the source name passed is not "passi0305"
    Then response status code is not 200