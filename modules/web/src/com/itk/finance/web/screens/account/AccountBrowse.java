package com.itk.finance.web.screens.account;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Account;

@UiController("finance_Account.browse")
@UiDescriptor("account-browse.xml")
@LookupComponent("accountsTable")
@LoadDataBeforeShow
public class AccountBrowse extends StandardLookup<Account> {
}