/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softcorp.cr.facturaelectronica.api.dtos.*;
import com.softcorp.cr.facturaelectronica.api.dtos.bills.FacturaElectronica;
import com.softcorp.cr.facturaelectronica.api.entities.CompaniesEntity;
import com.softcorp.cr.facturaelectronica.api.entities.ConsecutivesEntity;
import com.softcorp.cr.facturaelectronica.api.exceptions.ElectronicBillingException;
import com.softcorp.cr.facturaelectronica.api.helpers.RestClient;
import com.softcorp.cr.facturaelectronica.api.helpers.interfaces.IRestClient;
import com.softcorp.cr.facturaelectronica.api.security.encription.Encrypter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.net.URI;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

@Configuration
@RestController
@RequestMapping("api/invoices")
public class InvoicingController extends BaseController {
    protected Logger logger = LoggerFactory.getLogger(InvoicingController.class);
    @Autowired
    private Environment environment;

    @Value("${documents.input}")
    private String documentsInput;
    @Value("${certficates.location}")
    private String certificateLocation;
    @Value("${hacienda.recepcion.uri}")
    private String haciendaRecepcionUri;


    @PostMapping(value = "/generate", headers = "Accept=application/json")
    public GenerateDocumentResponse create(@RequestBody BillGenerationRequest request) throws Exception {

        URI location = null;
        GenerateDocumentResponse generateDocumentResponse = new GenerateDocumentResponse();
        FacturaElectronica facturaElectronica = null;
        String base64Xml = null;
        try {

            Optional<CompaniesEntity> companyInformation = Optional.ofNullable(companiesRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new ElectronicBillingException("Company " + request.getCompanyId() + " was not found", "001")));
            if (!companyInformation.isPresent()) {
                String msg = String.format("Company information for ID %s was not found", request.getCompanyId());
                logger.error(msg);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
            }

            facturaElectronica = request.getDocument();

            File temp = File.createTempFile(facturaElectronica.getClave(), ".xml");
            String tempPath = temp.getPath();

            String xmlUnsigned = tempPath;
            Document sourceDoc = xmlHelper.getDocument(facturaElectronica, FacturaElectronica.class);
            Transformer transformer = xmlHelper.getTransformer();
            Result output = new StreamResult(new File(xmlUnsigned));
            Source input = new DOMSource(sourceDoc);

            transformer.transform(input, output);
            base64Xml = signer.sign(companyInformation.get(),
                    "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/facturaElectronica", tempPath);

            DocumentSendRequest sendRequest = new DocumentSendRequest();
            sendRequest.setClave(facturaElectronica.getClave());

            EmisorReceptorForPostRequest emisor = new EmisorReceptorForPostRequest();
            emisor.setNumeroIdentificacion(facturaElectronica.getEmisor().getIdentificacion().getNumero());
            emisor.setTipoIdentificacion(facturaElectronica.getEmisor().getIdentificacion().getTipo());
            sendRequest.setEmisor(emisor);

            EmisorReceptorForPostRequest receptor = new EmisorReceptorForPostRequest();
            receptor.setNumeroIdentificacion(facturaElectronica.getReceptor().getIdentificacion().getNumero());
            receptor.setTipoIdentificacion(facturaElectronica.getReceptor().getIdentificacion().getTipo());
            sendRequest.setReceptor(receptor);

            sendRequest.setComprobanteXml(base64Xml);
            sendRequest.setFecha(facturaElectronica.getFechaEmision().toGregorianCalendar().toInstant().toString());

            //SEND DOC TO HACIENDA
            String token = tokenService.getTokenFromHacienda(companyInformation.get().getAtvUsername(),
                    encrypter.decrypt(companyInformation.get().getAtvPassword()));

            IRestClient client = new RestClient(token);
            RestClientResponse response = client.post(haciendaRecepcionUri, sendRequest);

            if (null != response.getHeaders() && null != response.getHeaders().getLocation()) {
                location = response.getHeaders().getLocation();
            }
            logGeneratedDocumentInfo(location, facturaElectronica.getClave(), logger);
            generateDocumentResponse.setLocation(null != location ? location.getPath() : "");
            generateDocumentResponse.setXml(sendRequest.getComprobanteXml());

            ConsecutivesEntity currentConsecutive = consecutivesRepository.getCurrentConsecutiveInformation(request.getCompanyId(), request.getOfficeId(), request.getPosId(), request.getDocumentType());
            currentConsecutive.setConsecutive(facturaElectronica.getNumeroConsecutivo());
            currentConsecutive.setDocumentKey(facturaElectronica.getClave());
            currentConsecutive.setGenerationTimestamp(Timestamp.from(Instant.now()));
            consecutivesRepository.save(currentConsecutive);

            String jsonBase64 = "";
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(facturaElectronica);
            jsonBase64 = Base64.getEncoder().encodeToString(json.getBytes());
            documentsHelper.saveDocument(request.getCompanyId(), facturaElectronica.getClave(), jsonBase64, base64Xml);

        } catch (HttpClientErrorException.BadRequest badRequest) {
            logger.error("The document number " + (null != facturaElectronica ? facturaElectronica.getClave() : "") + " was not successfully sent to hacienda. Error cause = " + badRequest.getResponseHeaders().get("X-Error-Cause"));
            String errorMessage = badRequest.getResponseHeaders().get("X-Error-Cause").toString();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
        } catch (HttpClientErrorException.Forbidden fe) {
            HttpClientErrorException.Forbidden forbidden = fe;
            logger.error(forbidden.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return generateDocumentResponse;
    }
}
