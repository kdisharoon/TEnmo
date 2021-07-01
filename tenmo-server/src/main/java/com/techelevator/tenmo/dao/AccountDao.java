package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

public interface AccountDao {

    Account getAccount(Integer userId);

    boolean updateAccount(Account account, Integer accountId);


}
