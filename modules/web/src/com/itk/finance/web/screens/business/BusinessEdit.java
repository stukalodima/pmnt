package com.itk.finance.web.screens.business;

import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Business;
import com.itk.finance.service.UserPropertyService;

import javax.inject.Inject;

@UiController("finance_Business.edit")
@UiDescriptor("business-edit.xml")
@EditedEntityContainer("businessDc")
@LoadDataBeforeShow
public class BusinessEdit extends StandardEditor<Business> {
    @Inject
    private UserPropertyService userPropertyService;

    @Subscribe
    public void onInitEntity(InitEntityEvent<Business> event) {
        event.getEntity().setManagementCompany(userPropertyService.getDefaultManagementCompany());
    }

}