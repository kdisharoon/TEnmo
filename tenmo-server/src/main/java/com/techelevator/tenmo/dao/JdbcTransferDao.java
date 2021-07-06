package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, AccountDao accountDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountDao = accountDao;
    }


    @Override
    public Transfer getTransfer(Integer transferId) throws TransferNotFoundException {
        Transfer t;
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                     "FROM transfers " +
                     "WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            t =  mapRowToTransfer(results);
        }
        else {
            throw new TransferNotFoundException();
        }

        assert t != null;

        String sqlFrom = "SELECT username FROM users u " +
                         "JOIN accounts a USING (user_id) JOIN transfers t ON (a.account_id = t.account_from) " +
                         "WHERE t.transfer_id = ?;";
        SqlRowSet fromResults = jdbcTemplate.queryForRowSet(sqlFrom, transferId);
        if (fromResults.next()) {
            t.setUsernameFrom(fromResults.getString("username"));
        }

        String sqlTo = "SELECT username FROM users u " +
                "JOIN accounts a USING (user_id) JOIN transfers t ON (a.account_id = t.account_to) " +
                "WHERE t.transfer_id = ?;";
        SqlRowSet toResults = jdbcTemplate.queryForRowSet(sqlTo, transferId);
        if (toResults.next()) {
            t.setUsernameTo(toResults.getString("username"));
        }

        return t;
    }

    //below method is almost duplicating getAllTransfers, this can be refactored with more time
        @Override
    public List<Transfer> getAllPendingTransfers(Integer userId) {
        List<Transfer> transfers = new ArrayList<>();

        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfers t " +
                "JOIN accounts a ON (t.account_from = a.account_id OR t.account_to = a.account_id) " +
                "JOIN users u ON (a.user_id = u.user_id) " +
                "WHERE a.user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        while (results.next()) {
            Transfer t = mapRowToTransfer(results);

            if (t.getTransferStatusId() == 1) {

                String sqlFrom = "SELECT username FROM users u " +
                        "JOIN accounts a USING (user_id) JOIN transfers t ON (a.account_id = t.account_from) " +
                        "WHERE t.transfer_id = ?;";
                SqlRowSet fromResults = jdbcTemplate.queryForRowSet(sqlFrom, t.getTransferId());
                if (fromResults.next()) {
                    t.setUsernameFrom(fromResults.getString("username"));
                }

                String sqlTo = "SELECT username FROM users u " +
                        "JOIN accounts a USING (user_id) JOIN transfers t ON (a.account_id = t.account_to) " +
                        "WHERE t.transfer_id = ?;";
                SqlRowSet toResults = jdbcTemplate.queryForRowSet(sqlTo, t.getTransferId());
                if (toResults.next()) {
                    t.setUsernameTo(toResults.getString("username"));
                }

                transfers.add(t);
            }
        }

        return transfers;
    }

    @Override
    public List<Transfer> getAllTransfers(Integer userId) {

        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfers t " +
                "JOIN accounts a ON (t.account_from = a.account_id OR t.account_to = a.account_id) " +
                "JOIN users u ON (a.user_id = u.user_id) " +
                "WHERE a.user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);

        while (results.next()) {
            Transfer t = mapRowToTransfer(results);

            String sqlFrom = "SELECT username FROM users u " +
                    "JOIN accounts a USING (user_id) JOIN transfers t ON (a.account_id = t.account_from) " +
                    "WHERE t.transfer_id = ?;";
            SqlRowSet fromResults = jdbcTemplate.queryForRowSet(sqlFrom, t.getTransferId());
            if (fromResults.next()) {
                t.setUsernameFrom(fromResults.getString("username"));
            }

            String sqlTo = "SELECT username FROM users u " +
                    "JOIN accounts a USING (user_id) JOIN transfers t ON (a.account_id = t.account_to) " +
                    "WHERE t.transfer_id = ?;";
            SqlRowSet toResults = jdbcTemplate.queryForRowSet(sqlTo, t.getTransferId());
            if (toResults.next()) {
                t.setUsernameTo(toResults.getString("username"));
            }

            transfers.add(t);
        }

        return transfers;
    }

    @Override
    public Transfer createTransfer(Transfer transfer) throws TransferNotFoundException {
        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                     "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";
        Integer newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, transfer.getTransferTypeId(),
                transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());

        //if this transfer has been approved
        if (transfer.getTransferStatusId() == 2) {
            updateBalances(transfer);
        }

        return getTransfer(newTransferId);
    }

    private void updateBalances(Transfer transfer) {
        String sqlFrom = "UPDATE accounts SET balance = ? WHERE account_id = ?;";
        Account fromAccount = accountDao.getAccountByAccountId(transfer.getAccountFrom());
        jdbcTemplate.update(sqlFrom, fromAccount.getBalance().subtract(transfer.getAmount()), fromAccount.getAccountId());

        String sqlTo = "UPDATE accounts SET balance = ? WHERE account_id = ?;";
        Account toAccount = accountDao.getAccountByAccountId(transfer.getAccountTo());
        jdbcTemplate.update(sqlTo, toAccount.getBalance().add(transfer.getAmount()), toAccount.getAccountId());
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer t = new Transfer();
        t.setTransferId(rs.getInt("transfer_id"));
        t.setTransferTypeId(rs.getInt("transfer_type_id"));
        t.setTransferStatusId(rs.getInt("transfer_status_id"));
        t.setAccountFrom(rs.getInt("account_from"));
        t.setAccountTo(rs.getInt("account_to"));
        t.setAmount(rs.getBigDecimal("amount"));
        return t;
    }


}
