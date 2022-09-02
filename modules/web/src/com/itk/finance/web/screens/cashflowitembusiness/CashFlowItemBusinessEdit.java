package com.itk.finance.web.screens.cashflowitembusiness;

import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.LookupPickerField;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Business;
import com.itk.finance.entity.CashFlowItemBusiness;
import com.itk.finance.entity.Company;
import com.itk.finance.service.UserPropertyService;

import javax.inject.Inject;

@UiController("finance_CashFlowItemBusiness.edit")
@UiDescriptor("cash-flow-item-business-edit.xml")
@EditedEntityContainer("cashFlowItemBusinessDc")
@LoadDataBeforeShow
public class CashFlowItemBusinessEdit extends StandardEditor<CashFlowItemBusiness> {
    @Inject
    private UserPropertyService userPropertyService;
    @Inject
    private CollectionLoader<Company> companiesDl;
    @Inject
    private LookupPickerField<Company> companyField;

    @Subscribe
    public void onInitEntity(InitEntityEvent<CashFlowItemBusiness> event) {
        event.getEntity().setBusiness(userPropertyService.getDefaultBusiness());
    }

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        setParameterCompanyDl();
    }

    @Subscribe("businessField")
    public void onBusinessFieldValueChange(HasValue.ValueChangeEvent<Business> event) {
        if (event.isUserOriginated()) {
            if (getEditedEntity().getCompany() != null && !getEditedEntity().getCompany().getBusiness().equals(getEditedEntity().getBusiness())) {
                companyField.clear();
            }
            setParameterCompanyDl();
        }
    }

    @Subscribe("companyField")
    public void onCompanyFieldValueChange(HasValue.ValueChangeEvent<Company> event) {
        if (event.isUserOriginated()) {
            if (getEditedEntity().getCompany()!= null && getEditedEntity().getBusiness() == null) {
                getEditedEntity().setBusiness(getEditedEntity().getCompany().getBusiness());
            }
        }
    }

    private void setParameterCompanyDl() {
        companiesDl.setParameter("business", getEditedEntity().getBusiness());
        companiesDl.load();
    }
}