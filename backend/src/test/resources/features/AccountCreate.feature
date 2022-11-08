Feature: Create Account
  Scenario: Call /api/account/add with already existing username
    When the client calls endpoints "/api/account/add"
    And the account name passed is "passi0305"
    Then response status code is not 200

  Scenario: Call /api/account/add with already existing email
    When the client calls endpoints "/api/account/add"
    And the email passed is "nichtpassis@e.mail"
    Then response status code is not 200