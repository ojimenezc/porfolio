

/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softcorp.cr.facturaelectronica.api.dtos.RestClientResponse;
import com.softcorp.cr.facturaelectronica.api.helpers.interfaces.IRestClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestClient implements IRestClient {

    private RestTemplate restTemplate = null;
    private HttpHeaders headers = null;
    HttpEntity<?> entity = null;

    public RestClient(String token) {
        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory();

        factory.setConnectTimeout(5000);
        factory.setReadTimeout(10000);

        restTemplate = new RestTemplate(factory);

        headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 Firefox/26.0");
        headers.set("Authorization", "Bearer " + token);
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*/*");
    }

    @Override
    public void setRest(RestTemplate rest) {
        this.restTemplate = rest;
    }


    @Override
    public RestClientResponse get(String endpoint, Object body) {

        entity = new HttpEntity<>(body, headers);
        HttpEntity<?> response = restTemplate.exchange(endpoint,
                HttpMethod.GET,
                entity, String.class);

        RestClientResponse restClientResponse = new RestClientResponse();
        restClientResponse.setHeaders(response.getHeaders());
        restClientResponse.setBody(response.getBody());
        return restClientResponse;
    }

    @Override
    public void delete(String endpoint) {

        restTemplate.delete(endpoint, headers);
    }

    @Override
    public void delete(String endpoint, Object request) {

        restTemplate.delete(endpoint, request, headers);
    }


    @Override
    public RestClientResponse post(String endpoint, Object request) throws JsonProcessingException {

        entity = new HttpEntity<>(request, headers);
        HttpEntity<?> response = restTemplate.exchange(endpoint,
                HttpMethod.POST,
                entity, String.class);

        RestClientResponse restClientResponse = new RestClientResponse();
        restClientResponse.setHeaders(response.getHeaders());
        restClientResponse.setBody(response.getBody());
        return restClientResponse;
    }

    @Override
    public void put(String endpoint, Object request) {

        restTemplate.put(endpoint, request);
    }

    @Override
    public void setStatus(HttpStatus status) {
        //This is not needed, no status being used
    }

}

