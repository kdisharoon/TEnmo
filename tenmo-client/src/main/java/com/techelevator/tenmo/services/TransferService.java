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

public class TransferService {
    private final String API_BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();

    public TransferService(String apiUrl) {
        this.API_BASE_URL = apiUrl;

    }

    public Transfer getTransferById(AuthenticatedUser currentUser, Integer transferId) {

        Transfer transfer = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity entity = new HttpEntity(headers);

        try {
            transfer = restTemplate.exchange(API_BASE_URL + "transfers/" + transferId,
                                             HttpMethod.GET, entity, Transfer.class).getBody();
        }
        catch (Exception e) {
            System.out.println("getTransferById exception!");
        }

        return transfer;

    }

    public Transfer[] getAllTransfers(AuthenticatedUser currentUser) {

        Transfer[] transfers = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity entity = new HttpEntity(headers);

        Integer userId = currentUser.getUser().getId();

        try {
            transfers = restTemplate.exchange(API_BASE_URL + "transfers/" + userId,
                                              HttpMethod.GET, entity, Transfer[].class).getBody();
        }
        catch (Exception e) {
            System.out.println("getAllTransfers exception!");
        }

        return transfers;

    }






}
