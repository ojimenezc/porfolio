

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
import java.util.List;

public class FirstCertificateSelector
        implements KeyStoreKeyingDataProvider.SigningCertSelector
{
    public X509Certificate selectCertificate(List<X509Certificate> availableCertificates) { return (X509Certificate)availableCertificates.get(0); }
}
