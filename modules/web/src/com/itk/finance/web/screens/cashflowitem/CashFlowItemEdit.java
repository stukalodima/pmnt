package com.itk.finance.web.screens.cashflowitem;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.CashFlowItem;

@UiController("finance_CashFlowItem.edit")
@UiDescriptor("cash-flow-item-edit.xml")
@EditedEntityContainer("cashFlowItemDc")
@LoadDataBeforeShow
public class CashFlowItemEdit extends StandardEditor<CashFlowItem> {
}