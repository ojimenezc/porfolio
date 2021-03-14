

/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class StatusResponse {
    @JsonProperty("clave")
    private String key;
    @JsonProperty("fecha")
    private Timestamp dateTime;
    @JsonProperty("ind-estado")
    private String statusIndicator;

    public String getRespuestaXML() {
        return respuestaXML;
    }

    public void setRespuestaXML(String respuestaXML) {
        this.respuestaXML = respuestaXML;
    }

    @JsonProperty("respuesta-xml")
    private String respuestaXML;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public String getStatusIndicator() {
        return statusIndicator;
    }

    public void setStatusIndicator(String statusIndicator) {
        this.statusIndicator = statusIndicator;
    }
}
