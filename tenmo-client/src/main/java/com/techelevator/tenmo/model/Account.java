package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {

    private Integer accountId;
    private Integer userId;
    private BigDecimal balance;

    public Account() { }

    public Account(Integer accountId, Integer userId, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }



}
