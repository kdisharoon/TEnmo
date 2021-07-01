package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, AccountDao accountDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountDao = accountDao;
    }


    @Override
    public Transfer getTransfer(Long transferId) {
        Transfer t = null;
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                     "FROM transfers " +
                     "WHERE transfer_id = ?;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            t =  mapRowToTransfer(results);
        }

        return t;
    }

    @Override
    public List<Transfer> getAllTransfers(Long userId) {

        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, account_from, account_to, amount " +
                "FROM transfers t" +
                "JOIN accounts a ON (t.account_from = a.account_id OR t.account_to = a.account_id)" +
                "JOIN users u ON (a.user_id = u.user_id)" +
                "WHERE u.user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;

    }

    @Override
    public BigDecimal createTransfer(Long userFromId, Long userToId, BigDecimal amount) {

        BigDecimal fromBalance = accountDao.getAccount(userFromId).getBalance();
        BigDecimal toBalance = accountDao.getAccount(userToId).getBalance();

        if (fromBalance.compareTo(amount) >= 0) {
            BigDecimal toBalanceNew = toBalance.add(amount);
            BigDecimal fromBalanceNew = fromBalance.subtract(amount);

            String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?;";

            jdbcTemplate.update(sql, toBalanceNew, userToId);
            jdbcTemplate.update(sql, fromBalanceNew, userFromId);
        }

        return amount;
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer t = new Transfer();
        t.setTransferId(rs.getLong("transfer_id"));
        t.setTransferTypeId(rs.getInt("transfer_type_id"));
        t.setTransferStatusId(rs.getInt("transfer_status_id"));
        t.setAccountFrom(rs.getLong("account_from"));
        t.setAccountTo(rs.getLong("account_to"));
        t.setAmount(rs.getBigDecimal("amount"));
        return t;
    }


}
