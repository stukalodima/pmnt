package com.itk.finance.web.screens.company;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Company;

@UiController("finance_Company.edit")
@UiDescriptor("company-edit.xml")
@EditedEntityContainer("companyDc")
@LoadDataBeforeShow
public class CompanyEdit extends StandardEditor<Company> {
}