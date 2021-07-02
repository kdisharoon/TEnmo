package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.util.List;

public interface AccountDao {

    Account getAccount(Integer userId);

    List<Account> getAllAccounts();

    boolean updateAccount(Account account, Integer accountId);


}
