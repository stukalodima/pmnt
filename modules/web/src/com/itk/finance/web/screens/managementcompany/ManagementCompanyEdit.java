package com.itk.finance.web.screens.managementcompany;

import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.ManagementCompany;

import java.util.Objects;

@UiController("finance_ManagementCompany.edit")
@UiDescriptor("management-company-edit.xml")
@EditedEntityContainer("managementCompanyDc")
@LoadDataBeforeShow
public class ManagementCompanyEdit extends StandardEditor<ManagementCompany> {

    @Subscribe("shortNameField")
    public void onShortNameFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        if (Objects.isNull(getEditedEntity().getName()) || Objects.equals(getEditedEntity().getName(), "")) {
            getEditedEntity().setName(event.getValue());
        }
    }
}