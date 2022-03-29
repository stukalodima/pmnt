package com.itk.finance.web.screens.cashflowitembusiness;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.CashFlowItemBusiness;
import com.itk.finance.service.UserPropertyService;

import javax.inject.Inject;

@UiController("finance_CashFlowItemBusiness.edit")
@UiDescriptor("cash-flow-item-business-edit.xml")
@EditedEntityContainer("cashFlowItemBusinessDc")
@LoadDataBeforeShow
public class CashFlowItemBusinessEdit extends StandardEditor<CashFlowItemBusiness> {
    @Inject
    private UserPropertyService userPropertyService;

    @Subscribe
    public void onInitEntity(InitEntityEvent<CashFlowItemBusiness> event) {
        event.getEntity().setCompany(userPropertyService.getDefaultCompany());
    }
}