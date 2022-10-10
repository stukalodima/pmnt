package com.itk.finance.web.screens.cashflowitembusinessalternativevalues;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.CashFlowItemBusinessAlternativeValues;

@UiController("finance_CashFlowItemBusinessAlternativeValues.edit")
@UiDescriptor("cash-flow-item-business-alternative-values-edit.xml")
@EditedEntityContainer("cashFlowItemBusinessAlternativeValuesDc")
@LoadDataBeforeShow
public class CashFlowItemBusinessAlternativeValuesEdit extends StandardEditor<CashFlowItemBusinessAlternativeValues> {
}