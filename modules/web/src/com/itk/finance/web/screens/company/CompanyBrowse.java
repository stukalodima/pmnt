package com.itk.finance.web.screens.company;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Company;

@UiController("finance_Company.browse")
@UiDescriptor("company-browse.xml")
@LookupComponent("companiesTable")
@LoadDataBeforeShow
public class CompanyBrowse extends StandardLookup<Company> {
}