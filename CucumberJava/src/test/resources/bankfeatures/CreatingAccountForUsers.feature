Feature: CreatingAccountForUsers

  Scenario: Creating account for a user
    Given "Sam" already exist
    When creating account for user "Sam"
    Then account must be created for "Sam"


  Scenario: Creating account for a user that has an account Already
    Given "Bob" already exist and has an account
    When creating account for user "Bob"
    Then exception needs to be thrown

