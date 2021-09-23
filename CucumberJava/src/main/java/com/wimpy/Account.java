package com.wimpy;

public class Account {

  private final long accountNr;
  private String accountName;
  private double balance;

  public Account(long accountNr, String accountName, double balance) {
    this.accountNr = accountNr;
    this.accountName = accountName;
    this.balance = balance;
  }

  public long getAccountNr() {
    return accountNr;
  }

  public String getAccountName() {
    return accountName;
  }

  public double getBalance() {
    return balance;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }
}
