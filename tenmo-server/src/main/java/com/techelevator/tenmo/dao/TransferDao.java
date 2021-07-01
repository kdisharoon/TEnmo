package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    Transfer getTransfer(Integer transferId);

    List<Transfer> getAllTransfers(Integer userId);

    BigDecimal createTransfer(Integer userFromId, Integer userToId, BigDecimal amount);

}
