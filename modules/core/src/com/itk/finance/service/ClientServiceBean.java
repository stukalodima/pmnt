package com.itk.finance.service;

import com.haulmont.cuba.core.global.DataManager;
import com.itk.finance.entity.Client;
import com.itk.finance.entity.ClientTypeEnum;
import com.itk.finance.entity.xml.ClientXml;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.inject.Inject;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service(ClientService.NAME)
public class ClientServiceBean implements ClientService {

    @Inject
    private DataManager dataManager;
    @Inject
    private XmlReaderService xmlReaderService;

    @Override
    public void getClientListfromExternal() throws ParserConfigurationException, IOException, SAXException {
        xmlReaderService.getClientFromEdrSax();
    }

    @Override
    public Client getClientByEDRPOU(String EDRPOU) {
        Client client = null;
         List<Client> clientList = dataManager.load(Client.class)
                 .query("select e from finance_Client e where e.edrpou = :edrpou")
                .parameter("edrpou", EDRPOU)
                .list();
         if (clientList.size() > 0) {
             client = clientList.get(0);
         }
        return client;
    }

    @Override
    public Client getClientById(String id) {
        UUID uuid = UUID.fromString(id);
        return getClientById(uuid);
    }

    @Override
    public Client getClientById(UUID id) {
        Client client = null;
        List<Client> clientList = dataManager.load(Client.class)
                .query("select e from finance_Client e where e.id = :id")
                .parameter("id", id)
                .list();
        if (clientList.size() > 0) {
            client = clientList.get(0);
        }
        return client;
    }

    @Override
    public void fillClientEntity(ClientXml clientXml) {
        Client client = getClientByEDRPOU(clientXml.getEdrpou());
        if (Objects.isNull(client)) {
            client = dataManager.create(Client.class);
        }
        client.setName(clientXml.getName());
        if (Objects.equals(clientXml.getShortName(), "")) {
            client.setShortName(clientXml.getName());
        } else {
            client.setShortName(clientXml.getShortName());
        }
        client.setEdrpou(clientXml.getEdrpou());
        client.setAddress(clientXml.getAddress());
        client.setKved(clientXml.getKved());
        client.setBoss(clientXml.getBoss());
        client.setStan(clientXml.getStan());
        client.setClientType(ClientTypeEnum.JUR_OSOBA);

        dataManager.commit(client);
    }
}