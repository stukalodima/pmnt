package com.itk.finance.web.screens.bank;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Bank;

@UiController("finance_Bank.browse")
@UiDescriptor("bank-browse.xml")
@LookupComponent("banksTable")
@LoadDataBeforeShow
public class BankBrowse extends StandardLookup<Bank> {
}