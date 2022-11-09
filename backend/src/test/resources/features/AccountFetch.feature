Feature: Fetch Accounts
  Scenario: Call /api/account/get endpoint to fetch existing accounts
    When the client calls endpoints "/api/account/get"
    Then response status code is 200
    And one of the returned Accounts should have an Account-Name "passi0305"