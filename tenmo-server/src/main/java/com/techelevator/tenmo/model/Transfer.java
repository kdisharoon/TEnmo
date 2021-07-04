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

    public Integer getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public Integer getTransferStatusId() {
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
        return "Transfer{" +
                "transferId=" + transferId +
                ", transferTypeId='" + transferTypeId +
                ", transferStatusId=" + transferStatusId +
                ", accountFrom=" + accountFrom +
                ", usernameFrom=" + usernameFrom +
                ", accountTo=" + accountTo +
                ", usernameTo=" + usernameTo +
                ", amount=" + amount +
                '}';
    }

}

