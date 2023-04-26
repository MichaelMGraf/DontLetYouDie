Feature: Redeem Goodie
  Scenario: Call /api/shop/buy with existing goodie
    When the client calls endpoint "/api/shop/buy"
    And the goodie to buy exists
    Then response status code is 201
    And goodie has been added to user's inventory
    And the price has been detracted from user's purse

  Scenario: Call /api/shop/buy with non-existing goodie
    When the client calls endpoint "/api/shop/buy"
    And the item to be bought doesn't exist
    Then response status code is 400

  Scenario: Call /api/shop/buy with enough currency
    When the client calls endpoint "/api/shop/buy"
    And the user has enough currency in purse
    Then response status code is 201

  Scenario: Call /api/shop/buy with non-existing goodie
    When the client calls endpoint "/api/shop/buy"
    And the user doesn't have enough currency in purse
    Then response status code is 400