

/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Table(name = "documents", schema = "facturaelectronica")
@Entity
public class DocumentsEntity {
    @Id
    @GeneratedValue
    @JsonProperty(value = "id")
    private int id;

    @JsonProperty("documentKey")
    @Column(name = "document_key")
    private String key;

    @JsonProperty("generationDate")
    @Column(name = "generation_date")
    private Timestamp generationDate;

    @JsonProperty("companyId")
    @Column(name = "company_id")
    private int companyId;

    @JsonProperty("statusIndicator")
    @Column(name = "status_indicator")
    private String statusIndicator;

    @JsonProperty("statusRequestDate")
    @Column(name = "status_request_date")
    private Timestamp statusRequestDate;

    @JsonProperty("jsonbase64")
    @Column(name = "json_Base64")
    private String JSONBase64;

    @JsonProperty("xmlbase64")
    @Column(name = "xml_Base64")
    private String XMLBase64;

    @JsonProperty("haciendaReceiptXML")
    @Column(name = "hacienda_Receipt_XML")
    private String haciendaReceiptXML;

    @JsonProperty("pdfBase64")
    @Column(name = "pdf_Base_64")
    private String pdfBase64;

    @JsonProperty("emailStatus")
    @Column(name = "email_status")
    private int emailStatus;

    @JsonProperty("emailSentDate")
    @Column(name = "email_sent_date")
    private Instant emailSentDate;

    public String getPdfBase64() {
        return pdfBase64;
    }

    public void setPdfBase64(String pdfBase64) {
        this.pdfBase64 = pdfBase64;
    }

    public int getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(int emailStatus) {
        this.emailStatus = emailStatus;
    }

    public Instant getEmailSentDate() {
        return emailSentDate;
    }

    public void setEmailSentDate(Instant emailSentDate) {
        this.emailSentDate = emailSentDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Timestamp getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(Timestamp generationDate) {
        this.generationDate = generationDate;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getStatusIndicator() {
        return statusIndicator;
    }

    public void setStatusIndicator(String statusIndicator) {
        this.statusIndicator = statusIndicator;
    }

    public Timestamp getStatusRequestDate() {
        return statusRequestDate;
    }

    public void setStatusRequestDate(Timestamp statusRequestDate) {
        this.statusRequestDate = statusRequestDate;
    }

    public String getJSONBase64() {
        return JSONBase64;
    }

    public void setJSONBase64(String jsonBase64) {
        this.JSONBase64 = jsonBase64;
    }

    public String getXMLBase64() {
        return XMLBase64;
    }

    public void setXMLBase64(String xmlbase64) {
        this.XMLBase64 = xmlbase64;
    }

    public String getHaciendaReceiptXML() {
        return haciendaReceiptXML;
    }

    public void setHaciendaReceiptXML(String haciendaReceiptXML) {
        this.haciendaReceiptXML = haciendaReceiptXML;
    }
}

