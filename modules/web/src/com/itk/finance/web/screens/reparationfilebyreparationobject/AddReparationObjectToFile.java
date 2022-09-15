package com.itk.finance.web.screens.reparationfilebyreparationobject;

import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Business;
import com.itk.finance.entity.Company;
import com.itk.finance.entity.PropertyType;
import com.itk.finance.entity.ReparationObject;
import com.itk.finance.web.screens.reparationfilebyreparationobject.action.AddReparationObjectsAction;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@UiController("finance_AddReparationObjectToFile")
@UiDescriptor("add-reparation-object-to-file.xml")
public class AddReparationObjectToFile extends Screen {
    @Inject
    private GroupTable<ReparationObject> reparationObjectsTable;
    private Business business;

    private Company company;

    private PropertyType propertyType;

    @Inject
    private CollectionLoader<ReparationObject> reparationObjectsDl;
    @Inject
    private Label<String> businessLabel;
    @Inject
    private Messages messages;
    @Inject
    private MessageTools messageTools;
    @Inject
    private Label<String> businessValue;
    @Inject
    private Label<String> companyLabel;
    @Inject
    private Label<String> companyValue;
    @Inject
    private Label<String> propertyTypeLabel;
    @Inject
    private Label<String> propertyTypeValue;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        refreshReparationObjectDl();
    }

    @Install(to = "reparationObjectsTable.create", subject = "initializer")
    private void reparationObjectsTableCreateInitializer(ReparationObject reparationObject) {
        reparationObject.setBusiness(business);
        reparationObject.setCompany(company);
        reparationObject.setPropertyType(propertyType);
    }

    private void refreshReparationObjectDl() {
        if (business != null) {
            reparationObjectsDl.setParameter("business", business);
        } else {
            reparationObjectsDl.removeParameter("business");
        }
        if (company != null) {
            reparationObjectsDl.setParameter("company", company);
        } else {
            reparationObjectsDl.removeParameter("company");
        }
        if (propertyType != null) {
            reparationObjectsDl.setParameter("propertyType", propertyType);
        } else {
            reparationObjectsDl.removeParameter("propertyType");
        }
        reparationObjectsDl.load();
    }

    @Subscribe("lookupSelectAction")
    public void onLookupSelectActionClick(Button.ClickEvent event) {
        List<ReparationObject> reparationObjectList = new ArrayList<>(reparationObjectsTable.getSelected());
        AddReparationObjectsAction action = new AddReparationObjectsAction("lookupSelectAction", reparationObjectList);
        close(action);
    }

    @Subscribe("lookupCancelAction")
    public void onLookupCancelActionClick(Button.ClickEvent event) {
        closeWithDefaultAction();
    }

    public void setBusiness(Business business) {
        this.business = business;
        businessLabel.setValue(messageTools.getEntityCaption(business.getMetaClass()));
        businessValue.setValue(business.getName());
    }

    public void setCompany(Company company) {
        this.company = company;
        companyLabel.setValue(messageTools.getEntityCaption(company.getMetaClass()));
        companyValue.setValue(company.getShortName());
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
        propertyTypeLabel.setValue(messageTools.getEntityCaption(propertyType.getMetaClass()));
        propertyTypeValue.setValue(propertyType.getName());
    }
}