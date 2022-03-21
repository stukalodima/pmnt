package com.itk.finance.web.screens.client;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Client;

@UiController("finance_Client.edit")
@UiDescriptor("client-edit.xml")
@EditedEntityContainer("clientDc")
@LoadDataBeforeShow
public class ClientEdit extends StandardEditor<Client> {
}