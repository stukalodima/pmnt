package com.itk.finance.web.screens.accountremains;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.AccountRemains;

@UiController("finance_AccountRemains.browse")
@UiDescriptor("account-remains-browse.xml")
@LookupComponent("accountRemainsesTable")
@LoadDataBeforeShow
public class AccountRemainsBrowse extends StandardLookup<AccountRemains> {
}