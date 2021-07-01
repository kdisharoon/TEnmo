package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccount(Long accountId) {
        Account account = null;
        String sql = "SELECT account_id, user_id, balance FROM accounts WHERE account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            account = mapRowToAccount(results);
        }
        return account;
    }

    @Override
    public boolean updateAccount(Account account) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?;";
        int rows = jdbcTemplate.update(sql, account.getBalance(), account.getAccountId());
        return (rows == 1);
    }

    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account a = new Account();
        a.setAccountId(rowSet.getLong("account_id"));
        a.setUserId(rowSet.getLong("user_id"));
        a.setBalance(rowSet.getBigDecimal("balance"));
        return a;
    }



}
