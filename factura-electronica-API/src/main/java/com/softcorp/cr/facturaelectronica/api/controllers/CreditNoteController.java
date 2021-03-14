

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
import com.softcorp.cr.facturaelectronica.api.dtos.creditnotes.NotaCreditoElectronica;
import com.softcorp.cr.facturaelectronica.api.entities.CompaniesEntity;
import com.softcorp.cr.facturaelectronica.api.entities.ConsecutivesEntity;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping("api/credinotes")
public class CreditNoteController extends BaseController {
    protected Logger logger = LoggerFactory.getLogger(CreditNoteController.class);
    @Autowired
    private Environment environment;

    @Value("${documents.input}")
    private String documentsInput;
    @Value("${certficates.location}")
    private String certificateLocation;
    @Value("${hacienda.recepcion.uri}")
    private String haciendaRecepcionUri;


    @GetMapping(value = "/generate", headers = "Accept=application/json")
    public GenerateDocumentResponse create(@RequestBody CreditNoteRequest request) {

        URI location = null;
        GenerateDocumentResponse generateDocumentResponse = new GenerateDocumentResponse();
        NotaCreditoElectronica notaCreditoElectronica = null;
        String base64Xml = null;
        try {

            Optional<CompaniesEntity> companyInformation = getCompanyInformation(request.getCompanyId());

            if (!companyInformation.isPresent()) {
                String msg = String.format("Company information for ID %s was not found", request.getCompanyId());
                logger.error(msg);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, msg);
            }

            notaCreditoElectronica = request.getDocument();

            File temp = File.createTempFile(notaCreditoElectronica.getClave(), ".xml");
            String tempPath = temp.getPath();

            String xmlUnsigned = tempPath;
            Document sourceDoc = xmlHelper.getDocument(notaCreditoElectronica, NotaCreditoElectronica.class);
            Transformer transformer = xmlHelper.getTransformer();
            Result output = new StreamResult(new File(xmlUnsigned));
            Source input = new DOMSource(sourceDoc);

            transformer.transform(input, output);
            if (companyInformation.isPresent()) {
                base64Xml = signer.sign(companyInformation.get(),
                        "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaCreditoElectronica", tempPath);
            }

            DocumentSendRequest sendRequest = new DocumentSendRequest();
            sendRequest.setClave(notaCreditoElectronica.getClave());

            EmisorReceptorForPostRequest emisor = new EmisorReceptorForPostRequest();
            emisor.setNumeroIdentificacion(notaCreditoElectronica.getEmisor().getIdentificacion().getNumero());
            emisor.setTipoIdentificacion(notaCreditoElectronica.getEmisor().getIdentificacion().getTipo());
            sendRequest.setEmisor(emisor);

            EmisorReceptorForPostRequest receptor = new EmisorReceptorForPostRequest();
            receptor.setNumeroIdentificacion(notaCreditoElectronica.getReceptor().getIdentificacion().getNumero());
            receptor.setTipoIdentificacion(notaCreditoElectronica.getReceptor().getIdentificacion().getTipo());
            sendRequest.setReceptor(receptor);

            sendRequest.setComprobanteXml(base64Xml);
            sendRequest.setFecha(notaCreditoElectronica.getFechaEmision().toGregorianCalendar().toInstant().toString());

            //SEND DOC TO HACIENDA

            String token = tokenService.getTokenFromHacienda(companyInformation.get().getAtvUsername(),
                    encrypter.decrypt(companyInformation.get().getAtvPassword()));

            IRestClient client = new RestClient(token);
            RestClientResponse response = client.post(haciendaRecepcionUri, sendRequest);

            if (null != response.getHeaders() && null != response.getHeaders().getLocation()) {
                location = response.getHeaders().getLocation();
            }

            logGeneratedDocumentInfo(location, notaCreditoElectronica.getClave(),logger);
            generateDocumentResponse.setLocation(null != location ? location.getPath() : "");
            generateDocumentResponse.setXml(sendRequest.getComprobanteXml());

            ConsecutivesEntity currentConsecutive = consecutivesRepository.getCurrentConsecutiveInformation(request.getCompanyId(), request.getOfficeId(), request.getPosId(), request.getDocumentType());
            currentConsecutive.setConsecutive(notaCreditoElectronica.getNumeroConsecutivo());
            currentConsecutive.setDocumentKey(notaCreditoElectronica.getClave());
            currentConsecutive.setGenerationTimestamp(Timestamp.from(Instant.now()));
            consecutivesRepository.save(currentConsecutive);

            String jsonBase64 = "";
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(notaCreditoElectronica);
            jsonBase64 = Base64.getEncoder().encodeToString(json.getBytes());
            documentsHelper.saveDocument(request.getCompanyId(), notaCreditoElectronica.getClave(), jsonBase64, base64Xml);

        } catch (HttpClientErrorException.BadRequest badRequest) {
            logger.error("The document number " + (null != notaCreditoElectronica ? notaCreditoElectronica.getClave() : "") + " was not successfully sent to hacienda. Error cause = " + badRequest.getResponseHeaders().get("X-Error-Cause"), badRequest);
            String errorMessage = badRequest.getResponseHeaders().get("X-Error-Cause").toString();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
        } catch (HttpClientErrorException.Forbidden fe) {
            logger.error(fe.getResponseBodyAsString(), fe);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred generating the document");
        }

        return generateDocumentResponse;
    }

    private Optional<CompaniesEntity> getCompanyInformation(int id) {
        String notFoundMessage = String.format("Company %s was not found", id);
        return Optional.ofNullable(companiesRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, notFoundMessage)));
    }
}
