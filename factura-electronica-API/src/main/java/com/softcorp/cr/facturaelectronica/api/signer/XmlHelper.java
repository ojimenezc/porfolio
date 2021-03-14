

/*
 * Copyright (c) 2020. SOFTCORP-CR S.A
 * NOTICE:  All information contained herein is, and remains the property of SOFTCORP-CR S.A and its suppliers, if any.
 * The intellectual and technical concepts contained herein are proprietary to SOFTCORP-CR S.A and its suppliers and may be covered by Costa Rica and Foreign.
 * Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from SOFTCORP-CR S.A.
 */

package com.softcorp.cr.facturaelectronica.api.signer;

import com.softcorp.cr.facturaelectronica.api.controllers.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

@Configuration
@ComponentScan("com.softcorp.cr.facturaelectronica.api.signer")
public class XmlHelper {
    protected Logger logger = LoggerFactory.getLogger(XmlHelper.class);

    public Document getDocument(String path) throws ParserConfigurationException, IOException, SAXException {
        File fXmlFile = new File(path);
        DocumentBuilderFactory dbFactory = getDocumentBuilderFactory();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        return doc;
    }

    public Document getDocument(Object source, Class<?> type) throws ParserConfigurationException, IOException, SAXException {
        try {
            //Create JAXB Context
            JAXBContext jaxbContext = JAXBContext.newInstance(type);

            //Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            //Required formatting??
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            //Print XML String to Console
            StringWriter sw = new StringWriter();
            //Write XML to StringWriter
            jaxbMarshaller.marshal(source, sw);

            DocumentBuilderFactory dbFactory = getDocumentBuilderFactory();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new StringReader(sw.toString())));
            doc.getDocumentElement().normalize();
            return doc;

        } catch (JAXBException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public Transformer getTransformer() throws TransformerConfigurationException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        return transformerFactory.newTransformer();
    }

    public DocumentBuilderFactory getDocumentBuilderFactory() {
        DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
        df.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        df.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        return df;
    }
}
