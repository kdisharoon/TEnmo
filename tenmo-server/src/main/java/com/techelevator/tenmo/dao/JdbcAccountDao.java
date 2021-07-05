package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccount(Integer userId) {
        Account account = null;
        String sql = "SELECT account_id, user_id, balance FROM accounts WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            account = mapRowToAccount(results);
        }
        return account;
    }

    @Override
    public Account getAccountByAccountId(Integer accountId) {
        Account account = null;
        String sql = "SELECT account_id, user_id, balance FROM accounts WHERE account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            account = mapRowToAccount(results);
        }
        return account;
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT account_id, user_id, balance FROM accounts;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            accounts.add(mapRowToAccount(results));
        }
        return accounts;
    }

    @Override
    public boolean updateAccount(Account account, Integer accountId) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?;";
        int rows = jdbcTemplate.update(sql, account.getBalance(), accountId);
        return (rows == 1);
    }

    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account a = new Account();
        a.setAccountId(rowSet.getInt("account_id"));
        a.setUserId(rowSet.getInt("user_id"));
        a.setBalance(rowSet.getBigDecimal("balance"));
        return a;
    }



}
