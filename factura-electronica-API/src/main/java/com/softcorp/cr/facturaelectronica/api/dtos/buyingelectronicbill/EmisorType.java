


/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.dtos.buyingelectronicbill;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EmisorType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="EmisorType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Nombre">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Identificacion" type="{https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/facturaElectronicaCompra}IdentificacionType"/>
 *         &lt;element name="NombreComercial" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="80"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Ubicacion" type="{https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/facturaElectronicaCompra}UbicacionType"/>
 *         &lt;element name="Telefono" type="{https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/facturaElectronicaCompra}TelefonoType" minOccurs="0"/>
 *         &lt;element name="Fax" type="{https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/facturaElectronicaCompra}TelefonoType" minOccurs="0"/>
 *         &lt;element name="CorreoElectronico">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="160"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EmisorType", namespace = "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/facturaElectronicaCompra", propOrder = {
        "nombre",
        "identificacion",
        "nombreComercial",
        "ubicacion",
        "telefono",
        "fax",
        "correoElectronico"
})
public class EmisorType {

    @XmlElement(name = "Nombre", namespace = "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/facturaElectronicaCompra", required = true)
    protected String nombre;
    @XmlElement(name = "Identificacion", namespace = "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/facturaElectronicaCompra", required = true)
    protected IdentificacionType identificacion;
    @XmlElement(name = "NombreComercial", namespace = "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/facturaElectronicaCompra")
    protected String nombreComercial;
    @XmlElement(name = "Ubicacion", namespace = "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/facturaElectronicaCompra", required = true)
    protected UbicacionType ubicacion;
    @XmlElement(name = "Telefono", namespace = "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/facturaElectronicaCompra", required = false)
    protected TelefonoType telefono;
    @XmlElement(name = "Fax", namespace = "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/facturaElectronicaCompra", required = false)
    protected TelefonoType fax;
    @XmlElement(name = "CorreoElectronico", namespace = "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/facturaElectronicaCompra", required = true)
    protected String correoElectronico;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public IdentificacionType getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(IdentificacionType identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public UbicacionType getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(UbicacionType ubicacion) {
        this.ubicacion = ubicacion;
    }

    public TelefonoType getTelefono() {
        return telefono;
    }

    public void setTelefono(TelefonoType telefono) {
        this.telefono = telefono;
    }

    public TelefonoType getFax() {
        return fax;
    }

    public void setFax(TelefonoType fax) {
        this.fax = fax;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }
}
