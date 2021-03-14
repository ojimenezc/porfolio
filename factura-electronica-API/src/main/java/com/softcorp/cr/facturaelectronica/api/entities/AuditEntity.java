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
import java.time.Instant;

@Table(name = "audit", schema = "facturaelectronica")
@Entity
public class AuditEntity {
    @Id
    @GeneratedValue
    @JsonProperty(value = "id")
    private int id;

    @JsonProperty("eventType")
    @Column(name = "event_type")
    private String eventType;

    @JsonProperty("info")
    @Column(name = "event_info")
    private String info;

    @JsonProperty("eventDate")
    @Column(name = "event_date")
    private Instant eventDate;

    @JsonProperty("tableModified")
    @Column(name = "table_modified")
    private String tableModified;

    @JsonProperty("beforeModificationValue")
    @Column(name = "before_modification_value")
    private String beforeModificationValue;

    @JsonProperty("afterModificationValue")
    @Column(name = "after_modification_value")
    private String afterModificationValue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Instant getEventDate() {
        return eventDate;
    }

    public void setEventDate(Instant eventDate) {
        this.eventDate = eventDate;
    }

    public String getTableModified() {
        return tableModified;
    }

    public void setTableModified(String tableModified) {
        this.tableModified = tableModified;
    }

    public String getBeforeModificationValue() {
        return beforeModificationValue;
    }

    public void setBeforeModificationValue(String beforeModificationValue) {
        this.beforeModificationValue = beforeModificationValue;
    }

    public String getAfterModificationValue() {
        return afterModificationValue;
    }

    public void setAfterModificationValue(String afterModificationValue) {
        this.afterModificationValue = afterModificationValue;
    }
}
