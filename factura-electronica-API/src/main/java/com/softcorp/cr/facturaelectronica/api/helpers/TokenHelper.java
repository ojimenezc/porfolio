/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.helpers;

import java.time.Instant;

public class TokenHelper {
    TokenHelper() {
    }

    private static String token;
    private static Instant expirationDate;

    public static String getToken() {

        if (null != expirationDate && Instant.now().compareTo(expirationDate) >= 0) {
            TokenHelper.token = null;
            return null;
        }

        return token;
    }

    public static void setExpirationDate(Instant expirationDate) {
        TokenHelper.expirationDate = expirationDate;
    }

    public static void setToken(String token) {
        TokenHelper.token = token;
    }
}
