package com.itk.finance.web.screens.bank;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Bank;

@UiController("finance_Bank.edit")
@UiDescriptor("bank-edit.xml")
@EditedEntityContainer("bankDc")
@LoadDataBeforeShow
public class BankEdit extends StandardEditor<Bank> {
}