package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private Integer transferId;
    private int transferTypeId;
    private int transferStatusId;
    private Integer accountFrom;
    private Integer accountTo;
    private BigDecimal amount;
    private String usernameFrom;
    private String usernameTo;

    public Transfer() { }

    public Transfer(Integer transferId, int transferTypeId, int transferStatusId, Integer accountFrom, Integer accountTo, BigDecimal amount) {
        this.transferId = transferId;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public Integer getTransferId() {
        return transferId;
    }

    public void setTransferId(Integer transferId) {
        this.transferId = transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public Integer getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Integer accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Integer getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Integer accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getUsernameFrom() {
        return usernameFrom;
    }

    public void setUsernameFrom(String usernameFrom) {
        this.usernameFrom = usernameFrom;
    }

    public String getUsernameTo() {
        return usernameTo;
    }

    public void setUsernameTo(String usernameTo) {
        this.usernameTo = usernameTo;
    }

    @Override
    public String toString() {
        return "Transfer ID: " + transferId + ", From: " + usernameFrom + ", To: " + usernameTo + ", Amount: " + amount;
    }

    public void printFullTransferDetails() {
        String transferTypeText = transferTypeId == 1 ? "Request" :
                                  transferTypeId == 2 ? "Send" : "Error in Transfer Type";
        String transferStatusText = transferStatusId == 1 ? "Pending" :
                                    transferStatusId == 2 ? "Approved" :
                                    transferStatusId == 3 ? "Rejected" : "Error in Transfer Status";
        System.out.println("Transfer ID: " + transferId);
        System.out.println("From: " + usernameFrom);
        System.out.println("To: " + usernameTo);
        System.out.println("Type: " + transferTypeText);
        System.out.println("Status: " + transferStatusText);
        System.out.println("Amount: " + amount);
    }

    public String toStringPending() {
        return "Transfer ID: " + transferId + ", To: " + usernameTo + ", Amount: " + amount;
    }

}
