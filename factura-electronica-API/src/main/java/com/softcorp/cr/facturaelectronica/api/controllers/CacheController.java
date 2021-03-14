/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/cache")
public class CacheController extends BaseController {
    @Autowired
    private CacheManager cacheManager;

    @PostMapping(value = "/purge", headers = "Accept=application/json")
    public ResponseEntity<String> purge() {
        if (null != cacheManager) {
            cacheManager.getCacheNames().parallelStream().forEach(name -> cacheManager.getCache(name).clear());
        } else {
            return new ResponseEntity<>("Cache Manager Null", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok("Cache purged successfully");
    }
}
