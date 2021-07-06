package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.AcctNotFoundException;
import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;


@RestController
@PreAuthorize("isAuthenticated()")
public class TenmoController {

    private UserDao userDao;
    private AccountDao accountDao;
    private TransferDao transferDao;

    public TenmoController(UserDao userDao, AccountDao accountDao, TransferDao transferDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }


    @RequestMapping(path = "users", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @RequestMapping(path = "users/{username}", method = RequestMethod.GET)
    public User getUser(@PathVariable String username) throws UserNotFoundException {
        return userDao.findByUsername(username);
    }

    @RequestMapping(path = "accounts/{id}", method = RequestMethod.GET)
    public Account getAccount(@PathVariable Integer id) throws AcctNotFoundException {
        return accountDao.getAccount(id);
    }

    @RequestMapping(path = "accounts", method = RequestMethod.GET)
    public List<Account> getAllAccounts() {
        return accountDao.getAllAccounts();
    }

    @RequestMapping(path = "accounts/{id}", method = RequestMethod.PUT)
    public boolean updateBalance(@Valid @RequestBody Account acct, @PathVariable Integer id) throws AcctNotFoundException {
        return accountDao.updateAccount(acct, id);
    }

//    @RequestMapping(path = "accounts/{id}/transfers", method = RequestMethod.GET)
//    public List<Transfer> getTransfers(@PathVariable("id") Integer userId) throws UserNotFoundException {
//        return transferDao.getAllTransfers(userId);
//    }

    @RequestMapping(path = "accounts/{id}/transfers", method = RequestMethod.GET)
    public List<Transfer> getTransfers(@PathVariable("id") Integer userId,
                                @RequestParam(required = false, defaultValue = "") String status, @RequestParam(defaultValue = "100") Integer limit) {
        if (status.equalsIgnoreCase("pending")) {
            return transferDao.getAllPendingTransfers(userId);
        }
        else {
            return transferDao.getAllTransfers(userId);
        }
    }

    @RequestMapping(path = "transfers/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferDetails(@PathVariable("transferId") Integer transferId) throws TransferNotFoundException {
        return transferDao.getTransfer(transferId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "transfers", method = RequestMethod.POST)
    public Transfer create(@Valid @RequestBody Transfer transfer) throws TransferNotFoundException {
        return transferDao.createTransfer(transfer);
    }



}
