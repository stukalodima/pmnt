package com.itk.finance.service;

import com.haulmont.cuba.core.global.GlobalConfig;
import com.itk.finance.entity.xml.ClientXml;
import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.inject.Inject;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

@Service(XmlReaderService.NAME)
public class XmlReaderServiceBean implements XmlReaderService {
    @Inject
    private ClientService clientService;
    @Inject
    private GlobalConfig globalConfig;

    public void getClientFromEdrSax() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        XMLHandler handler = new XMLHandler();
        handler.setClientServiceHedler(clientService);
        parser.parse(globalConfig.getTempDir() + "\\CLIENT_EDR_UO.xml", handler);
    }

    private static class XMLHandler extends DefaultHandler {
        private ClientService clientServiceHedler;
        private ClientXml clientXml;
        private String informationFiled;
        @Override
        public void startElement (String uri, String localName, String qName, Attributes attributes) {
            if (qName.equals("RECORD")) {
                clientXml = new ClientXml();
            } else {
                informationFiled = "";
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String information = new String(ch, start, length);

            information = information.replace("\n", "").trim();

            if (!information.isEmpty()) {
                informationFiled = informationFiled.concat(information);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("RECORD".equals(qName)) {
                clientServiceHedler.fillClientEntity(clientXml);
            }else if ("SHORT_NAME".equals(qName)) {
                clientXml.setShortName(informationFiled);
            } else if ("NAME".equals(qName)) {
                clientXml.setName(informationFiled);
            } else if ("EDRPOU".equals(qName)) {
                clientXml.setEdrpou(informationFiled);
            } else if ("ADDRESS".equals(qName)) {
                clientXml.setAddress(informationFiled);
            } else if ("KVED".equals(qName)) {
                clientXml.setKved(informationFiled);
            } else if ("BOSS".equals(qName)) {
                clientXml.setBoss(informationFiled);
            } else if ("STAN".equals(qName)) {
                clientXml.setStan(informationFiled);
            }
        }

        public void setClientServiceHedler(ClientService clientServiceHedler) {
            this.clientServiceHedler = clientServiceHedler;
        }
    }
}