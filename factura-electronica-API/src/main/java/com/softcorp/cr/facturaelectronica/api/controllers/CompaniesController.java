

/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.controllers;

import com.softcorp.cr.facturaelectronica.api.dtos.CompaniesSaveRequestDTO;
import com.softcorp.cr.facturaelectronica.api.entities.CompaniesEntity;
import com.softcorp.cr.facturaelectronica.api.entities.CustomerCompanyEntity;
import com.softcorp.cr.facturaelectronica.api.entities.DocumentsEntity;
import com.softcorp.cr.facturaelectronica.api.security.encription.Encrypter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/companies")
public class CompaniesController extends BaseController {
    protected Logger logger = LoggerFactory.getLogger(CompaniesController.class);

    @PostMapping(value = "/save", headers = "Accept=application/json")
    public CompaniesEntity save(@RequestBody CompaniesSaveRequestDTO request) {
        try {

            CompaniesEntity existing = companiesRepository.getCompanyByIdentificationNumber(request.getCompany().getIdentificationNumber());
            if (null != existing) {
                return update(request);
            } else {

                CompaniesEntity newCompany = request.getCompany();
                boolean isUpdate = newCompany.getId() > 0;
                newCompany.setAtvPassword(encrypter.encrypt(request.getCompany().getAtvPassword()));
                newCompany.setCertificatePin(encrypter.encrypt(request.getCompany().getCertificatePin()));
                newCompany.setAtvCertificate(encrypter.encrypt(request.getCompany().getAtvCertificate()));
                newCompany.setActive(true);
                CompaniesEntity newCompanySaved = companiesRepository.save(newCompany);
                if (newCompanySaved.getId() > 0 && !isUpdate) {
                    CustomerCompanyEntity customerCompany = new CustomerCompanyEntity();
                    customerCompany.setCompanyId(newCompanySaved.getId());
                    customerCompany.setCustomerId(request.getCustomerId());
                    customerCompaniesRepository.save(customerCompany);
                }
                newCompanySaved.setCertificatePin(encrypter.decrypt(newCompanySaved.getCertificatePin()));
                newCompanySaved.setAtvPassword(encrypter.decrypt(newCompanySaved.getAtvPassword()));
                newCompanySaved.setAtvCertificate(encrypter.decrypt(newCompanySaved.getAtvCertificate()));
                return newCompanySaved;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public CompaniesEntity update(CompaniesSaveRequestDTO request) {
        try {
            boolean isUpdate = request.getCompany().getId() > 0;
            request.getCompany().setAtvPassword(encrypter.encrypt(request.getCompany().getAtvPassword()));
            request.getCompany().setCertificatePin(encrypter.encrypt(request.getCompany().getCertificatePin()));
            request.getCompany().setAtvCertificate(encrypter.encrypt(request.getCompany().getAtvCertificate()));
            if (!isUpdate) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The company id is null. To update the id is required");
            }
            Optional<CompaniesEntity> existing = companiesRepository.findById(request.getCompany().getId());
            if (existing.isPresent()) {

                CompaniesEntity newCompanySaved = companiesRepository.save(request.getCompany());
                newCompanySaved.setCertificatePin(encrypter.decrypt(newCompanySaved.getCertificatePin()));
                newCompanySaved.setAtvPassword(encrypter.decrypt(newCompanySaved.getAtvPassword()));
                newCompanySaved.setAtvCertificate(encrypter.decrypt(newCompanySaved.getAtvCertificate()));
                return newCompanySaved;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The compnay with ID = " + request.getCompany().getId() + " does not exist");
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @GetMapping(value = "/get", headers = "Accept=application/json")
    public List<CompaniesEntity> get(int customerId) {
        try {
            List<CompaniesEntity> returnValue = companiesRepository.getCompaniesByCustomer(customerId);
            returnValue.stream().forEach(company -> {
                company.setCertificatePin(encrypter.decrypt(company.getCertificatePin()));
                company.setAtvPassword(encrypter.decrypt(company.getAtvPassword()));
                company.setAtvCertificate(encrypter.decrypt(company.getAtvCertificate()));
            });
            return returnValue;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping(value = "/getcompanyinfo", headers = "Accept=application/json")
    public CompaniesEntity getById(int companyId) {
        try {
            Optional<CompaniesEntity> companyInfo = companiesRepository.findById(companyId);
            if (companyInfo.isPresent()) {
                CompaniesEntity returnValue = companyInfo.get();
                returnValue.setCertificatePin(encrypter.decrypt(returnValue.getCertificatePin()));
                returnValue.setAtvPassword(encrypter.decrypt(returnValue.getAtvPassword()));
                returnValue.setAtvCertificate(encrypter.decrypt(returnValue.getAtvCertificate()));
                return returnValue;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The company with id = " + companyId + " was not found");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }


    @DeleteMapping(value = "/delete", headers = "Accept=application/json")
    public boolean delete(int companyId) {
        try {
            Optional<CompaniesEntity> companyToRemove = companiesRepository.findById(companyId);
            if (companyToRemove.isPresent()) {
                companyToRemove.get().setActive(false);
                companiesRepository.save(companyToRemove.get());
                return true;
            } else {
                logger.error("The company with id " + companyId + " was not found and could not be deleted");
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Client not found"
                );
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping(value = "/documents", headers = "Accept=application/json")
    public List<DocumentsEntity> getDocuments(int companyId) {
        return documentsRepository.getByCompany(companyId);
    }
}
