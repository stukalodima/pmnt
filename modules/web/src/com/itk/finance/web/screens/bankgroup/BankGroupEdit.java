package com.itk.finance.web.screens.bankgroup;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.BankGroup;

@UiController("finance_BankGroup.edit")
@UiDescriptor("bank-group-edit.xml")
@EditedEntityContainer("bankGroupDc")
@LoadDataBeforeShow
public class BankGroupEdit extends StandardEditor<BankGroup> {
}