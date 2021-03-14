

/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.controllers;

import com.softcorp.cr.facturaelectronica.api.dtos.CompanyActivityRequest;
import com.softcorp.cr.facturaelectronica.api.entities.CompanyActivityEntity;
import com.softcorp.cr.facturaelectronica.api.entities.EconomicActivityEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/activities")
public class EconomicActivitiesController extends BaseController {
    protected Logger logger = LoggerFactory.getLogger(EconomicActivitiesController.class);

    @Cacheable("activities")
    @GetMapping(value = "/get", headers = "Accept=application/json")
    public List<EconomicActivityEntity> get(int offset, int pageSize) {
        try {
            return economicActivitiesRepository.getAllOffset(offset, pageSize);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }


    @GetMapping(value = "/getbycompany", headers = "Accept=application/json")
    public List<CompanyActivityEntity> getbycompany(int companyId) {
        try {
            return companyActivitiesRepository.getByCompany(companyId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping(value = "/addtocompany", headers = "Accept=application/json")
    public List<CompanyActivityEntity> addtocompany(@RequestBody CompanyActivityRequest companyActivityRequest) {
        try {
            CompanyActivityEntity companyActivityEntity = companyActivityRequest;
            companyActivitiesRepository.save(companyActivityEntity);
            return companyActivitiesRepository.getByCompany(companyActivityEntity.getCompanyId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @DeleteMapping(value = "/removefromcompany", headers = "Accept=application/json")
    public boolean removefromcompany(int companyId, String code) {
        try {
            CompanyActivityEntity toDelete = companyActivitiesRepository.getByCompanyAndCode(companyId, code);
            if (null != toDelete) {
                companyActivitiesRepository.delete(toDelete);
            }
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }
}
