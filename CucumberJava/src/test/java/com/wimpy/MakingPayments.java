package com.wimpy;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;

import static org.junit.Assert.*;

public class MakingPayments extends AssertionState {

  @Given("{string} already exist and has an account with money `{double}`")
  public void already_exist_and_has_an_account_with_money(String string, Double double1) {

    User user = bankFeatures.createUser(string);
    Account account = bankFeatures.createAccount(user);
    account.setBalance(double1);

    assertEquals(double1, account.getBalance(), 0.0);
  }

  @When("{string} makes a payment of money `{double}` to {string} account")
  public void makes_a_payment_of_money_to_account(String string, Double double1, String string2) {

    Optional<User> user = bankFeatures.findUser(string);
    Optional<User> user2 = bankFeatures.findUser(string2);

    try {

      bankFeatures.pay(user.get(), user2.get(), double1);
    } catch (Exception e) {
      exception = e;
    }
  }

  @Then("`{double}` money needs to be subtracted from {string} account")
  public void money_needs_to_be_subtracted_from_account(Double double1, String string) {
    Optional<User> user = bankFeatures.findUser(string);

    if (user.isPresent()) {
      Optional<Account> accountFor = bankFeatures.getAccountFor(user.get());

      accountFor.ifPresentOrElse(
          account -> {
            assertNotEquals(double1, account.getBalance());
          },
          () -> fail("account is not found for user=" + user.get()));

    } else {
      fail("User does not exist");
    }
  }

  @Then("`{double}` needs to be added to {string} account")
  public void needs_to_be_added_to_account(Double double1, String string) {
    Optional<User> user = bankFeatures.findUser(string);

    if (user.isPresent()) {
      Optional<Account> accountFor = bankFeatures.getAccountFor(user.get());

      accountFor.ifPresentOrElse(
          account -> {
            assertEquals(double1, account.getBalance(), 0.0);
          },
          () -> fail("account is not found for user=" + user.get()));

    } else {
      fail("User does not exist");
    }
  }
}
