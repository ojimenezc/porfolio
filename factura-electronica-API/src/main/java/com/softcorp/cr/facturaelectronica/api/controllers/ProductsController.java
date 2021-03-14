/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.controllers;

import com.softcorp.cr.facturaelectronica.api.dtos.ProductsSaveRequest;
import com.softcorp.cr.facturaelectronica.api.entities.ProductsEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/products")
public class ProductsController extends BaseController {
    protected Logger logger = LoggerFactory.getLogger(ProductsController.class);

    @PostMapping(value = "/save", headers = "Accept=application/json")
    public ProductsEntity save(@RequestBody ProductsSaveRequest productsSaveRequest) {
        try {
            ProductsEntity productsEntity = productsSaveRequest;
            return productsRepository.save(productsEntity);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @GetMapping(value = "/get", headers = "Accept=application/json")
    public Iterable<ProductsEntity> get(int customerId) {
        try {
            return productsRepository.getByCustomer(customerId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @GetMapping(value = "/getsingle", headers = "Accept=application/json")
    public ProductsEntity getsingle(int id) {
        try {
            return productsRepository.getSingle(id);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
