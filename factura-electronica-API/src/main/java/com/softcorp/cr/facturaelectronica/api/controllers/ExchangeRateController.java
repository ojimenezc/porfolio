

/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@RestController
@RequestMapping("api/exchangerate")
public class ExchangeRateController extends BaseController {
    protected Logger logger = LoggerFactory.getLogger(ExchangeRateController.class);

    @GetMapping(value = "/get", headers = "Accept=application/json")
    public ResponseEntity<Float> get() {
        try {
            RestTemplate restTemplate = new RestTemplate();

            LocalDateTime datetime = LocalDateTime.now();
            String today = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(datetime);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("Indicador", "318");
            map.add("FechaInicio", today);
            map.add("FechaFinal", today);
            map.add("Nombre", "SOFTCORP-CR S.A");
            map.add("SubNiveles", "NO");
            map.add("CorreoElectronico", "ojimenez@softcorp-cr.com");
            map.add("Token", "RTJFFMC3R1");

            String requestURL = "https://gee.bccr.fi.cr/Indicadores/Suscripciones/WS/wsindicadoreseconomicos.asmx/ObtenerIndicadoresEconomicos";

            ResponseEntity<String> response = restTemplate.postForEntity(requestURL, map, String.class);
            int firIndexOf = response.getBody().indexOf("<NUM_VALOR>") + 11;
            int lastIndexOf = response.getBody().indexOf("</NUM_VALOR>");
            String substring = response.getBody().substring(firIndexOf, lastIndexOf);
            return new ResponseEntity<>(Float.parseFloat(substring), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error getting exchange rate", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
