

/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.repositories;

import com.softcorp.cr.facturaelectronica.api.entities.ProductsEntity;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Configuration
@ComponentScan("com.softcorp.cr.facturaelectronica.api.repositories")
@Repository
public interface ProductsRepository extends CrudRepository<ProductsEntity, Integer> {

    @Query(value = "SELECT t.tax_code 'tax_code',rt.name 'tax_name', rt.code 'tax_rate_code'," +
            " rt.name 'rate_type',rt.ratePercentage 'tax_rate', p.unit_measure,p.customer_id,p.id,p.price,p.name,p.code,p.currency" +
            " FROM facturaelectronica.products p " +
            " INNER JOIN taxes t ON t.tax_code = p.tax_code" +
            " INNER JOIN rate_types rt ON rt.code=p.rate_type" +
            " WHERE customer_id = ?1", nativeQuery = true)
    Iterable<ProductsEntity> getByCustomer(@Param("customerId") int customerId);

    @Query(value = "SELECT t.tax_code 'tax_code',rt.name 'tax_name', rt.code 'tax_rate_code'," +
            " rt.name 'rate_type',rt.ratePercentage 'tax_rate', p.unit_measure,p.customer_id,p.id,p.price,p.name,p.code,p.currency" +
            " FROM facturaelectronica.products p " +
            " INNER JOIN taxes t ON t.tax_code = p.tax_code" +
            " INNER JOIN rate_types rt ON rt.code=p.rate_type" +
            " WHERE p.id = ?1", nativeQuery = true)
    ProductsEntity getSingle(@Param("prodId") int prodId);

}
