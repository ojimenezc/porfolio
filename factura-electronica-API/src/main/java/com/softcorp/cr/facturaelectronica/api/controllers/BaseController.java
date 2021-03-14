

/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.controllers;

import com.softcorp.cr.facturaelectronica.api.helpers.DocumentsHelper;
import com.softcorp.cr.facturaelectronica.api.repositories.*;
import com.softcorp.cr.facturaelectronica.api.security.encription.Encrypter;
import com.softcorp.cr.facturaelectronica.api.services.implementations.TokenService;
import com.softcorp.cr.facturaelectronica.api.signer.XMLSigner;
import com.softcorp.cr.facturaelectronica.api.signer.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;

@CrossOrigin(origins = "*")
@RestController
public class BaseController {
    protected Logger logger = LoggerFactory.getLogger(BaseController.class);
    @Autowired
    DocumentsHelper documentsHelper;
    @Autowired
    CustomersRepository customersRepository;
    @Autowired
    ConsecutivesRepository consecutivesRepository;
    @Autowired
    CompaniesRepository companiesRepository;
    @Autowired
    OfficesRepository officesRepository;
    @Autowired
    CountriesRepository countriesRepository;
    @Autowired
    DocumentsRepository documentsRepository;
    @Autowired
    DocumentTypesRepository documentTypesRepository;
    @Autowired
    CustomerCompaniesRepository customerCompaniesRepository;
    @Autowired
    IdentificationTypesRepository identificationTypesRepository;
    @Autowired
    ProvincesRepository provincesRepository;
    @Autowired
    CountiesRepository countiesRepository;
    @Autowired
    NeighborhoodRepository neighborhoodRepository;
    @Autowired
    DistrictsRepository districtsRepository;
    @Autowired
    ClientsRepository clientsRepository;
    @Autowired
    CustomersClientRepository customersClientRepository;
    @Autowired
    CurrenciesRepository currenciesRepository;
    @Autowired
    PointOfSalesRepository pointOfSalesRepository;
    @Autowired
    TaxesRepository taxesRepository;
    @Autowired
    UnitMeasuresRepository unitMeasuresRepository;
    @Autowired
    ProductsRepository productsRepository;
    @Autowired
    RatesRepository ratesRepository;
    @Autowired
    EconomicActivitiesRepository economicActivitiesRepository;
    @Autowired
    CompanyActivitiesRepository companyActivitiesRepository;
    @Autowired
    XMLSigner signer;
    @Autowired
    TokenService tokenService;
    @Autowired
    XmlHelper xmlHelper;

    @Autowired
    Encrypter encrypter;


    void logGeneratedDocumentInfo(URI location, String key, Logger classLogger) throws IOException {
        String infoMsg = String.format("Document number %s was sent to Hacienda. Location to validate document  = [ %s ]", key,
                null != location ? location.toString() : "");
        classLogger.info(infoMsg);
    }
}
