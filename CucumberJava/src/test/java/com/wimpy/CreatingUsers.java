package com.wimpy;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertNotNull;

public class CreatingUsers extends AssertionState {


  @Given("no users exist")
  public void no_users_exist() {
    bankFeatures = new BankFeatures();
  }

  @Then("user with name {string} exist")
  public void user_with_exist(String string) {
    assertNotNull(bankFeatures.findUser(string));
  }

  @Given("{string} already exist")
  public void name_already_exist(String string) {
    bankFeatures.createUser(string);
    assertNotNull("Users should exist", bankFeatures.findUser(string).get());
  }

  @When("creating user with name {string}")
  public void creating_user_with_name(String string) {

    try {
      bankFeatures.createUser(string);
    } catch (Exception e) {
      exception = e;
    }
  }

  @Then("exception needs to be thrown")
  public void exception_needs_to_be_thrown() {
    assertNotNull(exception);
  }
}
