package com.itk.finance.web.screens.client;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Client;
import com.itk.finance.service.ClientService;

import javax.inject.Inject;
import javax.xml.stream.XMLStreamException;

@UiController("finance_Client.browse")
@UiDescriptor("client-browse.xml")
@LookupComponent("clientsTable")
@LoadDataBeforeShow
public class ClientBrowse extends StandardLookup<Client> {
    @Inject
    private ClientService clientService;
    @Inject
    private Notifications notifications;
    @Inject
    private Messages messages;

    @Subscribe("clientsTable.getClientListFromEdr")
    public void onClientsTableGetClientListFromEdr(Action.ActionPerformedEvent event) {
        try {
            clientService.getClientListfromExternal();
        } catch (XMLStreamException e) {
            notifications.create()
                    .withType(Notifications.NotificationType.ERROR)
                    .withPosition(Notifications.Position.MIDDLE_CENTER)
                    .withCaption(messages.getMessage(ClientBrowse.class, "message.xmlReader.error.caption"))
                    .withDescription(messages.getMessage(ClientBrowse.class, "message.xmlReader.error.text")
                            + "\n" + e.getMessage())
                    .show();
        } catch (Exception e) {
            notifications.create()
                    .withType(Notifications.NotificationType.ERROR)
                    .withPosition(Notifications.Position.MIDDLE_CENTER)
                    .withCaption(messages.getMessage(ClientBrowse.class, "message.nullPointer.error.caption"))
                    .withDescription(messages.getMessage(ClientBrowse.class, "message.nullPointer.error.text")
                            + "\n" + e.getMessage())
                    .show();
        }
    }
}