package com.itk.finance.web.screens.managementcompany;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.ManagementCompany;

@UiController("finance_ManagementCompany.browse")
@UiDescriptor("management-company-browse.xml")
@LookupComponent("managementCompaniesTable")
@LoadDataBeforeShow
public class ManagementCompanyBrowse extends StandardLookup<ManagementCompany> {
}