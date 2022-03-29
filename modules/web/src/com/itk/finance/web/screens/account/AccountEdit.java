package com.itk.finance.web.screens.account;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Account;
import com.itk.finance.service.UserPropertyService;

import javax.inject.Inject;

@UiController("finance_Account.edit")
@UiDescriptor("account-edit.xml")
@EditedEntityContainer("accountDc")
@LoadDataBeforeShow
public class AccountEdit extends StandardEditor<Account> {
    @Inject
    private UserPropertyService userPropertyService;

    @Subscribe
    public void onInitEntity(InitEntityEvent<Account> event) {
        event.getEntity().setCompany(userPropertyService.getDefaultCompany());
    }
}