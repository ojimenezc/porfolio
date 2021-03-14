

/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.controllers;

import com.softcorp.cr.facturaelectronica.api.dtos.ClientRequestDto;
import com.softcorp.cr.facturaelectronica.api.entities.ClientEntity;
import com.softcorp.cr.facturaelectronica.api.entities.CustomerClientEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/clients")
public class ClientsController extends BaseController {
    protected Logger logger = LoggerFactory.getLogger(ClientsController.class);

    @PostMapping(value = "/save", headers = "Accept=application/json")
    public ClientEntity save(@RequestBody ClientRequestDto newClient) {
        try {
            boolean isUpdate = newClient.getClient().getId() > 0;
            ClientEntity newClientSaved = clientsRepository.save(newClient.getClient());

            if (newClientSaved.getId() > 0 && !isUpdate) {
                CustomerClientEntity customerClientEntity = new CustomerClientEntity();
                customerClientEntity.setApiCustomerId(newClient.getCustomer());
                customerClientEntity.setClientID(newClientSaved.getId());
                customersClientRepository.save(customerClientEntity);
            }

            return newClientSaved;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @PutMapping(value = "/update", headers = "Accept=application/json")
    public ClientEntity update(@RequestBody ClientRequestDto newClient) {
        try {
            boolean isUpdate = newClient.getClient().getId() > 0;
            if (!isUpdate) {
                throw new IllegalStateException("The client id is null. To update the id is required");
            }
            Optional<ClientEntity> existing = clientsRepository.findById(newClient.getClient().getId());
            if (existing.isPresent()) {
                return clientsRepository.save(newClient.getClient());
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The client with id = " + newClient.getClient().getId() + " does not exist");
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @GetMapping(value = "/get", headers = "Accept=application/json")
    public List<ClientEntity> get(int customerId) {
        try {
            return clientsRepository.getByCustomerId(customerId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping(value = "/delete", headers = "Accept=application/json")
    public boolean delete(int clientId) {
        try {
            ClientEntity clientToRemove = clientsRepository.getByClientId(clientId);
            if (null != clientToRemove) {
                clientsRepository.delete(clientToRemove);
                List<CustomerClientEntity> linksToDelete = customersClientRepository.getLinksToDelete(clientId);
                if (null != linksToDelete) {
                    customersClientRepository.deleteAll(linksToDelete);
                }
                return true;
            } else {
                logger.error("The client with id " + clientId + " was not found and could not be deleted");
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Client not found"
                );
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }
}
