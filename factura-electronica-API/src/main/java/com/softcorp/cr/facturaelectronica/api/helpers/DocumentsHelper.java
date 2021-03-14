/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.helpers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softcorp.cr.facturaelectronica.api.controllers.BaseController;
import com.softcorp.cr.facturaelectronica.api.dtos.RestClientResponse;
import com.softcorp.cr.facturaelectronica.api.dtos.StatusResponse;
import com.softcorp.cr.facturaelectronica.api.entities.CompaniesEntity;
import com.softcorp.cr.facturaelectronica.api.entities.DocumentsEntity;
import com.softcorp.cr.facturaelectronica.api.exceptions.ElectronicBillingException;
import com.softcorp.cr.facturaelectronica.api.helpers.interfaces.IRestClient;
import com.softcorp.cr.facturaelectronica.api.repositories.CompaniesRepository;
import com.softcorp.cr.facturaelectronica.api.repositories.DocumentsRepository;
import com.softcorp.cr.facturaelectronica.api.security.encription.Encrypter;
import com.softcorp.cr.facturaelectronica.api.services.implementations.TokenService;
import org.dom4j.DocumentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Configuration
@ComponentScan("com.softcorp.cr.facturaelectronica.api.helpers")
public class DocumentsHelper {
    protected Logger logger = LoggerFactory.getLogger(DocumentsHelper.class);

    @Autowired
    DocumentsRepository documentsRepository;
    @Autowired
    Encrypter encrypter;
    @Autowired
    TokenService tokenService;
    @Autowired
    CompaniesRepository companiesRepository;

    private ObjectMapper objectMapper = new ObjectMapper().
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Value("${hacienda.status.uri}")
    private String statusRequestUri;

    public void saveDocument(int companyId, String key, String jsonBase64, String xmlBase64) {
        DocumentsEntity newDocumentRegistry = new DocumentsEntity();
        newDocumentRegistry.setCompanyId(companyId);
        newDocumentRegistry.setGenerationDate(Timestamp.from(Instant.now()));
        newDocumentRegistry.setKey(key);
        newDocumentRegistry.setJSONBase64(jsonBase64);
        newDocumentRegistry.setXMLBase64(xmlBase64);
        newDocumentRegistry.setEmailStatus(0);
        documentsRepository.save(newDocumentRegistry);
    }

    public StatusResponse processStatus(String documentId) {
        StatusResponse statusResponse = null;
        try {
            DocumentsEntity document = documentsRepository.getByDocumentKey(documentId);
            if (document == null) {
                throw new ElectronicBillingException("The document with id " + documentId + " was not found in the database", "000");
            }

            Optional<CompaniesEntity> companyInformation = Optional.ofNullable(companiesRepository.findById(document.getCompanyId())
                    .orElseThrow(() -> new ElectronicBillingException("Company " + document.getCompanyId() + " was not found", "001")));

            if (companyInformation.isPresent()) {
                String token = tokenService.getTokenFromHacienda(companyInformation.get().getAtvUsername(),
                        encrypter.decrypt(companyInformation.get().getAtvPassword()));

                IRestClient client = new RestClient(token);
                RestClientResponse response = client.get(statusRequestUri + documentId, null);

                statusResponse = objectMapper.readValue(response.getBody().toString(), StatusResponse.class);

                document.setStatusIndicator(statusResponse.getStatusIndicator());
                document.setStatusRequestDate(statusResponse.getDateTime());
                document.setHaciendaReceiptXML(statusResponse.getRespuestaXML());
                documentsRepository.save(document);
            }

        } catch (HttpClientErrorException.BadRequest badRequest) {
            logger.error("The document number " + documentId + " was not successfully sent to hacienda. Error cause = " + badRequest.getResponseHeaders().get("X-Error-Cause"), badRequest);
            String errorMessage = badRequest.getResponseHeaders().get("X-Error-Cause").toString();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
        } catch (HttpClientErrorException.Forbidden fe) {
            HttpClientErrorException.Forbidden forbidden = fe;
            logger.error(forbidden.getResponseBodyAsString(), fe);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return statusResponse;
    }

}
