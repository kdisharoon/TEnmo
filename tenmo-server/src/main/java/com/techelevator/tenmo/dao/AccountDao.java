package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal getBalance(Long accountId);

    BigDecimal transferBalance(Long userFromId, Long userToId, BigDecimal amount);


}
