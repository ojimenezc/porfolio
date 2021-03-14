

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
import java.math.BigInteger;

@Table(name = "document_types", schema = "facturaelectronica")
@Entity
public class DocumentsTypeEntity {
    @Id
    @GeneratedValue
    @JsonProperty(value = "id")
    private int id;
    @JsonProperty("documentName")
    @Column(name = "document_name")
    private String documentName;
    @JsonProperty("documentCode")
    @Column(name = "document_code")
    private BigInteger documentCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public BigInteger getDocumentCode() {
        return documentCode;
    }

    public void setDocumentCode(BigInteger documentCode) {
        this.documentCode = documentCode;
    }
}
