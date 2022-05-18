package com.itk.finance.web.screens.accounttype;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.AccountType;

@UiController("finance_AccountType.browse")
@UiDescriptor("account-type-browse.xml")
@LookupComponent("accountTypesTable")
@LoadDataBeforeShow
public class AccountTypeBrowse extends StandardLookup<AccountType> {
}