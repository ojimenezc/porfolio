
/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.controllers;

import com.softcorp.cr.facturaelectronica.api.dtos.POSSaveRequest;
import com.softcorp.cr.facturaelectronica.api.entities.PointOfSaleEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Configuration
@RestController
@RequestMapping("api/pos")
public class PointsOfSaleController extends BaseController {
    protected Logger logger = LoggerFactory.getLogger(BaseController.class);

    @GetMapping(value = "/get", headers = "Accept=application/json")
    public List<PointOfSaleEntity> get(final int officeId) {
        try {
            return pointOfSalesRepository.getByOfficeId(officeId);
        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping(value = "/save", headers = "Accept=application/json")
    public ResponseEntity<PointOfSaleEntity> save(@RequestParam final POSSaveRequest posSaveRequest) {
        try {
            PointOfSaleEntity request = posSaveRequest;
            if (null != request) {
                final PointOfSaleEntity response = pointOfSalesRepository.save(request);
                return ResponseEntity.ok(response);
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The request to save the POS is null");
            }
        } catch (final Exception e) {
            logger.error("Error saving Point of Sale", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error saving the POS");
        }
    }
}
