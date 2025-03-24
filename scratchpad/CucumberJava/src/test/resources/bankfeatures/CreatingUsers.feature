Feature: CreatingUsers


  Scenario: Creating a user
    Given no users exist
    When creating user with name "Marco"
    Then user with name "name" exist

  Scenario: Creating a user that already exist
    Given "Marco" already exist
    When creating user with name "Marco"
    Then exception needs to be thrown

  Scenario: Creating a user that does not exist
    Given "Marco" already exist
    When creating user with name "Jak"
    Then user with name "name" exist

  Scenario: Creating a user that has digits in name
    Given "Marco" already exist
    When creating user with name "Jak1234"
    Then exception needs to be thrown
