package com.itk.finance.web.screens.company;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Company;
import com.itk.finance.service.CompanyService;

import javax.inject.Inject;
import java.io.IOException;

@UiController("finance_Company.browse")
@UiDescriptor("company-browse.xml")
@LookupComponent("companiesTable")
@LoadDataBeforeShow
public class CompanyBrowse extends StandardLookup<Company> {
    @Inject
    private CompanyService companyService;
    @Inject
    private CollectionLoader<Company> companiesDl;
    @Inject
    private Dialogs dialogs;
    @Inject
    private Messages messages;

    @Subscribe("companiesTable.getCompanyList")
    public void onCompaniesTableGetCompanyList(Action.ActionPerformedEvent event) {
        try {
            companyService.getCompanyListFromExternal();
        } catch (IOException e) {
            dialogs.createMessageDialog()
                    .withCaption(messages.getMessage(CompanyBrowse.class, "messages.getCompanyListError.caption"))
                    .withMessage(messages.getMessage(CompanyBrowse.class, "messages.getCompanyListError.text")
                            + "\n" + e.getMessage())
                    .show();
        }
        companiesDl.load();
    }
}