Feature: Create Proof
  Scenario: The user has taken a picture and wants to upload it as proof
    When the user sends a request to "/api/upload/image"
    Then a picture with a unique name was saved to the filesystem
    And there exists a record with the corresponding filename in the database
    And response status code is 200
