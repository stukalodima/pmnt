package com.itk.finance.web.screens.company;

import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Company;
import com.itk.finance.service.UserPropertyService;

import javax.inject.Inject;
import java.util.Objects;

@UiController("finance_Company.edit")
@UiDescriptor("company-edit.xml")
@EditedEntityContainer("companyDc")
@LoadDataBeforeShow
public class CompanyEdit extends StandardEditor<Company> {
    @Inject
    private UserPropertyService userPropertyService;

    @Subscribe
    public void onInitEntity(InitEntityEvent<Company> event) {
        event.getEntity().setBusiness(userPropertyService.getDefaultBusiness());
    }
    @Subscribe("shortNameField")
    public void onShortNameFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        if (Objects.isNull(getEditedEntity().getName()) || Objects.equals(getEditedEntity().getName(), "")) {
            getEditedEntity().setName(event.getValue());
        }
    }
}