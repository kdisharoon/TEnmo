package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    Transfer getTransfer(Long transferId);

    List<Transfer> getAllTransfers(Long userId);

    BigDecimal sendTransfer(Long userFromId, Long userToId, BigDecimal amount);

}
