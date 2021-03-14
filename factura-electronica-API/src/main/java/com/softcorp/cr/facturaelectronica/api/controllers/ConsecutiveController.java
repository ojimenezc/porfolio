

/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.controllers;

import com.softcorp.cr.facturaelectronica.api.dtos.NextKeyResponse;
import com.softcorp.cr.facturaelectronica.api.entities.CompaniesEntity;
import com.softcorp.cr.facturaelectronica.api.entities.ConsecutivesEntity;
import com.softcorp.cr.facturaelectronica.api.entities.CountriesEntity;
import com.softcorp.cr.facturaelectronica.api.entities.OfficesEntity;
import com.softcorp.cr.facturaelectronica.api.helpers.BillingSecurityCodeGenerator;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/consecutives")
public class ConsecutiveController extends BaseController {
    protected Logger logger = LoggerFactory.getLogger(ConsecutiveController.class);

    @GetMapping(value = "/generate", headers = "Accept=application/json")
    public ResponseEntity<NextKeyResponse> getNextDocumentID(int companyId, int officeId, int posId, int documentType, int documentSituation) {
        return getNextDocumentKey(companyId, officeId, posId, documentType, documentSituation);
    }


    private ResponseEntity<NextKeyResponse> getNextDocumentKey(int companyId, int officeId, int posId, int documentType, int documentSituation) {
        try {

            String nextID = "";
            int consecutive = 0;
            ConsecutivesEntity currentConsecutive = null;
            String nextConsecutive = null;
            Optional<CompaniesEntity> company = null;
            Optional<OfficesEntity> officesEntity;
            Optional<CountriesEntity> countriesEntity;

            officesEntity = officesRepository.findById(officeId);
            if (!officesEntity.isPresent()) {
                String msg = "[10003] The office with id " + officeId + " does not exists.";
                logger.error(msg);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
            }

            //Information gathering
            company = companiesRepository.findById(companyId);
            if (!company.isPresent()) {
                String msg = "[10004] The company with id " + companyId + " was not found in the database";
                logger.error(msg);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
            }

            countriesEntity = countriesRepository.findById(company.get().getCountryId());
            if (!countriesEntity.isPresent()) {
                String msg = "[10001] The county id " + companyId + " provided was not found in the database";
                logger.error(msg);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
            }

            currentConsecutive = consecutivesRepository.getCurrentConsecutiveInformation(companyId, officeId, posId, documentType);

            if (currentConsecutive == null) {
                //if consecutive is null then create it
                ConsecutivesEntity newConsecutive = new ConsecutivesEntity();
                newConsecutive.setCompanyID(companyId);
                newConsecutive.setOfficeID(officeId);
                newConsecutive.setPosID(posId);
                newConsecutive.setDocumentType(documentType);
                nextConsecutive = officesEntity.get().getCode() +
                        String.format("%05d", posId) +
                        String.format("%02d", documentType) +
                        String.format("%010d", 1);
                newConsecutive.setConsecutive(nextConsecutive);
                newConsecutive.setDocumentKey("0");
                newConsecutive.setGenerationTimestamp(Timestamp.from(Instant.now()));
                consecutivesRepository.save(newConsecutive);
            } else {
                if (currentConsecutive.getDocumentKey().equals("0")) {
                    nextConsecutive = currentConsecutive.getConsecutive();
                } else {
                    consecutive = Integer.parseInt(currentConsecutive.getConsecutive().substring(10, currentConsecutive.getConsecutive().length()));
                    nextConsecutive = officesEntity.get().getCode() +
                            String.format("%05d", posId) +
                            String.format("%02d", documentType) +
                            String.format("%010d", ++consecutive);
                }
            }

            nextID = countriesEntity.get().getCode() +
                    String.format("%02d", LocalDate.now().getDayOfMonth()) +
                    String.format("%02d", LocalDate.now().getMonth().getValue()) +
                    String.valueOf(LocalDate.now().getYear()).substring(2, 4) +
                    StringUtils.leftPad(company.get().getIdentificationNumber(), 12, "0") +
                    nextConsecutive +
                    documentSituation +
                    BillingSecurityCodeGenerator.getSecurityCode();

            NextKeyResponse response = new NextKeyResponse();
            response.setConsecutive(nextConsecutive);
            response.setKey(nextID);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating next document key");
        }
    }

    @GetMapping(value = "/get", headers = "Accept=application/json")
    public List<ConsecutivesEntity> companyConsecutives(int companyId, int officeId, int posId) {
        try {
            return consecutivesRepository.getCompanyConsecutives(companyId, officeId, posId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping(value = "/updatelist", headers = "Accept=application/json")
    public List<ConsecutivesEntity> updateList(@RequestBody List<ConsecutivesEntity> toUpdate) {
        try {
            if (null == toUpdate) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The request body is null");
            }

            toUpdate.stream().forEach(item -> {
                if (null == item.getGenerationTimestamp()) {
                    item.setGenerationTimestamp(Timestamp.from(Instant.now()));
                }
            });

            consecutivesRepository.saveAll(toUpdate);
            return toUpdate;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }
}
