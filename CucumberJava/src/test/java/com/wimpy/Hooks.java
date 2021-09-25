package com.wimpy;

import io.cucumber.java.Before;

public class Hooks extends AssertionState {

  /*
  Before hooks run before the first step of each scenario.
   */

  @Before
  public void doSomethingBefore() {
    bankFeatures = new BankFeatures();
    exception = null;
  }
}
