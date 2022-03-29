package com.itk.finance.service;

import com.haulmont.cuba.core.global.DataManager;
import com.itk.finance.entity.Client;
import com.itk.finance.entity.ClientTypeEnum;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.xml.stream.XMLStreamException;
import java.util.HashMap;
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
    public void getClientListfromExternal() throws XMLStreamException {
        xmlReaderService.getClientFromEdr();
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
    public void fillClientEntity(HashMap<String, String> clientMap) {
        Client client = getClientByEDRPOU(clientMap.get("EDRPOU"));
        if (Objects.isNull(client)) {
            client = dataManager.create(Client.class);
        }
        client.setName(clientMap.get("NAME"));
        if (clientMap.get("SHORT_NAME").equals("")) {
            client.setShortName(client.getName());
        } else {
            client.setShortName(clientMap.get("SHORT_NAME"));
        }
        client.setEdrpou(clientMap.get("EDRPOU"));
        client.setAddress(clientMap.get("ADDRESS"));
        client.setKved(clientMap.get("KVED"));
        client.setBoss(clientMap.get("BOSS"));
        client.setStan(clientMap.get("STAN"));
        client.setClientType(ClientTypeEnum.JUR_OSOBA);

        dataManager.commit(client);
    }
}