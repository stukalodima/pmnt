package com.itk.finance.web.screens.client;

import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Client;
import com.itk.finance.entity.ClientTypeEnum;

import java.util.Objects;

@UiController("finance_Client.edit")
@UiDescriptor("client-edit.xml")
@EditedEntityContainer("clientDc")
@LoadDataBeforeShow
public class ClientEdit extends StandardEditor<Client> {
    @Subscribe
    public void onInitEntity(InitEntityEvent<Client> event) {
        event.getEntity().setClientType(ClientTypeEnum.JUR_OSOBA);
    }
    @Subscribe("shortNameField")
    public void onShortNameFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        if (Objects.isNull(getEditedEntity().getName()) || Objects.equals(getEditedEntity().getName(), "")){
            getEditedEntity().setName(event.getValue());
        }
    }
}