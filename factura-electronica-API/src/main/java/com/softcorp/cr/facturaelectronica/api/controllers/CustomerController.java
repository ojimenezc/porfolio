
/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.controllers;

import com.softcorp.cr.facturaelectronica.api.dtos.CustomerCompanyRequest;
import com.softcorp.cr.facturaelectronica.api.dtos.CustomersCreateRequest;
import com.softcorp.cr.facturaelectronica.api.entities.CustomerCompanyEntity;
import com.softcorp.cr.facturaelectronica.api.entities.CustomersEntity;
import com.softcorp.cr.facturaelectronica.api.security.encription.Encrypter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/customers")
public class CustomerController extends BaseController {
    protected Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @PostMapping(value = "/save", headers = "Accept=application/json")
    public ResponseEntity<CustomersEntity> create(@RequestBody CustomersCreateRequest customersCreateRequest) {
        try {
            CustomersEntity customersEntity = customersCreateRequest;

            customersEntity.setPassword(encrypter.encrypt(customersEntity.getPassword()));
            customersEntity.setUsername(encrypter.encrypt(customersEntity.getUsername()));
            customersRepository.save(customersEntity);
            customersEntity.setPassword(null);
            customersEntity.setBiometrics(null);
            return ResponseEntity.ok(customersEntity);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PostMapping(value = "/linkcompany", headers = "Accept=application/json")
    public CustomerCompanyEntity linkCompany(@RequestBody CustomerCompanyRequest customerCompanyRequest) {
        CustomerCompanyEntity customersEntity = customerCompanyRequest;
        customerCompaniesRepository.save(customersEntity);
        return customersEntity;
    }

    @GetMapping(value = "/login", headers = "Accept=application/json")
    public Object login(String username, String password) {
        try {

            return customersRepository.getByUsernameAndPassword(encrypter.encrypt(username),
                    encrypter.encrypt(password));
        } catch (Exception e) {
            logger.error("Error login in user " + e.getMessage(), e);
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error login in user");
        }
    }
}
