package com.techelevator.tenmo.dao;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal getBalance(Long accountId);

    BigDecimal transferBalance(Long userFromId, Long userToId, BigDecimal amount);


}
