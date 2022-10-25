package com.itk.finance.web.screens.bank;

import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupPickerField;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Bank;
import com.itk.finance.entity.BankGroup;

import javax.inject.Inject;

@UiController("finance_Bank.edit")
@UiDescriptor("bank-edit.xml")
@EditedEntityContainer("bankDc")
@LoadDataBeforeShow
public class BankEdit extends StandardEditor<Bank> {

    @Inject
    private LookupPickerField<BankGroup> bankGroupField;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        updateVisibility();
    }

    @Subscribe("parentBankField")
    public void onParentBankFieldValueChange(HasValue.ValueChangeEvent<Bank> event) {
        if (event.isUserOriginated()) {
            updateVisibility();
        }
    }

    public void updateVisibility() {
        bankGroupField.setVisible(getEditedEntity().equals(getEditedEntity().getParentBank()));
    }
}