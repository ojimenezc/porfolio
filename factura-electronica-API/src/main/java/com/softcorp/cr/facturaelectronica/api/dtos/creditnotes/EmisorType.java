


/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.dtos.creditnotes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="Identificacion" type="{https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaCreditoElectronica}IdentificacionType"/>
 *         &lt;element name="NombreComercial" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="80"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Ubicacion" type="{https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaCreditoElectronica}UbicacionType"/>
 *         &lt;element name="Telefono" type="{https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaCreditoElectronica}TelefonoType" minOccurs="0"/>
 *         &lt;element name="Fax" type="{https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaCreditoElectronica}TelefonoType" minOccurs="0"/>
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
@XmlType(name = "EmisorType", namespace = "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaCreditoElectronica", propOrder = {
        "nombre",
        "identificacion",
        "nombreComercial",
        "ubicacion",
        "telefono",
        "fax",
        "correoElectronico"
})
public class EmisorType {

    @XmlElement(name = "Nombre", namespace = "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaCreditoElectronica", required = true)
    protected String nombre;
    @XmlElement(name = "Identificacion", namespace = "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaCreditoElectronica", required = true)
    protected IdentificacionType identificacion;
    @XmlElement(name = "NombreComercial", namespace = "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaCreditoElectronica")
    protected String nombreComercial;
    @XmlElement(name = "Ubicacion", namespace = "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaCreditoElectronica", required = true)
    protected UbicacionType ubicacion;
    @XmlElement(name = "Telefono", namespace = "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaCreditoElectronica", required = false)
    protected TelefonoType telefono;
    @XmlElement(name = "Fax", namespace = "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaCreditoElectronica", required = false)
    protected TelefonoType fax;
    @XmlElement(name = "CorreoElectronico", namespace = "https://cdn.comprobanteselectronicos.go.cr/xml-schemas/v4.3/notaCreditoElectronica", required = true)
    protected String correoElectronico;

    /**
     * Gets the value of the nombre property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the value of the nombre property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Gets the value of the identificacion property.
     *
     * @return possible object is
     * {@link IdentificacionType }
     */
    public IdentificacionType getIdentificacion() {
        return identificacion;
    }

    /**
     * Sets the value of the identificacion property.
     *
     * @param value allowed object is
     *              {@link IdentificacionType }
     */
    public void setIdentificacion(IdentificacionType value) {
        this.identificacion = value;
    }

    /**
     * Gets the value of the nombreComercial property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getNombreComercial() {
        return nombreComercial;
    }

    /**
     * Sets the value of the nombreComercial property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNombreComercial(String value) {
        this.nombreComercial = value;
    }

    /**
     * Gets the value of the ubicacion property.
     *
     * @return possible object is
     * {@link UbicacionType }
     */
    public UbicacionType getUbicacion() {
        return ubicacion;
    }

    /**
     * Sets the value of the ubicacion property.
     *
     * @param value allowed object is
     *              {@link UbicacionType }
     */
    public void setUbicacion(UbicacionType value) {
        this.ubicacion = value;
    }


    /**
     * Gets the value of the correoElectronico property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCorreoElectronico() {
        return correoElectronico;
    }

    /**
     * Sets the value of the correoElectronico property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCorreoElectronico(String value) {
        this.correoElectronico = value;
    }

}
