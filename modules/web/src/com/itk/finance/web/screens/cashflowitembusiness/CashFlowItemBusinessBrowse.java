package com.itk.finance.web.screens.cashflowitembusiness;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.CashFlowItemBusiness;

@UiController("finance_CashFlowItemBusiness.browse")
@UiDescriptor("cash-flow-item-business-browse.xml")
@LookupComponent("cashFlowItemBusinessesTable")
@LoadDataBeforeShow
public class CashFlowItemBusinessBrowse extends StandardLookup<CashFlowItemBusiness> {
}