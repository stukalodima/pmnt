package com.itk.finance.web.screens.bankgroup;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.BankGroup;

@UiController("finance_BankGroup.browse")
@UiDescriptor("bank-group-browse.xml")
@LookupComponent("bankGroupsTable")
@LoadDataBeforeShow
public class BankGroupBrowse extends StandardLookup<BankGroup> {
}