package com.itk.finance.service;

import com.itk.finance.entity.Client;
import com.itk.finance.entity.xml.ClientXml;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public interface ClientService {
    String NAME = "finance_ClientService";

    void getClientListfromExternal() throws XMLStreamException, ParserConfigurationException, IOException, SAXException;
    Client getClientByEDRPOU(String EDRPOU);
    Client getClientById(String id);

    Client getClientById(UUID id);

    void fillClientEntity(ClientXml clientXml);
}