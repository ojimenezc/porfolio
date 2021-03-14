

/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.signer;

import xades4j.providers.impl.KeyStoreKeyingDataProvider;

import java.security.cert.X509Certificate;

public class DirectPasswordProvider
        implements KeyStoreKeyingDataProvider.KeyStorePasswordProvider, KeyStoreKeyingDataProvider.KeyEntryPasswordProvider {
    private String password;

    DirectPasswordProvider(String password) {
        this.password = password;
    }

    public char[] getPassword() {
        return this.password.toCharArray();
    }

    public char[] getPassword(String entryAlias, X509Certificate entryCert) {
        return this.password.toCharArray();
    }
}
