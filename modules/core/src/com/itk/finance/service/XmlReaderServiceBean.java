package com.itk.finance.service;

import com.haulmont.cuba.core.global.Resources;
import com.itk.finance.config.ExternalSystemConnectConfig;
import org.apache.poi.util.IOUtils;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.HashMap;

@Service(XmlReaderService.NAME)
public class XmlReaderServiceBean implements XmlReaderService {

    @Inject
    private Resources resources;
    @Inject
    private ClientService clientService;
    @Inject
    private ExternalSystemConnectConfig externalSystemConnectConfig;

    @Override
    public void getClientFromEdr() throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        InputStream stream = null;
        try {
            stream = resources.getResourceAsStream(externalSystemConnectConfig.getClientListUrl());
            XMLEventReader eventReader = factory.createXMLEventReader(stream);
            HashMap<String, String> current = null;
            StringBuilder qValue = new StringBuilder();
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT:
                        StartElement startElement = event.asStartElement();
                        if (startElement.getName().getLocalPart().equals("RECORD")) {
                            current = new HashMap<>();
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        Characters characters = event.asCharacters();
                        if (!characters.toString().equals("\n")) {
                            qValue.append(characters);
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        EndElement endElement = event.asEndElement();
                        setValueToMap(current, endElement.getName().getLocalPart(), qValue.toString());
                        qValue = new StringBuilder();
                        break;
                }
            }
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    private void setValueToMap(HashMap<String, String> clientMap, String propertyName, String propertyValue) {
        if (propertyName.equals("RECORD")) {
            clientService.fillClientEntity(clientMap);
        } else {
            clientMap.put(propertyName, propertyValue);
        }
    }
}