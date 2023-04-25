Feature: Redeem Goodie
  Scenario: Call /api/shop/redeem with existing goodie
    When the client calls endpoint "/api/shop/redeem"
    And the user owns the respective goodie to be redeemed
    Then response status code is 200
    And goodie has been detracted in database
    And the effect of the goodie has been applied

  Scenario: Call /api/shop/redeem with non-existing goodie
    When the client calls endpoint "/api/shop/redeem"
    And the user doesn't own the respective goodie to be redeemed
    Then response status code is 400