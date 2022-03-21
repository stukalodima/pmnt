package com.itk.finance.web.screens.client;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Client;

@UiController("finance_Client.browse")
@UiDescriptor("client-browse.xml")
@LookupComponent("clientsTable")
@LoadDataBeforeShow
public class ClientBrowse extends StandardLookup<Client> {
}