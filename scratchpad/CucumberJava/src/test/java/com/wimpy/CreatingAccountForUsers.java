package com.wimpy;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;

import static org.junit.Assert.*;

public class CreatingAccountForUsers extends AssertionState {

  @Given("{string} already exist and has an account")
  public void already_exist_and_has_an_account(String string) {
    User user = bankFeatures.createUser(string);
    Account account = bankFeatures.createAccount(user);
    assertNotNull(account);
  }

  @When("creating account for user {string}")
  public void creating_account_for(String string) {

    Optional<User> user = bankFeatures.findUser(string);

    if (user.isPresent()) {
      try {
        bankFeatures.createAccount(user.get());
      } catch (Exception e) {
        exception = e;
      }
    } else {
      fail("User does not exist");
    }
  }

  @Then("account must be created for {string}")
  public void account_must_be_created_for(String string) {
    Optional<User> user = bankFeatures.findUser(string);

    if (user.isPresent()) {
      assertTrue(bankFeatures.getAccountFor(user.get()).isPresent());
    } else {
      fail("User does not exist");
    }
  }
}
