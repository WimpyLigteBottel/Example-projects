package com.wimpy;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BankFeatures {

  static long lastAccountNr = 0L;

  Map<String, User> users = new HashMap<>();

  public void pay(User from, User to, double amount) {

    Account fromAccount = from.getAccount();
    Account toAccount = to.getAccount();

    if (fromAccount.getBalance() < amount) {
      throw new RuntimeException(
          String.format(
              "from user does not have enough money to make payment [from=%s;from.balance=%s]",
              from.getName(), fromAccount.getBalance() + ""));
    }

    fromAccount.setBalance(fromAccount.getBalance() - amount);
    toAccount.setBalance(toAccount.getBalance() + amount);
  }

  public void transfer(User user, long fromAccount, long toAccount) {}

  public void retrieveBalance(User user) {}

  public void retrieveBalance(Account account) {}

  public Account createAccount(User user) {
    long accountNr = lastAccountNr++;

    validateCanUserCreateAccount(user);

    Account account = new Account(accountNr, String.valueOf(accountNr), 0.0);
    user.setAccount(account);

    return account;
  }

  private void validateCanUserCreateAccount(User user) {

    if (user.getAccount() != null) {
      throw new RuntimeException("User already has an account");
    }
  }

  public User createUser(java.lang.String name) {

    validateUserExist(name);
    User user = new User(name);

    users.put(name, user);

    return user;
  }

  private void validateUserExist(String name) {

    if (name == null || name.matches(".+[\\d]+")) {
      throw new RuntimeException("Can't contain digits in name");
    }

    users.forEach(
        (username, user) -> {
          if (name.equalsIgnoreCase(user.getName())) {
            throw new RuntimeException("User already exist");
          }
        });
  }

  public Optional<User> findUser(String name) {
    return Optional.ofNullable(users.get(name));
  }

  public Optional<Account> getAccountFor(User user) {
    return Optional.ofNullable(users.get(user.getName()).getAccount());
  }
}
