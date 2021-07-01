package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.AcctNotFoundException;
import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;


@RestController
public class TenmoController {

    private UserDao userDao;
    private AccountDao accountDao;

    public TenmoController(UserDao userDao, AccountDao accountDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;
    }


    @RequestMapping(path = "users", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @RequestMapping(path = "users/{username}", method = RequestMethod.GET)
    public User getUser(@PathVariable String username) throws UserNotFoundException {
        return userDao.findByUsername(username);
    }

//    @RequestMapping(path = "users/{username}", method = RequestMethod.GET)
//    public int getUserId(@PathVariable String username) throws UserNotFoundException {
//        return userDao.findIdByUsername(username);
//    }

    @RequestMapping(path = "accounts/{id}", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable Long id) throws AcctNotFoundException {
        return accountDao.getAccount(id).getBalance();
    }

    @RequestMapping(path = "accounts/{id}", method = RequestMethod.PUT)
    public boolean updateBalance(@Valid @RequestBody Account acct, @PathVariable Long id) throws AccountNotFoundException {
        return accountDao.updateAccount(acct);

    }



}
