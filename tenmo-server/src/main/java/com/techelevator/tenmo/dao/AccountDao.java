package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.util.List;

public interface AccountDao {

    Account getAccount(Integer userId);

    Account getAccountByAccountId(Integer accountId);

    List<Account> getAllAccounts();

    boolean updateAccount(Account account, Integer accountId);


}
