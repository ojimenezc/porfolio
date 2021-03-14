

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

@Table(name = "products", schema = "facturaelectronica")
@Entity
public class ProductsEntity {
    @Id
    @GeneratedValue
    @JsonProperty(value = "id")
    private int id;

    @JsonProperty("taxType")
    @Column(name = "tax_code")
    private String taxType;

    @JsonProperty("rateType")
    @Column(name = "rate_type")
    private String rateType;

    @JsonProperty("unitMeasure")
    @Column(name = "unit_measure")
    private String unitMeasure;

    @JsonProperty("productName")
    @Column(name = "name")
    private String productName;

    @JsonProperty("productCode")
    @Column(name = "code")
    private String productCode;

    @JsonProperty("productPrice")
    @Column(name = "price")
    private float productPrice;

    @JsonProperty("prodCurrency")
    @Column(name = "currency")
    private String prodCurrency;


    @JsonProperty("customerId")
    @Column(name = "customer_id")
    private int customerId;

}
