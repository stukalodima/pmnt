package com.itk.finance.web.screens.accounttype;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.AccountType;

@UiController("finance_AccountType.edit")
@UiDescriptor("account-type-edit.xml")
@EditedEntityContainer("accountTypeDc")
@LoadDataBeforeShow
public class AccountTypeEdit extends StandardEditor<AccountType> {
}