package com.itk.finance.web.screens.business;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Business;

@UiController("finance_Business.browse")
@UiDescriptor("business-browse.xml")
@LookupComponent("businessesTable")
@LoadDataBeforeShow
public class BusinessBrowse extends StandardLookup<Business> {
}