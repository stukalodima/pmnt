package com.itk.finance.service;

import com.itk.finance.entity.Client;

import javax.xml.stream.XMLStreamException;
import java.util.HashMap;
import java.util.UUID;

public interface ClientService {
    String NAME = "finance_ClientService";

    void getClientListfromExternal() throws XMLStreamException;
    Client getClientByEDRPOU(String EDRPOU);
    Client getClientById(String id);
    Client getClientById(UUID id);
    void fillClientEntity(HashMap<String, String> clientMap);
}