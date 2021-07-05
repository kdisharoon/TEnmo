package com.techelevator.tenmo.services;

import com.techelevator.tenmo.exception.TransferNotFoundException;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;


public class TransferService {
    private final String API_BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();

    public TransferService(String apiUrl) {
        this.API_BASE_URL = apiUrl;

    }

    public Transfer getTransferById(AuthenticatedUser currentUser, Integer transferId) {

        Transfer transfer = null;

        try {
            transfer = restTemplate.exchange(API_BASE_URL + "transfers/" + transferId, HttpMethod.GET,
                                             makeAuthEntity(currentUser), Transfer.class).getBody();
        }
        catch (ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        catch (RestClientResponseException e) {
            System.out.println(e.getRawStatusCode());
        }

        return transfer;

    }

    public Transfer[] getAllTransfers(AuthenticatedUser currentUser) {
        Transfer[] transfers = null;

        Integer userId = currentUser.getUser().getId();

        try {
            transfers = restTemplate.exchange(API_BASE_URL + "accounts/" + userId + "/transfers", HttpMethod.GET,
                                              makeAuthEntity(currentUser), Transfer[].class).getBody();
        }
        catch (ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        catch (RestClientResponseException e) {
            System.out.println(e.getRawStatusCode());
        }

        return transfers;

    }

    public Transfer[] getAllPendingTransfers (AuthenticatedUser currentUser) {
        Transfer[] transfers = null;

        Integer userId = currentUser.getUser().getId();

        try {
            transfers = restTemplate.exchange(API_BASE_URL + "accounts/" + userId + "/transfers?status=pending",
                    HttpMethod.GET, makeAuthEntity(currentUser), Transfer[].class).getBody();
        }
        catch (ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        catch (RestClientResponseException e) {
            System.out.println(e.getRawStatusCode());
        }

        return transfers;


    }

    public Transfer createTransfer(AuthenticatedUser currentUser, Transfer transfer) {
        try {
            transfer = restTemplate.postForObject(API_BASE_URL + "transfers",
                       makeTransferEntity(currentUser, transfer), Transfer.class);
        }
        catch (ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        catch (RestClientResponseException e) {
            System.out.println(e.getRawStatusCode());
        }
        return transfer;
    }

    private HttpEntity makeAuthEntity(AuthenticatedUser currentUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity entity = new HttpEntity(headers);
        return entity;
    }

    private HttpEntity<Transfer> makeTransferEntity(AuthenticatedUser currentUser, Transfer t) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(t, headers);
        return entity;
    }

}
