

/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.dtos;

public class DocumentSendRequest {
    private String clave;
    private String fecha;
    private EmisorReceptorForPostRequest emisor;
    private EmisorReceptorForPostRequest receptor;
    private String comprobanteXml;

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public EmisorReceptorForPostRequest getEmisor() {
        return emisor;
    }

    public void setEmisor(EmisorReceptorForPostRequest emisor) {
        this.emisor = emisor;
    }

    public EmisorReceptorForPostRequest getReceptor() {
        return receptor;
    }

    public void setReceptor(EmisorReceptorForPostRequest receptor) {
        this.receptor = receptor;
    }

    public String getComprobanteXml() {
        return comprobanteXml;
    }

    public void setComprobanteXml(String comprobanteXml) {
        this.comprobanteXml = comprobanteXml;
    }
}
