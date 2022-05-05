package com.itk.finance.web.screens.accountremains;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.AccountRemains;

@UiController("finance_AccountRemains.edit")
@UiDescriptor("account-remains-edit.xml")
@EditedEntityContainer("accountRemainsDc")
@LoadDataBeforeShow
public class AccountRemainsEdit extends StandardEditor<AccountRemains> {
}