package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;


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
        catch (ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        catch (RestClientResponseException e) {
            System.out.println(e.getRawStatusCode());
        }

        return account;

    }

    public Account[] getAllAccounts(AuthenticatedUser currentUser) {
        Account[] accounts = null;

        try {
            accounts = restTemplate.exchange(API_BASE_URL + "accounts",
                       HttpMethod.GET, makeAuthEntity(currentUser), Account[].class).getBody();
        }
        catch (ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        catch (RestClientResponseException e) {
            System.out.println(e.getRawStatusCode());
        }

        return accounts;
    }

    private HttpEntity makeAuthEntity(AuthenticatedUser currentUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity entity = new HttpEntity(headers);
        return entity;
    }

}
