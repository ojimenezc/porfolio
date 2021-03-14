

/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.helpers.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softcorp.cr.facturaelectronica.api.dtos.RestClientResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

public interface IRestClient {
    void setRest(RestTemplate rest);

    RestClientResponse get(String endpoint, Object body);

    void delete(String endpoint);

    void delete(String endpoint, Object request);

    <T> T post(String endpoint, Object request) throws JsonProcessingException;

    void put(String endpoint, Object request);

    void setStatus(HttpStatus status);
}
