package com.itk.finance.web.screens.accountremainsdetail;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.AccountRemainsDetail;

@UiController("finance_AccountRemainsDetail.edit")
@UiDescriptor("account-remains-detail-edit.xml")
@EditedEntityContainer("accountRemainsDetailDc")
@LoadDataBeforeShow
public class AccountRemainsDetailEdit extends StandardEditor<AccountRemainsDetail> {
}