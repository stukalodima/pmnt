package com.itk.finance.web.screens.userproperty;

import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.global.UserSession;
import com.itk.finance.entity.Business;
import com.itk.finance.entity.Company;
import com.itk.finance.entity.ManagementCompany;
import com.itk.finance.entity.UserProperty;

import javax.inject.Inject;

@UiController("finance_UserProperty.edit")
@UiDescriptor("user-property-edit.xml")
@EditedEntityContainer("userPropertyDc")
@LoadDataBeforeShow
public class UserPropertyEdit extends StandardEditor<UserProperty> {
    @Inject
    private UserSession userSession;
    @Inject
    private CollectionLoader<Business> businessesDl;

    @Subscribe
    public void onInitEntity(InitEntityEvent<UserProperty> event) {
        event.getEntity().setUser(userSession.getUser());
    }
    @Inject
    private CollectionLoader<Company> companiesDl;

    @Subscribe("businessField")
    public void onBusinessFieldValueChange(HasValue.ValueChangeEvent<Business> event) {
        companiesDl.setParameter("business", event.getValue());
        companiesDl.load();
    }

    @Subscribe("managementCompanyField")
    public void onManagementCompanyFieldValueChange(HasValue.ValueChangeEvent<ManagementCompany> event) {
        businessesDl.setParameter("managementCompany", event.getValue());
        businessesDl.load();
    }
}