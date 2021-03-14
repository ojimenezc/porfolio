/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.tasks;

import com.softcorp.cr.facturaelectronica.api.entities.DocumentsEntity;
import com.softcorp.cr.facturaelectronica.api.helpers.DocumentsHelper;
import com.softcorp.cr.facturaelectronica.api.repositories.DocumentsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class StatusScheduledTask {
    protected Logger logger = LoggerFactory.getLogger(StatusScheduledTask.class);

    @Autowired
    DocumentsRepository documentsRepository;

    @Autowired
    DocumentsHelper documentsHelper;

    @Scheduled(fixedDelay = 2400000)
    public void getStatus() {

        ArrayList<DocumentsEntity> pendindDocuments = documentsRepository.getAllPendingDocuments();

        pendindDocuments.stream().forEach(doc -> {
            try {
                documentsHelper.processStatus(doc.getKey());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }
}
