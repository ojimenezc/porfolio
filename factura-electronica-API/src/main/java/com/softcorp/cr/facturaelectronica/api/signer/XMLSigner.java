/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.signer;

import com.softcorp.cr.facturaelectronica.api.controllers.BaseController;
import com.softcorp.cr.facturaelectronica.api.entities.CompaniesEntity;
import com.softcorp.cr.facturaelectronica.api.security.encription.Encrypter;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import xades4j.XAdES4jException;
import xades4j.production.Enveloped;
import xades4j.production.XadesEpesSigningProfile;
import xades4j.production.XadesSigner;
import xades4j.production.XadesSigningProfile;
import xades4j.properties.IdentifierType;
import xades4j.properties.ObjectIdentifier;
import xades4j.properties.SignaturePolicyBase;
import xades4j.properties.SignaturePolicyIdentifierProperty;
import xades4j.providers.KeyingDataProvider;
import xades4j.providers.SignaturePolicyInfoProvider;
import xades4j.providers.impl.FileSystemKeyStoreKeyingDataProvider;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStoreException;
import java.util.Base64;

@Configuration
@ComponentScan("com.softcorp.cr.facturaelectronica.api.signer")
public class XMLSigner {
    protected Logger logger = LoggerFactory.getLogger(XMLSigner.class);
    @Autowired
    Encrypter encrypter;

    XmlHelper xmlHelper = new XmlHelper();

    public String sign(CompaniesEntity companyInfo, String policyURI, String documentInput) throws IOException, KeyStoreException, ParserConfigurationException, SAXException, XAdES4jException, TransformerException {

        String password = encrypter.decrypt(companyInfo.getCertificatePin());
        File temp = File.createTempFile(companyInfo.getIdentificationNumber(), ".p12");
        String keypath = temp.getPath();

        byte[] data = Base64.getDecoder().decode(encrypter.decrypt(companyInfo.getAtvCertificate()));
        try (OutputStream stream = new FileOutputStream(keypath)) {
            stream.write(data);
        }

        //establezco la politica de firma

        SignaturePolicyInfoProvider policyInfoProvider = new SignaturePolicyInfoProvider() {

            public SignaturePolicyBase getSignaturePolicy() {
                return new SignaturePolicyIdentifierProperty(
                        new ObjectIdentifier("oid:/1.2.4.0.9.4.5", IdentifierType.OIDAsURI, "Policy description"),
                        new ByteArrayInputStream(policyURI.getBytes()))
                        .withLocationUrl(policyURI);
            }
        };

        //ingreso la información de los certificados

        KeyingDataProvider kp = new FileSystemKeyStoreKeyingDataProvider(
                "pkcs12",
                keypath,
                new FirstCertificateSelector(),
                new DirectPasswordProvider(password),
                new DirectPasswordProvider(password),
                true);

        DocumentBuilderFactory factory = xmlHelper.getDocumentBuilderFactory();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = null;
        builder = factory.newDocumentBuilder();
        File inputFile = new File(documentInput);
        Document document = builder.parse(inputFile);
        Element elementToSign = document.getDocumentElement();

        // SignaturePolicyInfoProvider spi = new
        XadesSigningProfile p = new XadesEpesSigningProfile(kp, policyInfoProvider);
        XadesSigner signer = p.newSigner();

        // sign whole document
        new Enveloped(signer).sign(elementToSign);

        // save output file

        File tempOutput = File.createTempFile(companyInfo.getIdentificationNumber() + "signed", ".xml");

        String outputPath = tempOutput.getPath();
        Transformer transformer = xmlHelper.getTransformer();
        Result output = new StreamResult(new File(outputPath));
        Source input = new DOMSource(document);
        transformer.transform(input, output);
        Files.delete(Paths.get(inputFile.getPath()));

        //Once process is complete key is deleted
        try {
            Files.delete(Paths.get(temp.getPath()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return encodeFileToBase64Binary(outputPath);
    }

    private String encodeFileToBase64Binary(String fileName) throws IOException {
        File file = new File(fileName);
        byte[] encoded = Base64.getEncoder().encode(FileUtils.readFileToByteArray(file));
        Files.delete(Paths.get(file.getPath()));
        return new String(encoded, StandardCharsets.US_ASCII);
    }
}
