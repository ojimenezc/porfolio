

/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.repositories;

import com.softcorp.cr.facturaelectronica.api.entities.DocumentsEntity;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Configuration
@ComponentScan("com.softcorp.cr.facturaelectronica.api.repositories")
@Repository
public interface DocumentsRepository extends CrudRepository<DocumentsEntity, Integer> {
    @Query(value = "select * from documents d where d.document_key=?1",
            nativeQuery = true)
    DocumentsEntity getByDocumentKey(@Param("documentKey") String key);

    @Query(value = "SELECT * FROM facturaelectronica.documents WHERE company_id=?1", nativeQuery = true)
    ArrayList<DocumentsEntity> getByCompany(@Param("companyID") int companyId);

    @Query(value = "SELECT * FROM facturaelectronica.documents WHERE status_indicator='procesando' OR status_indicator IS NULL", nativeQuery = true)
    ArrayList<DocumentsEntity> getAllPendingDocuments();


}
