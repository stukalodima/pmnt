package com.itk.finance.web.screens.account;

import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.TextInputField;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Account;
import com.itk.finance.entity.Bank;
import com.itk.finance.service.BankService;
import com.itk.finance.service.UserPropertyService;

import javax.inject.Inject;
import java.util.Objects;

@UiController("finance_Account.edit")
@UiDescriptor("account-edit.xml")
@EditedEntityContainer("accountDc")
@LoadDataBeforeShow
public class AccountEdit extends StandardEditor<Account> {
    @Inject
    private UserPropertyService userPropertyService;
    @Inject
    private BankService bankService;

    @Subscribe
    public void onInitEntity(InitEntityEvent<Account> event) {
        event.getEntity().setCompany(userPropertyService.getDefaultCompany());
    }

    @Subscribe("ibanField")
    public void onIbanFieldTextChange(TextInputField.TextChangeEvent event) {
        if(!Objects.isNull(event.getSource().getValue()) && event.getSource().getValue().toString().length() > 11) {
            String mfo = event.getSource().getValue().toString().substring(5, 11);
            Bank bank = bankService.getBankByMfo(mfo);
            getEditedEntity().setBank(bank);
        }
    }
}