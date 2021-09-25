Feature: MakingPayments
  #This will contain list of all availiable features in the bank and describing it

  Scenario: Making a payment to another account when you have money
    Given "Bob" already exist and has an account with money `100.0`
    Given "Sam" already exist and has an account with money `0.0`
    When "Bob" makes a payment of money `100.0` to "Sam" account
    Then `100.0` money needs to be subtracted from "Bob" account
    And `100.0` needs to be added to "Sam" account


  Scenario: You should not be able to make payment if you don't have money
    Given "Bob" already exist and has an account with money `100.0`
    Given "Sam" already exist and has an account with money `0.0`
    When "Sam" makes a payment of money `100.0` to "Bob" account
    Then exception needs to be thrown