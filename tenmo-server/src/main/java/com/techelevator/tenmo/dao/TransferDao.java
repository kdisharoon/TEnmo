package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    Transfer getTransfer(Integer transferId) throws TransferNotFoundException;

    List<Transfer> getAllPendingTransfers(Integer userId);

    List<Transfer> getAllTransfers(Integer userId);

    Transfer createTransfer(Transfer transfer) throws TransferNotFoundException;

}
