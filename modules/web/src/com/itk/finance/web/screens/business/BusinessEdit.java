package com.itk.finance.web.screens.business;

import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Business;

import javax.inject.Inject;

@UiController("finance_Business.edit")
@UiDescriptor("business-edit.xml")
@EditedEntityContainer("businessDc")
@LoadDataBeforeShow
public class BusinessEdit extends StandardEditor<Business> {
    @Inject
    private CheckBox usePaymentClaimApprovalField;

    @Subscribe("usePatmantClaimField")
    public void onUsePatmantClaimFieldValueChange(HasValue.ValueChangeEvent<Boolean> event) {
        usePaymentClaimApprovalField.setEditable(Boolean.TRUE.equals(event.getValue()));
        if (Boolean.FALSE.equals(event.getValue())){
            usePaymentClaimApprovalField.setValue(event.getValue());
        }
    }
}