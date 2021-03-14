

/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.repositories;

import com.softcorp.cr.facturaelectronica.api.entities.ConsecutivesEntity;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Configuration
@ComponentScan("com.softcorp.cr.facturaelectronica.api.repositories")
@Repository
public interface ConsecutivesRepository extends CrudRepository<ConsecutivesEntity, Integer> {
    @Query(value = "select * from consecutives c where c.company_id=?1 and c.document_type=?4 and c.pos_id=?3 and c.office_id=?2",
            nativeQuery = true)
    ConsecutivesEntity getCurrentConsecutiveInformation(@Param("companyId") int companyId, @Param("officeID") int officeId, @Param("posId") int posID, @Param("documentType") int documentType);

    @Query(value = "select * from consecutives c where c.company_id=?1 and office_id=?2 and pos_id=?3",
            nativeQuery = true)
    List<ConsecutivesEntity> getCompanyConsecutives(@Param("companyId") int companyId, @Param("officeId") int officeId, @Param("posId") int posId);
}
