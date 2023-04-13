Feature: Manage Friend Request
  Scenario: Call /api/relationship/accept with existing friend request
    When the client calls endpoints "/api/relationship/accept"
    And the source account name passed is "passi0305"
    And the source account name passed is "gloria0305"
    And there is a pending friend request in the respective direction
    Then response status code is 201
    And the relationship is saved

  Scenario: Call /api/relationship/decline with existing friend request
    When the client calls endpoints "/api/relationship/decline"
    And the source account name passed is "passi0305"
    And the source account name passed is "gloria0305"
    And there is a pending friend request in the respective direction
    Then response status code is 200
    And the pending request is removed

  Scenario: Call /api/relationship/getFriends
    When the client calls endpoints "/api/relationship/getFriends"
    And the source name passed is "passi0305"
    Then response status code is not 200
    And all current friends are being shown

  Scenario: Call /api/relationship/getPendingFriendRequests
    When the client calls endpoints "/api/relationship/getFriends"
    And the source name passed is "passi0305"
    Then response status code is 200
    And all current friend requests are being shown