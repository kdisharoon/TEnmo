package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
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
    public BigDecimal getBalance(Long accountId) {
        String sql = "SELECT balance FROM accounts WHERE account_id = ?;";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
        return balance;
    }

    @Override
    public BigDecimal transferBalance(Long userFromId, Long userToId, BigDecimal amount) {

        BigDecimal fromBalance = getBalance(userFromId);
        BigDecimal toBalance = getBalance(userToId);

        if (fromBalance.compareTo(amount) >= 0) {
            BigDecimal toBalanceNew = toBalance.add(amount);
            BigDecimal fromBalanceNew = fromBalance.subtract(amount);

            String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?;";

            jdbcTemplate.update(sql, toBalanceNew, userToId);
            jdbcTemplate.update(sql, fromBalanceNew, userFromId);
        }

        return amount;
    }


}
