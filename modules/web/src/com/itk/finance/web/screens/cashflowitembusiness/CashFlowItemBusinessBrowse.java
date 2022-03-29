package com.itk.finance.web.screens.cashflowitembusiness;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.CashFlowItemBusiness;
import com.itk.finance.service.CashFlowItemsBusinessService;

import javax.inject.Inject;
import java.io.IOException;

@UiController("finance_CashFlowItemBusiness.browse")
@UiDescriptor("cash-flow-item-business-browse.xml")
@LookupComponent("cashFlowItemBusinessesTable")
@LoadDataBeforeShow
public class CashFlowItemBusinessBrowse extends StandardLookup<CashFlowItemBusiness> {
    @Inject
    private CashFlowItemsBusinessService cashFlowItemsBusinessService;
    @Inject
    private Notifications notifications;
    @Inject
    private Messages messages;
    @Inject
    private CollectionLoader<CashFlowItemBusiness> cashFlowItemBusinessesDl;

    @Subscribe("cashFlowItemBusinessesTable.fillCashFlowItems")
    public void onCashFlowItemBusinessesTableFillCashFlowItems(Action.ActionPerformedEvent event) {
        try {
            cashFlowItemsBusinessService.getCashFlowItemListfromExternal();
        } catch (IOException e) {
            notifications.create()
                    .withCaption(messages.getMessage(CashFlowItemBusinessBrowse.class,"message.restCall.error.caption"))
                    .withDescription(messages.getMessage(CashFlowItemBusinessBrowse.class,"message.restCall.error.text")
                            + "\n" + e.getMessage())
                    .withPosition(Notifications.Position.MIDDLE_CENTER)
                    .withType(Notifications.NotificationType.ERROR)
                    .show();
        }
        cashFlowItemBusinessesDl.load();
    }
}