package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;


public class AccountService {

    private final String API_BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();

    public AccountService(String apiUrl) {
        this.API_BASE_URL = apiUrl;
    }

    public Account getAccountById(AuthenticatedUser currentUser) {
        Account account = null;

        try {
            account = restTemplate.exchange(API_BASE_URL + "accounts/" + currentUser.getUser().getId(),
                                            HttpMethod.GET, makeAuthEntity(currentUser), Account.class).getBody();
        }
        catch (Exception e) {
            System.out.println("getAccountById exception!");
        }

        return account;

    }

    public Account[] getAllAccounts(AuthenticatedUser currentUser) {
        Account[] accounts = null;

        try {
            accounts = restTemplate.exchange(API_BASE_URL + "accounts",
                       HttpMethod.GET, makeAuthEntity(currentUser), Account[].class).getBody();
        }
        catch (Exception e) {
            System.out.println("getAllAccounts exception!");
        }

        return accounts;
    }



    //not sure if we want this method - will review later
    public Account updateAccount(AuthenticatedUser currentUser, BigDecimal newBalance) {

        Account account = getAccountById(currentUser);
        account.setBalance(newBalance);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Account> entity = new HttpEntity<>(account, headers);

        try {
            restTemplate.put(API_BASE_URL + "accounts/" + account.getAccountId(), entity);
        } catch (RestClientResponseException e) {
            System.out.println(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            System.out.println(e.getMessage());
        }

        return account;
    }

    private HttpEntity makeAuthEntity(AuthenticatedUser currentUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity entity = new HttpEntity(headers);
        return entity;
    }



}
