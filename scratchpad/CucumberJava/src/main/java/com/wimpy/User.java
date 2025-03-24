package com.wimpy;

public class User {

  private final java.lang.String name;
  private Account accounts;

  public User(java.lang.String name) {
    this.name = name;
  }

  public java.lang.String getName() {
    return name;
  }

  public Account getAccount() {
    return accounts;
  }

  public void setAccount(Account accounts) {
    this.accounts = accounts;
  }
}
