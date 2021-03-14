/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.controllers;

import com.softcorp.cr.facturaelectronica.api.entities.TaxesEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/taxes")
public class TaxesController extends BaseController {
    protected Logger logger = LoggerFactory.getLogger(TaxesController.class);

    @Cacheable(cacheNames = "taxes")
    @GetMapping(value = "/get", headers = "Accept=application/json")
    public Iterable<TaxesEntity> get() {
        try {
            return taxesRepository.findAll();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
