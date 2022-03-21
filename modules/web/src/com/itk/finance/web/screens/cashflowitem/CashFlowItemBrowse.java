package com.itk.finance.web.screens.cashflowitem;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.CashFlowItem;

@UiController("finance_CashFlowItem.browse")
@UiDescriptor("cash-flow-item-browse.xml")
@LookupComponent("cashFlowItemsTable")
@LoadDataBeforeShow
public class CashFlowItemBrowse extends StandardLookup<CashFlowItem> {
}